/**
 * This class is used to navigate on a specific map with no obstacle avoidance
 * 
 * @author Alex Lo,  260712192 
 * @author Aymar Muhikira,  260860188
 * @author Haoran Du
 * @author Charles Liu
 * @author David Schrier
 * @author Nicki Hu
 */
package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.odometer.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import static ca.mcgill.ecse211.project.Project.*;

public class Navigation {
	private static Odometer odometer;
	private static EV3LargeRegulatedMotor leftMotor;
	private static EV3LargeRegulatedMotor rightMotor;
	private static boolean navigating = false;



	public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor){
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.odometer = odometer;
	}

	public static Navigation getNavigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {

		Navigation navigator = new Navigation(odometer, leftMotor, rightMotor);
		return navigator;
	}

	public static void travelTo(double x, double y) {

		//x = x * TILE_SIZE;
		//y = y * TILE_SIZE;


		//reset motors
		leftMotor.stop();
		rightMotor.stop();

		leftMotor.setAcceleration(3000);
		rightMotor.setAcceleration(3000);


		navigating = true;

		//calculate trajectory path and angle
		double[] odoData = odometer.getXYT();
		double X = odoData[0];
		double Y = odoData[1];
		double trajectoryX = x - X;
		double trajectoryY = y - Y;
		double trajectoryAngle = Math.toDegrees(Math.atan2(trajectoryX, trajectoryY));

		//rotate to correct angle
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		turnTo(trajectoryAngle);
		double trajectoryLine = Math.hypot(trajectoryX, trajectoryY);
		//move forward correct distance
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		leftMotor.rotate(convertDistanceForMotor(trajectoryLine),true);
		rightMotor.rotate(convertDistanceForMotor(trajectoryLine),false);
	}
	private static double gyroFetch() {
		gyro_sp.fetchSample(gyro_sample, 0);
		angleCorrection();
		return odometer.getXYT()[2];
	}

	private static void angleCorrection() {
		gyro_sp.fetchSample(gyro_sample, 0);
		if (gyro_sample[0] >= 0) {
			odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1],gyro_sample[0]);
		}else {
			odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1], 360+gyro_sample[0]);
		}
	}
	//to make sure the angle of each turn is the minimum angle possible
	public static void turnTo(double heading) {
		double[] odoData = odometer.getXYT();
		//double theta = odoData[2];
		double theta = gyroFetch();
		//Replace previous line of code with previous comment to test correction
		//Also change turnRight and turnLeft with turnRight2 and turnLeft2
		double angle = heading-theta;


		if(angle < -180.0) {
			//angle = angle + 360;
			//turnRight(angle);
			turnRight2(360 - angle);
		} 
		else if (angle > 180.0) {
			//angle = angle - 360;
			//turnLeft(angle);
			turnLeft2(360 - angle);

		} 
		else if (angle < 0) {
			//turnLeft(angle);
			turnLeft2(Math.abs(angle));
		}
		else if(angle > 0) {
			//turnRight(angle);
			turnRight2(angle);
		}
	}


	private static void turnLeft (double angle) {
		leftMotor.rotate(-convertAngleForMotor(Math.abs(angle)),true);
		rightMotor.rotate(convertAngleForMotor(Math.abs(angle)),false);
	}

	private static void turnRight (double angle) {
		leftMotor.rotate(convertAngleForMotor(Math.abs(angle)),true);
		rightMotor.rotate(-convertAngleForMotor(Math.abs(angle)),false);
	}

	private static void turnLeft2(double degree) {
		if (degree <= 1) {
			return;
		}
		int speed;
		double minAngle = 0;
		double angle = gyroFetch();
		double angle1 = gyroFetch();
		while((Math.abs(angle - angle1 - degree)>=1) && (Math.abs((angle1 - angle) - (360-degree))>=1)){
			minAngle = Math.min((Math.abs(angle - angle1 - degree)), Math.abs((angle1 - angle) - (360-degree)));
			speed = (int)(80 - 25/(minAngle+1));
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
			leftMotor.backward();
			rightMotor.forward();
			angle1 = gyroFetch();
		}
		leftMotor.stop(true);
		rightMotor.stop();
	}

	private static void turnRight2(double degree) {
		if(degree <= 1) {
			return;
		}
		double minAngle = 0;
		int speed;
		double angle = gyroFetch();
		double angle1 = gyroFetch();
		while((Math.abs(angle1 - angle - degree)>=1) && (Math.abs((angle - angle1) - (360-degree))>=1)){
			minAngle = Math.min((Math.abs(angle1 - angle - degree)), Math.abs((angle - angle1) - (360-degree)));
			speed = (int)(80 - 25/(minAngle+1));
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
			leftMotor.forward();
			rightMotor.backward();
			angle1 = gyroFetch();
		}
		leftMotor.stop(true);
		rightMotor.stop();
	}

	/* returns: whether or not the vehicle is currently navigating
	 */
	public boolean isNavigating() {
		return navigating;
	}

	/**
	 * This method allows the conversion of a distance to the total rotation of each wheel need to
	 * cover that distance.
	 * 
	 * @param radius
	 * @param distance
	 * @return
	 */
	private static int convertDistanceForMotor(double distance){
		return (int) (360*distance/(2*Math.PI*WHEEL_RADIUS));
	}

	private static int convertAngleForMotor(double angle){
		return convertDistanceForMotor(Math.PI*WHEEL_BASE*angle/360.0);
	}


}
