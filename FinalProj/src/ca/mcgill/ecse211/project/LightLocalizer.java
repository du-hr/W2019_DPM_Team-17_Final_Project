/**
 * This class performs the light localization
 * 
 * @author Alex Lo,  260712192 
 * @author Aymar Muhikira,  260860188
 * @author Haoran Du
 * @author Charles Liu
 * @author David Schrier
 * @author Nicki Hu
 */
package ca.mcgill.ecse211.project;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

import static ca.mcgill.ecse211.project.Project.gyro_sample;
import static ca.mcgill.ecse211.project.Project.gyro_sp;

import ca.mcgill.ecse211.odometer.*;

class LightLocalizer {
	//Parameters used to know the location (odometry) and navigate
	public static int ROTATION_SPEED = 100;
	private double SENSOR_DIST = 12;
	private Odometer odometer;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	public Navigation navigation;
	//Parameters related to the light sensor (input)
	private SensorModes lightSensor;
	private SampleProvider color;
	private float[] colorData;
	private float prevColor = 0;
	private int numLines = 0;
	private int timer = 0;
	private double[] lineAngle = new double[4];

	/**
	 * This is the constructor for the class 
	 * @param odometer   The odometer
	 * @param leftMotor  The left motor of the robot
	 * @param rightMotor The right motor of the robot
	 * @return Not used
	 */
	public LightLocalizer(Odometer odometer, EV3LargeRegulatedMotor leftMotor,
			EV3LargeRegulatedMotor rightMotor) {
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		navigation = new Navigation(odometer, leftMotor, rightMotor); //We create a navigation class
		this.lightSensor = Project.lightSensor;
		this.color = lightSensor.getMode("Red");
		this.colorData = new float[lightSensor.sampleSize()];
	}

	/**
	 * This method is used to bring the cart to the origin 
	 * @return Not used
	 */
	public void findOrigin() {
		navigation.turnTo(45);
		leftMotor.setSpeed(ROTATION_SPEED);
		rightMotor.setSpeed(ROTATION_SPEED);
		color.fetchSample(colorData, 0);
		// use a differential filter to detect lines
		float colordiff = prevColor - colorData[0];
		prevColor = colorData[0];
		while (colordiff < 0.05) {//We move forward until we detect a line
			color.fetchSample(colorData, 0);
			colordiff = prevColor - colorData[0];
			//prevColor = colorData[0];
			leftMotor.forward();
			rightMotor.forward();
		}
		Sound.beep();
		//Once a line is detected, we move backward a specific distance
		leftMotor.stop(true);
		rightMotor.stop();
		leftMotor.rotate(convertDistance(Project.WHEEL_RADIUS, -12), true);
		rightMotor.rotate(convertDistance(Project.WHEEL_RADIUS, -12), false);

	}

	/**
	 * This method is used to perform the light localization 
	 * @return Not used
	 */
	public void localize() {
		leftMotor.setSpeed(ROTATION_SPEED);
		rightMotor.setSpeed(ROTATION_SPEED);
		Project.gyrosensor.reset();
		color.fetchSample(colorData, 0);
		prevColor = colorData[0];
		//Start by getting close to the origin
		findOrigin();
		while (numLines < 4) {//Rotate and detect the 4 lines the sensor comes across
			leftMotor.forward();
			rightMotor.backward();
			color.fetchSample(colorData, 0);
			float colordiff = prevColor - colorData[0];
			//prevColor = colorData[0];
			timer++;
			if (colordiff >= 0.05) {
				timer = 0;
				lineAngle[numLines] =gyroFetch();//Store the angle for each line
				numLines++;
				Sound.beep();
			}
		}
		leftMotor.stop(true);
		rightMotor.stop();
		double dX, dY, thetax, thetay;//Variables used to calculate the 0° direction and the origin
		//From the 4 angles stored, calculate how off from the origin and 0° the robot is
		thetay = lineAngle[3] - lineAngle[1];
		thetax = lineAngle[2] - lineAngle[0];
		dX = -1 * SENSOR_DIST * Math.cos(Math.toRadians(thetay / 2));
		dY = -1 * SENSOR_DIST * Math.cos(Math.toRadians(thetax / 2));
		odometer.setXYT(dX, dY, odometer.getXYT()[2]-6);//Set the accurate current position
		navigation.travelTo(0.0, 0.0);//Navigate to the origin
		leftMotor.setSpeed(ROTATION_SPEED);
		rightMotor.setSpeed(ROTATION_SPEED);
		//Rotate to be in the 0° direction
		if (odometer.getXYT()[2] <= 350 && odometer.getXYT()[2] >= 10.0) {
			leftMotor.rotate(convertAngle(Project.WHEEL_RADIUS, Project.WHEEL_BASE, -odometer.getXYT()[2]), true);
			rightMotor.rotate(-convertAngle(Project.WHEEL_RADIUS, Project.WHEEL_BASE, -odometer.getXYT()[2]), false);
		}
		leftMotor.stop(true);
		rightMotor.stop();
	}

	public void startCorner() {
		int corner = Project.corner;
		if(corner == 0) {
			odometer.setXYT(Project.TILE_SIZE, Project.TILE_SIZE, 0.0);
			Project.gyrosensor.reset();
		}
	}

	private double gyroFetch() {
		Project.gyro_sp.fetchSample(Project.gyro_sample, 0);
		angleCorrection();
		return odometer.getXYT()[2];
	}

	private void angleCorrection() {
		Project.gyro_sp.fetchSample(Project.gyro_sample, 0);
		if (Project.gyro_sample[0] >= 0) {
			odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1],Project.gyro_sample[0]);
		}else {
			odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1], 360+Project.gyro_sample[0]);
		}
	}

	/**
	 * This method allows the conversion of a distance to the total rotation of each
	 * wheel need to cover that distance.
	 * 
	 * @param radius
	 * @param distance
	 * @return
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

}
