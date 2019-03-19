package NavigationUnit;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Main {
	private static final Port usPort = LocalEV3.get().getPort("S1"); 		
	private static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(usPort);
	private static final EV3LargeRegulatedMotor leftMotor =
			new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor =
			new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
	public static final TextLCD lcd = LocalEV3.get().getTextLCD();
	public static final int FORWARD_SPEED = 100;
	public static final int ROTATE_SPEED = 50;
	public static final double WHEEL_RADIUS = 2.05;
	public static final double WHEEL_BASE = 9.5;
	public static final double TILE_SIZE = 30.48;
	public static int RedTeam = 1;
	public static int Corner = 0;
	public static int GreenTeam = 1; // Red=4, Yellow=3, Green=2, Blue=1
	public static int[] LL = new int[]{ 0,0}; 
	public static int[] UR = new int[]{ 4,9}; 
	public static int[] Island_LL = new int[]{ 6,0}; 
	public static int[] Island_UR = new int[]{ 15,9}; 
	public static int[] TN_LL = new int[]{ 4,7}; 
	public static int[] TN_UR = new int[]{ 6,8}; 
	public static int[] SZ_LL = new int[]{ 12,2}; 
	public static int[] SZ_UR = new int[]{ 14,5}; 
	public static EV3GyroSensor   gyrosensor;
	public static SampleProvider  gyro_sp;
	public static float [] gyro_sample;
	private static final Port gyroPort = LocalEV3.get().getPort("S4");
	private static final Port lightPort = LocalEV3.get().getPort("S3");
	public static EV3ColorSensor lightSensor = new EV3ColorSensor(lightPort);


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Uncomment following line if wifi class needed (don't forget to change server IP)
		// Wifi wifi = new Wifi();

	}

}
