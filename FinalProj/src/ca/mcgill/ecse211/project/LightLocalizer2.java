package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.project.Project;
import ca.mcgill.ecse211.odometer.*;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class LightLocalizer2 {

  private Odometer odometer; // The odometer instance
  private EV3LargeRegulatedMotor leftMotor; // The left motor of the robot
  private EV3LargeRegulatedMotor rightMotor; // The right motor of the robot
  private EV3ColorSensor colorSensor;	//Color Sensor
  private Navigation navigation; // The instance of sensor rotation
  
  double Radius=2.15; 
  double track=8.5; 
  public static final int RobotSize = 14;	// The distance from the light sensor to the center of the wheels
  
  
//  private SampleProvider myColorStatus; // The sample provider for the ultrasonic sensor
//  private float[] sampleColor; // The data buffer for the ultrasonic sensor reading
  
  public static final double TILE_SIZE = 30.48; // The tile size used for demo
  public static final int FACING_CORNER = 225; // Angle facing the corner
 

  double last = Math.PI; // Initialize the last variable to a specific number
  double current = 0; // last and current are both used for differential filter
  double[] BlackLines = new double[4]; // The x and y tile line detect angle, clockwise
  double xerror = 0; // The localization error in the x direction
  double yerror = 0; // The localization error in the y direction
  double terror = 0; // The localization error in angle

  
  
  private static final int FORWARD_SPEED = 100;
  public static final int ROTATION_SPEED = 50;
 
  private float[] RGBValues= new float[1];			//stores the sample returned by the color sensor
  private float ReferenceBrightness;			//This is the brightness of the board
  private float CurrentBrightness;			//brightness level returned by the color sensor, used to identify black lines
  double diffThreshold=4; 				//threshold for difference between normal tile brightness and black lines
  long correctionStart, correctionEnd;
  
  
  
  public LightLocalizer2(Odometer odo, EV3ColorSensor colorSensor, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Navigation navigation) throws OdometerExceptions {
	  this.odometer = odo;
	  this.colorSensor = colorSensor;
	  this.leftMotor = leftMotor;
	  this.rightMotor = rightMotor;
	  this.navigation = navigation;
  }

  public void doLocalization() {

	  leftMotor.setSpeed(ROTATION_SPEED);
	  rightMotor.setSpeed(ROTATION_SPEED);
	  leftMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, 45), true);
	  rightMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK, 45), false);
	  
	  leftMotor.setSpeed(FORWARD_SPEED);
	    rightMotor.setSpeed(FORWARD_SPEED);
	    
    
    
    //get the wooden brightness, which is the first value that appears on the colorSensor
    colorSensor.getRedMode().fetchSample(RGBValues, 0);
    ReferenceBrightness = RGBValues[0];
    
    
    while(true)
    {
    	leftMotor.forward();
	    rightMotor.forward();
    	if(filter())
    	{
//    		leftMotor.stop();
//            rightMotor.stop();
            break;   
    	}
    				
    }
    
//    while (rightMotor.isMoving() || leftMotor.isMoving()) {
//      if (filter()) {
//        leftMotor.stop(true);
//        rightMotor.stop(false);
//      }
//      try {
//        Thread.sleep(50);
//      } catch (Exception e) {
//      }
//    }

    //navigation.back(BACK_DIST, BACK_DIST);
    leftMotor.rotate(-convertDistance(Project.WHEEL_RAD, RobotSize), true); 
    rightMotor.rotate(-convertDistance(Project.WHEEL_RAD, RobotSize), false);
    
    leftMotor.stop();
    rightMotor.stop();
//    try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
    
    leftMotor.setSpeed(ROTATION_SPEED);
    rightMotor.setSpeed(ROTATION_SPEED);
    

     int i=0;
     while(true) {
    	 leftMotor.forward();
    	 rightMotor.backward();
        if (filter()) {
          BlackLines[i] = odometer.getXYT()[2];
          i++;
          if(i==1)
          {
        	  leftMotor.stop();
              rightMotor.stop();
              break;
          }
        	  
        }
        try {
          Thread.sleep(50);
        } catch (Exception e) {
        }
      }
      
     leftMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK, 15), true);
	  rightMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, 15), false);

//    xerror = RobotSize * Math.cos((BlackLines[3] - BlackLines[1]) / 2);
//    yerror = RobotSize * Math.cos((BlackLines[2] - BlackLines[0]) / 2);
//
//    terror = 270 - (BlackLines[3] + BlackLines[1]) / 2; 
//    double excessangle= odometer.getXYT()[2];
//    double extracorrection= excessangle - BlackLines[3];
//    
//    double terror= (BlackLines[1]-BlackLines[3])/2 -BlackLines[2]+ extracorrection; 
//    double correcttheta = odometer.getXYT()[2]+ terror ;
//    double thetaFinal =angleCorrection(correcttheta)+180;
//    // setting values in odometer
//    odometer.setXYT(xerror, yerror, thetaFinal);
//    navigation.travelTo(0, 0);
//    turnTo(180);
//    odometer.position[2] = 0;
//    odometer.setTheta(0);
//
//    navigation.forword(xerror, yerror);
//    //navigation.travelTo(xerror + odometer.getXYT()[0], yerror + odometer.getXYT()[1]);
//    navigation.turnTo(0);
//    odometer.setXYT(0, 0, 0);
  }

  boolean filter() { // Differential filter

	  
	  //correctionStart = System.currentTimeMillis();

      //get the current brightness level
      colorSensor.getRedMode().fetchSample(RGBValues, 0);
      CurrentBrightness = RGBValues[0];
       
      //They way to check whether the car has passed the black line is that if the difference between CurrentBrightness 
      //and WoodenBrightness is suddenly greater than the threshold, then the car must has passed a black line. 
      if(Math.abs(CurrentBrightness-ReferenceBrightness)*100 > diffThreshold) {	//if we have reached a black line, correct the odometer accordingly
    	  Sound.beep();	//Give us a signal that the sensor has read the black line
    	  return true;
      }
      else {
    	 return false;	//If not, then just keep going straight
      }
      
      
      
//    myColorStatus.fetchSample(sampleColor, 0); // Used for obtaining color reading from the
//                                               // SampleProvider
//
//    if (Math.abs(last - Math.PI) < Math.pow(0.1, 5)) { // If last has not been assigned for any
//                                                       // number yet
//      last = current = sampleColor[0];
//    } else {
//      last = current; // Update the last
//      current = sampleColor[0]; // Update the current
//    }
//
//    if ((current - last) / 0.01 < -0.7) { // If there is a black line detected
//      Sound.beepSequenceUp();
//      return true;
//    }
//    return false;
  }
  
  private static double angleCorrection(double angle){  // corrects the theta values
	    if (angle > 180) {
	      return angle - 360;
	    } else if (angle < -180) {
	      return angle + 360;
	    } else {
	      return angle;
	    }
	  }
  
  public void turnTo(double angle) {

	    leftMotor.setSpeed(100);
	    rightMotor.setSpeed(100);

	    leftMotor.rotate(convertAngle(Project.WHEEL_RAD, Project.TRACK, angle), true);
	    rightMotor.rotate(-convertAngle(Project.WHEEL_RAD, Project.TRACK, angle), false);

	  }
  
  private static int convertDistance(double radius, double distance) {
	    return (int) ((180.0 * distance) / (Math.PI * radius));
	  }

	  private static int convertAngle(double radius, double width, double angle) {
	    return convertDistance(radius, Math.PI * width * angle / 360.0);

	  }

}