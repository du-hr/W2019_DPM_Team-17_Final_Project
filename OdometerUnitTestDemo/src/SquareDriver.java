import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class SquareDriver {
  private static final int FORWARD_SPEED = 150;
  private static final int ROTATE_SPEED = 50;
  private static final double TILE_SIZE = 30.48;

	/**
	 * This method drives the robot in a square of size 3x3 tiles. It runs in parallel
	 * with the odometer and Odometer correction classes allow testing their functionality.
	 * 
	 * @param leftMotor
	 * @return The left motor of the EV3
	 * @param rightMotor
	 * @return The right motor of the EV3
	 * @param leftRadius
	 * @return Size of the left wheel radius
	 * @param rightRadius
	 * @return Size of the right wheel radius
	 * @param track
	 * @return the length of the track
	 */
	public static void drive(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
			double leftRadius, double rightRadius, double track) {
		// reset the motors
		for (EV3LargeRegulatedMotor motor : new EV3LargeRegulatedMotor[] {leftMotor, rightMotor}) {
			motor.stop();
			motor.setAcceleration(3000);
		}

		// Sleep for 2 seconds
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// There is nothing to be done here
		}

		for (int i = 0; i < 4; i++) {
			// drive forward two tiles
			leftMotor.setSpeed(FORWARD_SPEED);
			rightMotor.setSpeed(FORWARD_SPEED);

			rightMotor.rotate(convertDistance(leftRadius, 3 * TILE_SIZE), true);
			leftMotor.rotate(convertDistance(rightRadius, 3 * TILE_SIZE), false);

			// turn 90 degrees clockwise
			leftMotor.setSpeed(ROTATE_SPEED);
			rightMotor.setSpeed(ROTATE_SPEED);

			leftMotor.rotate(convertAngle(leftRadius, track, 90.0), true);
			rightMotor.rotate(-convertAngle(rightRadius, track, 90.0), false);
		}
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
