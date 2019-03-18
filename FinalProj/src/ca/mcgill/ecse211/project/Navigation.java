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

import ca.mcgill.ecse211.odometer.*;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Navigation extends Thread {
	//Variables related to the motors and to the odometer
	private Odometer odometer;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	//Variables related to the position
	private double dX;
	private double dY;
	private double Xpos;
	private double Ypos;
	private double theta;
	private int LLx;
	private int LLy;
	private int URx;
	private int URy;
	private double x;
	private double y;
	//Variables related to the speed, tile size, and wheel radius 
	private static final int FORWARD_SPEED = 150;
	private static final int ROTATE_SPEED = 140;
	private static final double TILE_SIZE = 30.48;
	private double WHEEL_RAD;
	private boolean navigate = true;

	/**
	 * This is the constructor for the class 
	 * @param odometer   The odometer
	 * @param leftMotor  The left motor of the robot
	 * @param rightMotor The right motor of the robot
	 * @param WR         The wheel radius of the robot
	 * @return Not used
	 */
	public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
			double WR) {
		this.WHEEL_RAD = WR;
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}	

	/**
	 * This is the run method for navigation
	 * @return Not used
	 */
	public void run() {
		//Traveling through the map the robot has to cover
		travelTo(TILE_SIZE, TILE_SIZE);
		travelTo(0.0, 2*TILE_SIZE);
		travelTo(2*TILE_SIZE, 2 * TILE_SIZE);
		travelTo(2*TILE_SIZE, TILE_SIZE);
		travelTo(TILE_SIZE,0.0);
	}

	/**
	 * This is the method that drives the motors forward by a specified distance
	 * @param travelDist  The distance to travel
	 * @return Not used
	 */
	public void drive (double travelDist) {
		//Set the speed
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		//Rotate the motors until achieving the required distance
		leftMotor.rotate(convertDistance(WHEEL_RAD, travelDist), true);
		rightMotor.rotate(convertDistance(WHEEL_RAD, travelDist), false);

	}

	/**
	 * This is the method that travels the EV3 from the current position to a specified location
	 * @param x  The x coordinate to achieve
	 * @param y  The y coordinate to achieve
	 * @return Not used
	 */
	public void travelTo(double x, double y) {
		//Get the current position
		Xpos = odometer.getXYT()[0];
		Ypos = odometer.getXYT()[1];
		//Get the displacement needed in x and y
		dX = x - Xpos;
		dY = y - Ypos;
		//Calculate the current angle
		theta = (odometer.getXYT()[2]) ;
		//Calculate the angle to rotate
		double angle = Math.atan2(dX,dY) - theta*Math.PI/180;
		if(angle < - Math.PI)
			angle += 2*Math.PI;
		else if (angle > Math.PI)
			angle -= 2*Math.PI;
		//Calculate the euclidean distance to travel
		double dist = Math.sqrt(dX*dX + dY*dY);
		//Turn to the right direction and drive to the final position
		turnTo(angle);
		drive(dist);

	}
	/**
	 * This is the method used to turn by a minimal angle
	 * @param angle  The angle to turn by
	 * @return Not used
	 */
	public void turnTo(double angle) {
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		/*angle = angle*180/Math.PI;
		Project.gyro_sp.fetchSample(Project.gyro_sample, 0);
		double init = Project.gyro_sample[0];
		if(init < 0) {
			init += 360;
		}
		double turn = Project.gyro_sample[0] - init - angle;
		while(Math.abs(turn) > 0.1) {
			double an = Project.gyro_sample[0];
			if(Project.gyro_sample[0] < angle) {
				leftMotor.forward();
				rightMotor.backward();
			}
			else {
				leftMotor.backward();
				rightMotor.forward();
			}
		}*/
		//If the angle is positive, turn to the right
		if (angle > 0) {
			leftMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, (angle * 180) / Math.PI), true);
			rightMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK, (angle * 180) / Math.PI), false);

		} else {//If the angle is negative, turn to the left
			leftMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK,-((angle * 180) / Math.PI)), true);
			rightMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, -((angle * 180) / Math.PI)), false);
		}
	}

	/**
	 * This method is used to check if the robot is navigating
	 * @return navigate
	 */
	boolean isNavigating() throws OdometerExceptions {
		return navigate;
	}

	/**
	 * This method is used to set the value of the boolean navigate
	 * @param isnav  boolean to set navigate value to
	 * @return Not used
	 */
	void setisnav(boolean isnav){
		navigate=isnav;
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

