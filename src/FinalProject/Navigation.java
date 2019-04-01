package FinalProject;

import Odometer.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;
import static FinalProject.Main.*;

public class Navigation {
  private static Odometer odometer;
  private static EV3LargeRegulatedMotor leftMotor;
  private static EV3LargeRegulatedMotor rightMotor;
  private static boolean isNavigating = false;
  private static SampleProvider gyroSensor;
  private static float[] gyroData;

  public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor,
      EV3LargeRegulatedMotor rightMotor, SampleProvider gyroSensor, float[] gyroData) {
    Navigation.odometer = odometer;
    Navigation.leftMotor = leftMotor;
    Navigation.rightMotor = rightMotor;
    Navigation.gyroSensor = gyroSensor;
    Navigation.gyroData = gyroData;
  }

  public static void travelTo(double x, double y) {

    x = x * TILE_SIZE;
    y = y * TILE_SIZE;


    // reset motors
    leftMotor.stop();
    rightMotor.stop();

    leftMotor.setAcceleration(3000);
    rightMotor.setAcceleration(3000);


    isNavigating = true;

    // calculate trajectory path and angle
    double[] odoData = odometer.getXYT();
    double X = odoData[0];
    double Y = odoData[1];
    double trajectoryX = x - X;
    double trajectoryY = y - Y;
    double trajectoryAngle = Math.toDegrees(Math.atan2(trajectoryX, trajectoryY));

    // rotate to correct angle
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    turnTo(trajectoryAngle);

    double trajectoryLine = Math.hypot(trajectoryX, trajectoryY);

    // move forward correct distance
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
    leftMotor.rotate(convertDistanceForMotor(trajectoryLine), true);
    rightMotor.rotate(convertDistanceForMotor(trajectoryLine), false);
  }

  // to make sure the angle of each turn is the minimum angle possible
  public static void turnTo(double heading) {
    double[] odoData = odometer.getXYT();
    double theta = odoData[2];

    double angle = heading - theta;


    if (angle < -180.0) {
      angle = angle + 360;
      turnRight(angle);
    } else if (angle > 180.0) {
      angle = angle - 360;
      turnLeft(angle);

    } else if (angle < 0) {
      turnLeft(angle);
    } else if (angle > 0) {
      turnRight(angle);
    }
  }

  private static void angleCorrection() {
    gyroSensor.fetchSample(gyroData, 0);
    if (gyroData[0] >= 0) {
      odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1], gyroData[0]);
    } else {
      odometer.setXYT(odometer.getXYT()[0], odometer.getXYT()[1], 360 + gyroData[0]);
    }
  }

  public static void turnLeft(double degree) {
    if (degree <= 1) {
      return;
    }
    int speed;
    double minAngle = 0;
    double angle = getGyroData();
    double angle1 = getGyroData();
    while ((Math.abs(angle - angle1 - degree) >= 1)
        && (Math.abs((angle1 - angle) - (360 - degree)) >= 1)) {
      minAngle = Math.min((Math.abs(angle - angle1 - degree)),
          Math.abs((angle1 - angle) - (360 - degree)));
      speed = (int) (80 - 25 / (minAngle + 1));
      leftMotor.setSpeed(speed);
      rightMotor.setSpeed(speed);
      leftMotor.backward();
      rightMotor.forward();
      angle1 = getGyroData();
    }
    leftMotor.stop(true);
    rightMotor.stop();
  }

  public static void turnRight(double degree) {
    if (degree <= 1) {
      return;
    }
    double minAngle = 0;
    int speed;
    double angle = getGyroData();
    double angle1 = getGyroData();
    while ((Math.abs(angle1 - angle - degree) >= 1)
        && (Math.abs((angle - angle1) - (360 - degree)) >= 1)) {
      minAngle = Math.min((Math.abs(angle1 - angle - degree)),
          Math.abs((angle - angle1) - (360 - degree)));
      speed = (int) (80 - 25 / (minAngle + 1));
      leftMotor.setSpeed(speed);
      rightMotor.setSpeed(speed);
      leftMotor.forward();
      rightMotor.backward();
      angle1 = getGyroData();
    }
    leftMotor.stop(true);
    rightMotor.stop();
  }


  /*
   * returns: whether or not the vehicle is currently navigating
   */
  public boolean isNavigating() {
    return isNavigating;
  }

  /**
   * This method allows the conversion of a distance to the total rotation of each wheel need to
   * cover that distance.
   * 
   * @param radius
   * @param distance
   * @return
   */
  private static int convertDistanceForMotor(double distance) {
    return (int) (360 * distance / (2 * Math.PI * WHEEL_RADIUS));
  }

  // gets the data from the color sensor, and returns a value corresponding
  // to the overall "brightness".
  private static double getGyroData() {
    gyroSensor.fetchSample(gyroData, 0);
    // we correct the angle in odometer and return it here as the
    // reading of heading angle from gyro sensor
    angleCorrection();
    return odometer.getXYT()[2];
  }


}
