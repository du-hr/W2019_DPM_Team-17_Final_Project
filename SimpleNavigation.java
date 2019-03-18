

package ca.mcgill.ecse211.lab51;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import java.util.Arrays;

//import ca.mcgill.ecse211.lab51.*;
//import ca.mcgill.ecse211.odometer.*;


public class SimpleNavigation extends Thread {
  private EV3LargeRegulatedMotor leftMotor;
  private EV3LargeRegulatedMotor rightMotor;
  //We might not use usPort, we will see
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
   

  //Constructor of Navigation 
  //IMPORTANT: NO ODOMETER IN THIS CLASS!!!
  public SimpleNavigation(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
  }

  /**
   * TravelTo method to let the robot travel to the expected location 
   * @param x - x position that the robot should travel to
   * @param y - y coordinate that the robot should travel to 
   */

  public void travelTo (double x, double y) {

    isNavigating = true; 

    //calculate the change of x and y coordinates and 
    //the distance that the robot should travel 
    distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

    //Go straight after turn theta
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

  private int createMap(int[] TNR_LL, int[] TNR_UR, int[] SZR_LL, int[] SZR_UR, double[] x, double[] y)
  {
    double deltaX = TNR_LL[0]-2;
    double deltaY = TNR_LL[1]-1;
    int count=1;
    int time=0;

    x[count] = deltaX;
    y[count] = 0.0;
    count++;

    x[count] = x[count-1];
    y[count] = deltaY+0.5;
    count++;
    
    //Going through tunnel, may change speed
    deltaX = TNR_UR[0];
    x[count] = x[count-1];
    y[count] = y[count-1];
    count++;
    
    deltaY = SZR_LL[1]-1;
    x[count] = x[count-1];
    y[count] = deltaY;
    count++;
    
    deltaX = SZR_LL[0]-1;
    x[count] = deltaX;
    y[count] = y[count-1];
    count++;
    
    deltaY = SZR_UR[1]-1;
    x[count] = x[count-1];
    y[count] = deltaY;
    count++;
    
    deltaX = SZR_UR[0]-1;
    x[count] = deltaX;
    y[count] = y[count-1];
    count++;
    
    return count;
  }
  
  public void drive (double travelDist) {
		//Set the speed
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		//Rotate the motors until achieving the required distance
		leftMotor.rotate(convertDistance(Lab52.WHEEL_RAD, travelDist), true);
		rightMotor.rotate(convertDistance(Lab52.WHEEL_RAD, travelDist), false);

	}


  /**
   * Hello Aymar! Please read the following code carefully.
   * I assume that the starting point is (1, 1) as we discussed, but now the robot needs to face the positive x direction!
   * It doesn't really matter but it's good for my code.
   *
   * @param LLx
   * @param LLy
   * @param URx
   * @param URy
   */
  public void doNavigation(int[] TNR_LL, int[] TNR_UR, int[] SZR_LL, int[] SZR_UR)
  {
    double x[] = new double[100];
    double y[] = new double[100];
    Arrays.fill(x, -1);
    Arrays.fill(y, -1);

    int length = createMap(TNR_LL, TNR_UR, SZR_LL, SZR_UR, x, y);

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
      Sound.beep();


    }
  }
}
