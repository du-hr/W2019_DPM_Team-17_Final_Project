/**
 * This class runs the robot through the map
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
import lejos.hardware.sensor.EV3UltrasonicSensor;
import java.util.Arrays;
import ca.mcgill.ecse211.odometer.*;


public class SimpleNavigation extends Thread {
	//Motor related parameters
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	//Parameters related to the US sensor (used to detect the cans)
	private static final Port usPort = LocalEV3.get().getPort("S2");
	private static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(usPort);
	private float[] usData = new float[4];
	private int[] currentDist = new int[4];


	//initialize variables 
	private boolean isNavigating;
	private double distance;

	//initialize constants 
	private double track = 11.2;
	private double radius = 2.05;
	private static final int FORWARD_SPEED = 100; //lower speed can reduce slipping
	private static final int ROTATE_SPEED = 50;

	//create Odometer object 
	private Odometer odometer;  

	/**
	 * This is the constructor for the class 
	 * @param leftMotor  The left motor of the robot
	 * @param rightMotor The right motor of the robot
	 * @return Not used
	 */
	public SimpleNavigation(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		try {
			this.odometer = Odometer.getOdometer();
		} catch (OdometerExceptions e) {
			e.printStackTrace();
		}
	}

	/**
	 * TravelTo method to let the robot travel to the expected location 
	 * @param x - x position that the robot should travel to
	 * @param y - y coordinate that the robot should travel to 
	 */

	public void travelTo (double x, double y) {

		isNavigating = true;  
		distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		leftMotor.rotate(convertDistance(radius, distance), true);
		rightMotor.rotate(convertDistance(radius, distance), false);
		isNavigating = false;

	}

	/**
	 * let the robot turn to an expected angle
	 * @param Theta - the angle that the robot should turn to 
	 */
	public void turnTo (double Theta) {

		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		if(Math.abs(Theta)>180)
		{
			if(Theta>0)
				Theta=Theta-360;
			else
				Theta=Theta+360;
		}

		//turn rightward if theta is smaller than zero     
		if(Theta<0) {
			leftMotor.rotate(convertAngle(radius, track, Math.abs(Theta)), true);
			rightMotor.rotate(-convertAngle(radius, track, Math.abs(Theta)), false);
		}
		//turn leftward if theta is greater than zero
		else if (Theta>0){    
			leftMotor.rotate(-convertAngle(radius, track, Math.abs(Theta)), true);
			rightMotor.rotate(convertAngle(radius, track, Math.abs(Theta)), false);      
		}
	}

	/**
	 * is Navigating method: return a boolean 
	 * @return isNavigating
	 */
	public boolean isNavigating() {

		return isNavigating; 
	}

	/**
	 * convert distance method 
	 * @param radius - radius of the wheels 
	 * @param distance - the distance that the robot should travel
	 * @return the distance that the wheels should travel
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * convert angle method
	 * @param radius - the radius of the wheels 
	 * @param width - the distance between the two wheels 
	 * @param angle - the angle that the robot should turn 
	 * @return the convert distance method 
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

	/**
	 * This is the method to get the map to travel in
	 * @param LLx  The x coordinate of the lower left corner
	 * @param LLy the y coordinate of the lower left corner
	 * @param URx  The x coordinate of the upper right corner
	 * @param URy the y coordinate of the upper right corner
	 * @param x   The x values in the map
	 * @param y  The y values in the map
	 * @param isNavigate  boolean values identifying where the robot has traveled 
	 * @return Not used
	 */
	private int createMap(int LLx, int LLy, int URx, int URy, double[] x, double[] y, boolean[] isNavigate)
	{
		double deltaX = LLx-1;
		double deltaY = LLy-1;
		int count=1;
		int time=0;

		x[count] = deltaX-0.5;
		y[count] = 0.0;
		count++;

		x[count] = x[count-1];
		y[count] = deltaY;
		isNavigate[count] = true;
		count++;

		deltaX = URx-LLx;
		deltaY=URy-LLy;
		time=(int)((deltaX+1)/2);
		for(int a=0; a<time; a++)
		{
			for(int i=0; i<deltaY; i++)
			{
				x[count] = x[count-1];
				y[count] = y[count-1]+1;
				isNavigate[count] = true;
				count++;
			}

			x[count] = x[count-1];
			y[count] = y[count-1]+0.5;
			count++;
			x[count] = x[count-1]+2;
			y[count] = y[count-1];
			count++;
			x[count] = x[count-1];
			y[count] = y[count-1]-0.5;
			isNavigate[count] = true;
			count++;

			for(int i=0; i<deltaY; i++)
			{
				x[count] = x[count-1];
				y[count] = y[count-1]-1;
				isNavigate[count] = true;
				count++;
			}
		}

		if(deltaX%2==1)
		{
			x[count] = x[count-1];
			y[count] = y[count-1]+deltaY;
			count++;
		}
		else
		{
			for(int i=0; i<deltaY; i++)
			{
				x[count] = x[count-1];
				y[count] = y[count-1]+1;
				isNavigate[count] = true;
				count++;
			}
		}

		return count;
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
		leftMotor.rotate(convertDistance(Project.WHEEL_RADIUS, travelDist), true);
		rightMotor.rotate(convertDistance(Project.WHEEL_RADIUS, travelDist), false);

	}


	/**
	 *This is the method that runs the robot through the map
	 * @param LLx  The x coordinate of the lower left corner
	 * @param LLy the y coordinate of the lower left corner
	 * @param URx  The x coordinate of the upper right corner
	 * @param URy the y coordinate of the upper right corner
	 */
	public void doNavigation(int LLx, int LLy, int URx, int URy)
	{
		double x[] = new double[100];
		double y[] = new double[100];
		boolean isNavigate[] = new boolean[100];
		Arrays.fill(x, 0);
		Arrays.fill(y, 0);
		Arrays.fill(isNavigate, false);

		int length = createMap(LLx, LLy, URx, URy, x, y, isNavigate);

		int previousturn = 0;
		for (int i = 1; i<length; i++) {
			double deltaX = x[i]-x[i-1];
			double deltaY = y[i]-y[i-1];
			int currentturn = 0;
			int shouldturn;
			//+ is counterclockwise (left), - is clockwise (right)
			if(deltaY>0)
			{
				if(deltaX>0)
					currentturn=45;
				else if(deltaX==0)
					currentturn=90;
				else
					currentturn=135;
			}
			else if(deltaY<0)
			{
				if(deltaX>0)
					currentturn=-45;
				else if(deltaX==0)
					currentturn=-90;
				else
					currentturn=-135;
			}
			else
			{
				if(deltaX>0)
					currentturn=0;
				else if(deltaX<0)
					currentturn=180;
			}
			shouldturn = currentturn +previousturn;
			turnTo(shouldturn);
			previousturn=-currentturn;

			travelTo(deltaX * 30.48, deltaY * 30.48);

			if(isNavigate[i]==true)
			{
				usSensor.getDistanceMode().fetchSample(usData, 0);
				currentDist[0] = (int)(usData[0]*100);
				if(currentDist[0] < 20) {
					turnTo(90);
					drive(7);
					Project.colorDetection.getColor();
					drive(-7);
					turnTo(-90);
				}
			}
		}
	}
}
