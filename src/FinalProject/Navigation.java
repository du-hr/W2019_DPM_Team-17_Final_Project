package FinalProject;

import Odometer.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import static FinalProject.Main.*;

public class Navigation {
  private static Odometer odometer;
  private static EV3LargeRegulatedMotor leftMotor;
  private static EV3LargeRegulatedMotor rightMotor;
  private static boolean isNavigating = false;

  public Navigation(Odometer odometer, EV3LargeRegulatedMotor leftMotor,
      EV3LargeRegulatedMotor rightMotor) {
  }

  public static Navigation getNavigator(Odometer odometer, EV3LargeRegulatedMotor leftMotor,
      EV3LargeRegulatedMotor rightMotor) {

    Navigation Navigator = new Navigation(odometer, leftMotor, rightMotor);
    return Navigator;
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

  private static void turnLeft(double angle) {
    leftMotor.rotate(-convertAngleForMotor(Math.abs(angle)), true);
    rightMotor.rotate(convertAngleForMotor(Math.abs(angle)), false);
  }

  private static void turnRight(double angle) {
    leftMotor.rotate(convertAngleForMotor(Math.abs(angle)), true);
    rightMotor.rotate(-convertAngleForMotor(Math.abs(angle)), false);
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

  private static int convertAngleForMotor(double angle) {
    return convertDistanceForMotor(Math.PI * WHEEL_BASE * angle / 360.0);
  }

}
