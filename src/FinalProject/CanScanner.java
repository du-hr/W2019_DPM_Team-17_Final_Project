/**
 * This class is used to scan through 90° (clockwise) from a waypoint and detect
 * if there is a can within a one tile distance
 */
package FinalProject;

import static FinalProject.Main.ROTATE_SPEED;
import Odometer.Odometer;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class CanScanner implements Runnable {
  private EV3LargeRegulatedMotor leftMotor;
  private EV3LargeRegulatedMotor rightMotor;
  private Odometer odometer;
  private SampleProvider usSensor;
  private float[] usData;
  private static final double TIGGER_DISTANCE = 30.48;
  public static boolean isScanning = false;
  public static double initialHeading;
  public static double degreesOfTurning;
  public static double detectedCanDistance;


  // constructor
  /**
   * This is the constructor for the class
   * @param usSensor the ultrasonic sensor
   * @param usData   the array to store distance readings from the ultrasonic sensor
   * @param leftMotor  the left motor
   * @param rightMotor  the right motor
   * @param odometer    the odometer
   * @return Not used
   */
  public CanScanner(SampleProvider usSensor, float[] usData, EV3LargeRegulatedMotor leftMotor,
      EV3LargeRegulatedMotor rightMotor, Odometer odometer) {
    this.usSensor = usSensor;
    this.usData = usData;
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
    this.odometer = odometer;
  }
  
  /**
   * This method is where the logic for the can scanner will run. It performs a search
   * for a can once the boolean variable isScanning is set to true (done in the
   * MapDriver class).
   */
  // turning right 90 degrees and stop rotating until finds a can
  @Override
  public void run() {
    usSensor.fetchSample(usData, 0);
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    while (isScanning == true) {
      double[] odoData = odometer.getXYT();
      double angle = odoData[2];
      while (Math.abs(angle - initialHeading) <= 90) {
        leftMotor.forward();
        rightMotor.backward();
        if (usData[0] <= TIGGER_DISTANCE) {
          leftMotor.stop();
          rightMotor.stop();
          detectedCanDistance = usData[0];
          // the angle the robot turns on each side of the search zone
          degreesOfTurning = angle-initialHeading;
          isScanning = false;
        }
      }
    }

  }

}
