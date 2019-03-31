/*
 * OdometryCorrection.java
 */
package Odometer;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class OdometryCorrection implements Runnable {
	private static final long CORRECTION_PERIOD = 10;
	private static final double TILE_SIZE = 30.48;
	private static final double x0 = -15.24;
	private static final double y0 = -15.24;
	private int counterX = 0;
	private int counterY = 0;
	private static final double offset = 3.2;

	private Odometer odometer;
	Port portColor = LocalEV3.get().getPort("S4"); // 1. Get port
	SensorModes myColor = new EV3ColorSensor(portColor); // 2. Get sensor instance
	SampleProvider myColorStatus = myColor.getMode("RGB"); // 3. Get sample provider
	float[] sampleColor = new float[myColorStatus.sampleSize()]; // 4. Create data buffer
	
	
	

	/**
	 * This is the default class constructor. An existing instance of the odometer
	 * is used. This is to ensure thread safety.
	 * 
	 * @throws OdometerExceptions
	 */
	public OdometryCorrection() throws OdometerExceptions {

		this.odometer = Odometer.getOdometer();

	}

	/**
	 * Here is where the odometer correction code should be run.
	 * 
	 * @throws OdometerExceptions
	 */
	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		Sound.setVolume(50);
		odometer.setXYT(x0, y0, 0.0);

		while (true) {

			correctionStart = System.currentTimeMillis();

			double[] odoData = odometer.getXYT();
			double theta = odoData[2];
			double X = odoData[0];
			double Y = odoData[1];

			myColorStatus.fetchSample(sampleColor, 0);

			if (sampleColor[0] < 0.1) {
				Sound.beep();

				if (theta >= 320 || theta < 40) {

					odometer.setY(0 + counterY * TILE_SIZE );

					counterY++; // =3
					LCD.drawString("loop_1", 0, 5);

				}

				else if (theta >= 50 && theta < 130) {

					odometer.setX(0 + counterX * TILE_SIZE);

					counterX++; // =3
					LCD.drawString("loop_2", 0, 5);
				}

				else if (theta >= 140 && theta < 220) {

					odometer.setY((counterY - 1) * TILE_SIZE);

					counterY--; // =0
					LCD.drawString("loop_3", 0, 5);
				}

				else if (theta >= 230 && theta < 310) {

					odometer.setX((counterX - 1) * TILE_SIZE);
					odometer.setY(Y+ offset); // offset
					

					counterX--; // =0

					LCD.drawString("loop_4", 0, 5);

				}

				// this ensure the odometry correction occurs only once every period
				correctionEnd = System.currentTimeMillis();
				if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
					try {
						Thread.sleep(CORRECTION_PERIOD - (correctionEnd - correctionStart));
					} catch (InterruptedException e) {
						// there is nothing to be done here
					}
				}
			}
			
			

		}
	}
}
