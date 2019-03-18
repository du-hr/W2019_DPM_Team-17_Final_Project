/**
 * This class implements the falling edge and rising edge localization with ultrasonic sensor
 * 
 * @author Alex Lo,  260712192 
 * @author Aymar Muhikira,  260860188
 * @author Haoran Du
 * @author Charles Liu
 * @author David Schrier
 * @author Nicki Hu
 */
package ca.mcgill.ecse211.project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import ca.mcgill.ecse211.odometer.*;

class USLocalizer {
	//Parameters used to know the location (odometry) and navigate
	public static int ROTATION_SPEED = 100;
	private double deltaTheta;
	private Odometer odometer;
	//Parameters related to the ultrasonic sensor (input) and motors (output)
	private float[] usData = new float[4];
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private EV3UltrasonicSensor usSensor;
	private int[] currentDist = new int[4];
	//Parameters related to wall detection (falling and rising edge)
	private double d = 35.00;
	private double k = 3;

	/**
	 * This is the constructor for the class 
	 * @param odo   The odometer
	 * @param leftMotor  The left motor of the robot
	 * @param rightMotor The right motor of the robot
	 * @param usSensor   The ultrasonic sensor
	 * @return Not used
	 */
	public USLocalizer(Odometer odo, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
			EV3UltrasonicSensor usSensor) {
		this.odometer = odo;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.usSensor = usSensor;
		leftMotor.setSpeed(ROTATION_SPEED);
		rightMotor.setSpeed(ROTATION_SPEED);
	}

	/**
	 * This method performs the rising edge localization 
	 * @return Not used
	 */
	public void risingEdge() {
		double angleA, angleB, turningAngle;//Variables to use to get the 0° angle
		//Get the distance from the US sensor
		usSensor.getDistanceMode().fetchSample(usData, 0);
		currentDist[0] = (int)(usData[0]*100);
		//If the robot is not facing the wall, rotate anti-clockwise until facing it
		while (currentDist[0] > d) {
			leftMotor.backward();
			rightMotor.forward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Once the robot faces the wall, rotate anti-clockwise until not facing it anymore
		while (currentDist[0] < d + k) {
			leftMotor.backward();
			rightMotor.forward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Record the angle from the odometer when not facing the wall anymore
		angleA = odometer.getXYT()[2];
		//Rotate clockwise until facing the wall again
		while (currentDist[0] > d) {
			leftMotor.forward();
			rightMotor.backward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Continue rotating clockwise until not facing the wall again
		while (currentDist[0] < d + k) {
			leftMotor.forward();
			rightMotor.backward();
			usSensor.getDistanceMode().fetchSample(usData, 0);
			currentDist[0] = (int)(usData[0]*100);
		}
		//Record this second angle when not facing the wall
		angleB = odometer.getXYT()[2];
		leftMotor.stop(true);
		rightMotor.stop();
		//Calculate the change in angle of the robot from the 0° angle
		if (angleA < angleB) {
			deltaTheta = 45 - (angleA + angleB) / 2 + 180;
		} else if (angleA > angleB) {
			deltaTheta = 225 - (angleA + angleB) / 2 + 180;
		}
		//Calculate the angle by which to turn to face the 0° angle
		turningAngle = deltaTheta + odometer.getXYT()[2];
		//Turn towards the 0° angle
		leftMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK, turningAngle+4.5), true);
		rightMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, turningAngle+4.5), false);
		//Reset the values of the odometer
		odometer.setXYT(0.0, 0.0, 0.0);
	}

	/**
	 * This method performs the falling edge localization 
	 * @return Not used
	 */
	public void fallingEdge() {
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
		leftMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK, turningAngle - 1), true);
		rightMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, turningAngle - 1), false);
		//Reset the values of the odometer
		odometer.setXYT(0.0, 0.0, 0.0);
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
