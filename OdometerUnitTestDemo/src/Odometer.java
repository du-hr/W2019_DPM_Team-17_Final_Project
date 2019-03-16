import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Odometer extends OdometerData implements Runnable {

	private OdometerData odoData;
	private static Odometer odo = null; // Returned as singleton

	// Motors and related variables
	private int leftMotorTachoCount;
	private int rightMotorTachoCount;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private int prevTachoR;
	private int prevTachoL;
	// Variables related to the distance traveled and the orientation of the robot
	private double distL;
	private double distR;
	private double deltaD;
	private double deltaT;
	private double dX;
	private double dY;
	private double Angle;
	// Size of track and wheel radius, used to compute the displacement and the change in angle
	private final double TRACK;
	private final double WHEEL_RAD;
	// odometer update period in ms
	private static final long ODOMETER_PERIOD = 25; 

	/**
	 * This is the default constructor of this class. It initiates all motors and variables once.It
	 * cannot be accessed externally.
	 * 
	 * @param leftMotor   
	 * 				the left motor
	 * @param rightMotor
	 * 				 the right motor
	 * @param TRACK
	 * 			 Length of the track
	 * @param WHEEL_RAD
	 * 			 The wheel radius
	 * @throws OdometerExceptions
	 */
	private Odometer(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
			final double TRACK, final double WHEEL_RAD) throws OdometerExceptions {
		odoData = OdometerData.getOdometerData(); // Allows access to x,y,z.
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		// Reset the values of x, y and z to 0
		odoData.setXYT(0, 0, 0);
		// Set the values of the tachometer count and angle to 0
		this.leftMotorTachoCount = 0;
		this.rightMotorTachoCount = 0;
		this.prevTachoL = 0;
		this.prevTachoR = 0;
		this.Angle = 0;
		// Initialize the values of track and wheel radius
		this.TRACK = TRACK;
		this.WHEEL_RAD = WHEEL_RAD;

	}

	/**
	 * This method is meant to ensure only one instance of the odometer is used throughout the code.
	 * 
	 * @param leftMotor
	 * 				the left motor
	 * @param rightMotor
	 * 				the right motor
	 * @param TRACK
	 * 			Length of the track
	 * @param WHEEL_RAD
	 * 			The wheel radius
	 * @return new or existing Odometer Object
	 * @throws OdometerExceptions
	 */
	public synchronized static Odometer getOdometer(EV3LargeRegulatedMotor leftMotor,
			EV3LargeRegulatedMotor rightMotor, final double TRACK, final double WHEEL_RAD)
					throws OdometerExceptions {
		if (odo != null) { // Return existing object
			return odo;
		} else { // create object and return it
			odo = new Odometer(leftMotor, rightMotor, TRACK, WHEEL_RAD);
			return odo;
		}
	}

	/**
	 * This class is meant to return the existing Odometer Object. It is meant to be used only if an
	 * odometer object has been created
	 * 
	 * @return error if no previous odometer exists
	 */
	public synchronized static Odometer getOdometer() throws OdometerExceptions {

		if (odo == null) {
			throw new OdometerExceptions("No previous Odometer exits.");

		}
		return odo;
	}

	/**
	 * This method implements the actual odometry by keeping track of the motor turns and calculating
	 * the new position accordingly
	 */
	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			//tachometer counts for each motor
			leftMotorTachoCount = leftMotor.getTachoCount();
			rightMotorTachoCount = rightMotor.getTachoCount();
			// Left and right displacement according to the difference in tachometer counts
			// between two consecutive readings
			distL = (Math.PI * WHEEL_RAD * (leftMotorTachoCount - prevTachoL))/180;
			distR = (Math.PI * WHEEL_RAD * (rightMotorTachoCount - prevTachoR))/180;
			// Update the tachometer counts
			prevTachoL = leftMotorTachoCount;
			prevTachoR = rightMotorTachoCount;
			//Calculate the displacement of the robot and the change in its angle (in rad) accordingly
			deltaD = 0.5*(distL + distR);
			deltaT = (distL - distR)/TRACK;
			// Update the angle
			Angle += deltaT ;
			// corresponding X and Y displacement
			dX = deltaD*Math.sin(Angle);
			dY = deltaD * Math.cos(Angle);
			// Updated odometer values with new calculated values and changed angle (converted to degrees)
			odo.update(dX, dY, 180*deltaT/Math.PI);

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done
				}
			}
		}
	}

}
