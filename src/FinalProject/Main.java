package FinalProject;

import Odometer.*;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;



public class Main {

  // Static Resources:
  // Left motor connected to output A
  // Right motor connected to output D
  // Claw motor connected to output B
  // Color sensor motor connected to output C
  // Ultrasonic sensor port connected to input S1
  // Color sensor (front) port connected to input S2
  // Color sensor (back) sensor port connected to input S3
  // Gyro sensor port connected to input S4
  private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
  private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  private static final EV3MediumRegulatedMotor clawMotor = new EV3MediumRegulatedMotor(LocalEV3.get().getPort("B"));
  private static final EV3LargeRegulatedMotor colorSensorMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
  private static final Port usPort = LocalEV3.get().getPort("S1");
  private static final Port frontColorPort = LocalEV3.get().getPort("S2");
  private static final Port backColorPort = LocalEV3.get().getPort("S3");
  public static final Port gyroPort = LocalEV3.get().getPort("S4");
  
  public static EV3GyroSensor gyro_Sensor;
  public static EV3ColorSensor back_sensor;
  
  // Robot hardware related parameters:
  public static final double WHEEL_RADIUS = 2.05;
  public static final double WHEEL_BASE = 9.5;
  public static final int ROTATE_SPEED = 100;
  public static final int FORWARD_SPEED = 150;
  
  // Project specifications:
  public static final double TILE_SIZE = 30.48;
  public static int corner = 3;
  public static int LLx = 0;
  public static int LLy = 5;
  public static int URx = 4;
  public static int URy = 9;
  public static int TN_LLx = 4;
  public static int TN_LLy = 7;
  public static int TN_URx = 6;
  public static int TN_URy = 6;
  public static int SZ_LLx = 7;
  public static int SZ_LLy = 6;
  public static int SZ_URx = 10;
  public static int SZ_URy = 9;
  
  public static void main(String[] args) throws OdometerExceptions, InterruptedException {
    
    // Setup ultrasonic sensor
    // 1. Create a port object attached to a physical port (done above)
    // 2. Create a sensor instance and attach to port
    // 3. Create a sample provider instance for the above and initialize operating mode
    // 4. Create a buffer for the sensor data
    SensorModes usSensor = new EV3UltrasonicSensor(usPort);
    SampleProvider usValue = usSensor.getMode("Distance"); // usValue provides samples from this instance
    float[] usData = new float[usValue.sampleSize()]; // usData is the buffer in which data are returned

    // Setup color sensor in the front
    // 1. Create a port object attached to a physical port (done above)
    // 2. Create a sensor instance and attach to port
    // 3. Create a sample provider instance for the above and initialize operating mode
    // 4. Create a buffer for the sensor data
    SensorModes frontColorSensor = new EV3ColorSensor(frontColorPort);
    SampleProvider frontColorValue = frontColorSensor.getMode("RGB"); // colorValue provides samples from this instance
    float[] frontColorData = new float[frontColorValue.sampleSize()]; // colorData is the buffer in which data are returned
    
    // Setup color sensor in the back
    // 1. Create a port object attached to a physical port (done above)
    // 2. Create a sensor instance and attach to port
    // 3. Create a sample provider instance for the above and initialize operating mode
    // 4. Create a buffer for the sensor data
    EV3ColorSensor backColorSensor = new EV3ColorSensor(backColorPort);
    back_sensor = backColorSensor;
   
    // Setup gyro sensor
    // 1. Create a port object attached to a physical port (done above)
    // 2. Create a sensor instance and attach to port
    // 3. Create a sample provider instance for the above and initialize operating mode
    // 4. Create a buffer for the sensor data
    EV3GyroSensor gyroSensor = new EV3GyroSensor(gyroPort);
    gyro_Sensor = gyroSensor;
    SampleProvider gyroValue = gyro_Sensor.getMode("Angle"); // gyroValue provides samples from this instance
    float[] gyroData = new float[gyroValue.sampleSize()]; // gyroData is the buffer in which data are returned

    // Setup the odometer
    Odometer odometer = Odometer.getOdometer(leftMotor, rightMotor, WHEEL_BASE, WHEEL_RADIUS);
    
    // Setup odometer related threads
    Thread odoThread = new Thread(odometer);
    odoThread.start();
    
    CanScanner canScanner = new CanScanner(usSensor,usData,leftMotor,rightMotor, odometer);
    Thread canScannerThread = new Thread(canScanner);
    canScannerThread.start();
    
    // Set up objects of classes
    @SuppressWarnings("unused")
    Navigation navigator = new Navigation(odometer, leftMotor, rightMotor, gyroValue, gyroData);
    MapDriver mapDriver = new MapDriver(odometer);
    @SuppressWarnings("unused")
    ClawMovement clawMovement = new ClawMovement(clawMotor);
    @SuppressWarnings("unused")
    CanColorDetection canColorDetector = new CanColorDetection(frontColorSensor, frontColorData, colorSensorMotor);
    @SuppressWarnings("unused")
    CanWeightDetection canWeightDetector = new CanWeightDetection(clawMotor);
    
    Wifi wifi = new Wifi();
    

    //USLocalization usLocalizer = new USLocalization(odometer, leftMotor, rightMotor, usSensor);
    //USLocalization.doUSLocalization();
    
    //LightLocalization lightLocalizer = new LightLocalization(odometer, leftMotor, rightMotor);
    //lightLocalizer.localize();
    
    
    Sound.beepSequenceUp();
    odometer.setXYT(1*TILE_SIZE, 8*TILE_SIZE, 0);
    mapDriver.drive();
  }
}


