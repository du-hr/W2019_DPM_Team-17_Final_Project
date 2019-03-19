package NavigationUnit;

import Odometer.*;
import static NavigationUnit.Main.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class USLocalization {
	public static int ROTATION_SPEED = 100;
	private double deltaTheta;
	private Odometer odometer;
	//Parameters related to the ultrasonic sensor (input) and motors (output)
	private float[] usData = new float[4];
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private EV3UltrasonicSensor usSensor;
	private int[] currentDist = new int[4];
	//Parameters related to wall detection
	private double d = 35.00;
	private double k = 3;

	public USLocalization(Odometer odo,  EV3LargeRegulatedMotor leftMotor,
			EV3LargeRegulatedMotor rightMotor, EV3UltrasonicSensor usSensor) {
		// get incoming values for variables
		this.odometer = odo;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.usSensor = usSensor;
		leftMotor.setSpeed(ROTATION_SPEED);
		rightMotor.setSpeed(ROTATION_SPEED);
	}
	//I'm not sure why we had this
	/*public static USLocalization lightLocalizer(Odometer odometer, SampleProvider usSensor, float[] usData,
			EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {

		USLocalization usLocalizer = new USLocalization(odometer, usSensor, usData, leftMotor,rightMotor);
		return usLocalizer;
	}*/

	public void doUSLocalization() {
		double angleA, angleB, turningAngle; // Variables to use to get the 0° angle
		//Get the distance from the US sensor
		usSensor.getDistanceMode().fetchSample(usData, 0);
		currentDist[0] = (int)(usData[0]*100);
		//If the robot is facing the wall, rotate anti-clockwise until not facing it
		while (currentDist[0] < d + k) {
			leftMotor.backward();
			rightMotor.forward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Once the robot doesn't face the wall, rotate anti-clockwise until facing it again
		while (currentDist[0] > d) {
			leftMotor.backward();
			rightMotor.forward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Record the angle from the odometer when facing the wall
		angleA = odometer.getXYT()[2];
		//Rotate clockwise until not facing the wall again
		while (currentDist[0] < d + k) {
			leftMotor.forward();
			rightMotor.backward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Continue rotating clockwise until facing the wall again
		while (currentDist[0] > d) {
			leftMotor.forward();
			rightMotor.backward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Record this second angle when facing the wall
		angleB = odometer.getXYT()[2];
		leftMotor.stop(true);
		rightMotor.stop();
		//Calculate the change in angle of the robot from the 0° angle
		if (angleA < angleB) {
			deltaTheta = 45 - (angleA + angleB) / 2;

		} else if (angleA > angleB) {
			deltaTheta = 225 - (angleA + angleB) / 2;
		}
		//Calculate the angle by which to turn to face the 0° angle
		turningAngle = deltaTheta + odometer.getXYT()[2];
		//Turn towards the 0° angle
		leftMotor.rotate(-convertAngle(WHEEL_RADIUS, WHEEL_BASE, turningAngle - 1), true);
		rightMotor.rotate(convertAngle(WHEEL_RADIUS, WHEEL_BASE, turningAngle - 1), false);
		//Reset the values of the odometer
		odometer.setXYT(0.0, 0.0, 0.0);
	}

	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

}
