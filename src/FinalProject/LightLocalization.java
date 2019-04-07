package FinalProject;

import static FinalProject.Main.*;
import Odometer.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class LightLocalization {

  // robot hardware parameter
  private static final double SENSOR_DIST = 12;

  private static Odometer odometer;
  private static EV3LargeRegulatedMotor leftMotor;
  private static EV3LargeRegulatedMotor rightMotor;
  private static SampleProvider colorSensor;
  private static float[] colorData;
  private static float prevColor;
  private static int numLines;
  private static double[] lineAngle = new double[4];

  public LightLocalization(Odometer odometer, EV3LargeRegulatedMotor leftMotor,
      EV3LargeRegulatedMotor rightMotor, SampleProvider colorSensor, float[] colorData) {
    LightLocalization.odometer = odometer;
    LightLocalization.leftMotor = leftMotor;
    LightLocalization.rightMotor = rightMotor;
    LightLocalization.colorSensor = colorSensor;
    LightLocalization.colorData = colorData;
  }


  public static void doLightLocalization() {
    prevColor = colorData[0];
    // Start by getting close to the origin
    findOrigin();
    while (numLines < 4) {// Rotate and detect the 4 lines the sensor comes across
      leftMotor.forward();
      rightMotor.backward();
      colorSensor.fetchSample(colorData, 0);
      float colordiff = prevColor - colorData[0];
      if (colordiff >= 0.05) {
        lineAngle[numLines] = Navigation.getGyroData();// Store the angle for each line
        numLines++;
      }
    }
    leftMotor.stop(true);
    rightMotor.stop();
    double dX, dY, thetax, thetay;// Variables used to calculate the 0° direction and the origin
    // From the 4 angles stored, calculate how off from the origin and 0° the robot is
    thetay = lineAngle[3] - lineAngle[1];
    thetax = lineAngle[2] - lineAngle[0];
    dX = -1 * SENSOR_DIST * Math.cos(Math.toRadians(thetay / 2));
    dY = -1 * SENSOR_DIST * Math.cos(Math.toRadians(thetax / 2));
    odometer.setXYT(dX, dY, odometer.getXYT()[2] - 10);// Set the accurate current position
    Navigation.travelTo(0.0, 0.0);// Navigate to the origin
    // Rotate to be in the 0° direction
    if (odometer.getXYT()[2] <= 358 && odometer.getXYT()[2] >= 2.0) {
      leftMotor.rotate(convertAngle(WHEEL_RADIUS, WHEEL_BASE, -odometer.getXYT()[2]), true);
      rightMotor.rotate(-convertAngle(WHEEL_RADIUS, WHEEL_BASE, -odometer.getXYT()[2]), false);
    }
    leftMotor.stop(true);
    rightMotor.stop();
  }

  private static void findOrigin() {
    Navigation.turnTo(45);
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    colorSensor.fetchSample(colorData, 0);
    // use a differential filter to detect lines
    float colordiff = prevColor - colorData[0];
    prevColor = colorData[0];
    while (colordiff < 0.05) {// We move forward until we detect a line
      colordiff = prevColor - colorData[0];
      // prevColor = colorData[0];
      leftMotor.forward();
      rightMotor.forward();
    }
    // Once a line is detected, we move backward a specific distance
    leftMotor.stop(true);
    rightMotor.stop();
    leftMotor.rotate(convertDistance(WHEEL_RADIUS, -12), true);
    rightMotor.rotate(convertDistance(WHEEL_RADIUS, -12), false);
  }


  /**
   * This method allows the conversion of a distance to the total rotation of each wheel need to
   * cover that distance.
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
