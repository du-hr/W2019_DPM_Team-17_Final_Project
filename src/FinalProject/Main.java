package FinalProject;

import Odometer.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;



public class Main {

  //get port for US sensor
  // Motor Objects, and Robot related parameters
  private static final EV3LargeRegulatedMotor leftMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
  private static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  public static final double WHEEL_RADIUS = 2.05;
  public static final double WHEEL_BASE = 9.5;
  public static final double TILE_SIZE = 30.48;
  public static final int ROTATE_SPEED = 50;
  public static final int FORWARD_SPEED = 100;
  // Parameters related to the map and color detection
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
  public static int corner = 3;
  //public static ColorDetect colorDetection = new ColorDetect();
  public static int TR = 3;
  public static EV3GyroSensor   gyrosensor;
  public static SampleProvider  gyro_sp;
  public static float [] gyro_sample;
  public static final Port gyroPort = LocalEV3.get().getPort("S4");

  
  public static void main(String[] args) throws OdometerExceptions{
    
    System.exit(0);
  }
}


