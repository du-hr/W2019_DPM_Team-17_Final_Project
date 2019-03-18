package ca.mcgill.ecse211.odometer;

import ca.mcgill.ecse211.project.*;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class OdometryCorrection implements Runnable {
	// Variables related to the light sensor and the tile length
	private static final long CORRECTION_PERIOD = 10;
	private static final double TILE_LENGTH = 30.48;
	private static Port lightPort = LocalEV3.get().getPort("S3");
	private Odometer odometer;
	private SensorModes lightSensor;
	private SampleProvider color;
	private float[] colorData;
	private float prevColor = 0;
	private int numruns = 0;

	/**
	 * This is the default class constructor. An existing instance of the odometer is used. This is to
	 * ensure thread safety.
	 * 
	 * @throws OdometerExceptions
	 */
	public OdometryCorrection() throws OdometerExceptions {

		this.odometer = Odometer.getOdometer();
		// Initialize the light sensor
		this.lightSensor = Project.lightSensor;
		this.color = lightSensor.getMode("Red");
		this.colorData = new float[lightSensor.sampleSize()];

	}

	/**
	 * The method that corrects the odometer values.
	 * 
	 * @throws OdometerExceptions
	 */
	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;

		while (true) {
			correctionStart = System.currentTimeMillis();
			// Read the light intensity detected by the light sensor
			color.fetchSample(colorData, 0);
			// use a differential filter to detect lines
			float colordiff = prevColor - colorData[0];
			prevColor = colorData[0];
			numruns++;//This is used to ensure we do not have line readings that are too close
			//As that would be a mistake. It supplements the differential filter
			if(colordiff >= 0.05 && numruns > 10) {
				Sound.beep();
				numruns = 0;
				double theta = odometer.getXYT()[2];//Direction depending on the angle
				double[] XYT = odometer.getXYT();
				Project.gyro_sp.fetchSample(Project.gyro_sample, 0);
				double angle = Project.gyro_sample[0];
				if(angle < 0) {
					angle += 360;
				}
				angle = 360 - angle;
				//This is the first Y turn, so change the Y position accordingly
				if((theta <25 || theta > 335)||(theta<205 && theta > 155)) { //The angle suggests a y direction
					int y = (int)Math.round(XYT[1]/TILE_LENGTH);
					odometer.setY(y*TILE_LENGTH);
					System.out.println("Changed y to "+(y*TILE_LENGTH));
				}
				else if((theta > 75 && theta < 115)||(theta > 255 && theta < 290)) {//The angle suggests an x direction
					int x = (int)Math.round(XYT[0]/TILE_LENGTH);
					odometer.setY(x*TILE_LENGTH);
					System.out.println("Changed x to "+(x*TILE_LENGTH));
				}
				odometer.setTheta(angle);
				System.out.println("Changed angle to "+angle);
			}
			// this ensures the odometry correction occurs only once every period
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