package FinalProject;

import static FinalProject.Main.*;
import Odometer.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class USLocalization {
  //Parameters used to know the location (odometry) and navigate
  private static double deltaTheta;
  private static Odometer odometer;
  //Parameters related to the ultrasonic sensor (input) and motors (output)
  private static float[] usData = new float[4];
  private static EV3LargeRegulatedMotor leftMotor;
  private static EV3LargeRegulatedMotor rightMotor;
  private static SampleProvider usSensor;
  private static int[] currentDist = new int[4];
  //Parameters related to wall detection (falling and rising edge)
  private static double d = 40.00;
  private static double k = 5;
  
  /**
   * This is the constructor for the class 
   * @param odo        The odometer
   * @param leftMotor  The left motor of the robot
   * @param rightMotor The right motor of the robot
   * @param usSensor   The ultrasonic sensor
   * @return Not used
   */
  public USLocalization(Odometer odo, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
      SampleProvider usSensor) {
      USLocalization.odometer = odo;
      USLocalization.leftMotor = leftMotor;
      USLocalization.rightMotor = rightMotor;
      USLocalization.usSensor = usSensor;
  }

  public static void doUSLocalization() {
    // TODO Auto-generated method stub
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    fallingEdge();
  }
  /**
   * This method performs the falling edge localization 
   * @return Not used
   */
  public static void fallingEdge() {
      double angleA, angleB, turningAngle; // Variables to use to get the 0� angle
      //Get the distance from the US sensor
      usSensor.fetchSample(usData, 0);
      currentDist[0] = (int)(usData[0]*100);
      //If the robot is facing the wall, rotate anti-clockwise until not facing it
      while (currentDist[0] < d + k) {
          leftMotor.backward();
          rightMotor.forward();
          usSensor.fetchSample(usData, 0);
          currentDist[0] = (int)(usData[0]*100);
      }
      //Once the robot doesn't face the wall, rotate anti-clockwise until facing it again
      while (currentDist[0] > d) {
          leftMotor.backward();
          rightMotor.forward();
          usSensor.fetchSample(usData, 0);
          currentDist[0] = (int)(usData[0]*100);
      }
      //Record the angle from the odometer when facing the wall
      angleA = odometer.getXYT()[2];
      //Rotate clockwise until not facing the wall again
      while (currentDist[0] < d + k) {
          leftMotor.forward();
          rightMotor.backward();
          usSensor.fetchSample(usData, 0);
          currentDist[0] = (int)(usData[0]*100);
      }
      //Continue rotating clockwise until facing the wall again
      while (currentDist[0] > d) {
          leftMotor.forward();
          rightMotor.backward();
          usSensor.fetchSample(usData, 0);
          currentDist[0] = (int)(usData[0]*100);
      }
      //Record this second angle when facing the wall
      angleB = odometer.getXYT()[2];
      leftMotor.stop(true);
      rightMotor.stop();
      //Calculate the change in angle of the robot from the 0� angle
      if (angleA < angleB) {
          deltaTheta = 45 - (angleA + angleB) / 2;

      } else if (angleA > angleB) {
          deltaTheta = 225 - (angleA + angleB) / 2;
      }
      //Calculate the angle by which to turn to face the 0� angle
      turningAngle = deltaTheta + odometer.getXYT()[2];
      //Turn towards the 0� angle
      leftMotor.rotate(-convertAngle(WHEEL_RADIUS, WHEEL_BASE, turningAngle - 1), true);
      rightMotor.rotate(convertAngle(WHEEL_RADIUS, WHEEL_BASE, turningAngle - 1), false);
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
