import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.Gyroscope;
import lejos.robotics.SampleProvider;

public class Main{

  // Motor Objects, and Robot related parameters
  private static final EV3LargeRegulatedMotor leftMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
  private static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
  private static final TextLCD lcd = LocalEV3.get().getTextLCD();
  public static final double WHEEL_RAD = 2.05;
  public static final double TRACK = 11.2;
  public static EV3GyroSensor   gyrosensor;
  public static SampleProvider  gyro_sp;
   public static float [] gyro_sample;
   private static final Port gyroPort = LocalEV3.get().getPort("S3");

  public static void main(String[] args) throws OdometerExceptions {

    int buttonChoice;

    // Odometer related objects
    Odometer odometer = Odometer.getOdometer(leftMotor, rightMotor, TRACK, WHEEL_RAD);
    OdometryCorrection odometryCorrection = new OdometryCorrection();
    gyrosensor = new EV3GyroSensor(gyroPort);
    gyro_sp = gyrosensor.getAngleAndRateMode();
    gyro_sample = new float[gyro_sp.sampleSize()];
    gyrosensor.reset();
    do {
      // clear the display
      lcd.clear();

      // ask the user whether the motors should drive in a square or float
      lcd.drawString("      start     ", 0, 0);

      buttonChoice = Button.waitForAnyPress(); // Record choice (left or right press)
    } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
      // clear the display
      lcd.clear();
      // Start odometer
      Thread odoThread = new Thread(odometer);
      odoThread.start();
      Thread odoCorrectionThread = new Thread(odometryCorrection);
      odoCorrectionThread.start();
      // spawn a new Thread to avoid SquareDriver.drive() from blocking
      (new Thread() {
        public void run() {
          SquareDriver.drive(leftMotor, rightMotor, WHEEL_RAD, WHEEL_RAD, TRACK);
        }
      }).start();

    while (Button.waitForAnyPress() != Button.ID_ESCAPE);
    System.exit(0);
  }
}