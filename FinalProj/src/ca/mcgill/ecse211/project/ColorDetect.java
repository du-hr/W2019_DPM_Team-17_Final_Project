/**
 * This class performs the color detection
 * 
 * @author Alex Lo,  260712192 
 * @author Aymar Muhikira,  260860188
 * @author Haoran Du
 * @author Charles Liu
 * @author David Schrier
 * @author Nicki Hu
 */
package ca.mcgill.ecse211.project;

import java.util.Arrays;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

class ColorDetect {
	//Parameters related to the light sensor (input)
	private static Port colorPort = LocalEV3.get().getPort("S2");
	private SensorModes colorSensor;
	private SampleProvider color;
	private float[] colorData;
	//Parameters related to the median filter
	private double[] redData = new double[10] ;
	private double[] blueData= new double[10] ;
	private double[] greenData= new double[10] ;
	private static final int filterWindowSize = 3;
	//Initialize the motor used to move the light sensor
	private static final EV3MediumRegulatedMotor sensorMotor =
			new EV3MediumRegulatedMotor(LocalEV3.get().getPort("C"));
	//Parameters related to the readings obtained from testing (data acquisition)
	private static final double rMean_REDCAN=0.97, gMean_REDCAN=0.18, bMean_REDCAN=0.11;
	private static final double rMean_GREENCAN=0.31, gMean_GREENCAN=0.93, bMean_GREENCAN=0.19;
	private static final double rMean_BLUECAN=0.22, gMean_BLUECAN=0.73, bMean_BLUECAN=0.64;
	private static final double rMean_YELLOWCAN=0.83, gMean_YELLOWCAN=0.54, bMean_YELLOWCAN=0.16;
	private static double[][] means = {{0.22,0.73,0.64},{0.83,0.54,0.16},
			{0.31,0.93,0.19},{0.97,0.18,0.11}};
	/**
	 * This is the constructor for the class 
	 * @return Not used
	 */
	public ColorDetect() {
		//We create a navigation class
		this.colorSensor = new EV3ColorSensor(colorPort);
		this.color = colorSensor.getMode("RGB");
		this.colorData = new float[color.sampleSize()];
	}

	/**
	 * This method identifies the color of the can in front of the EV3
	 * @return Not used
	 */
	public void getColor() {
		//Clear the display to be used
		Project.lcd.clear();
		sensorMotor.setSpeed(50);//Set the speed of the sensor Motor
		int colorNum = -1;//We have not yet identified the color
		int n = -1;//We have not started moving the motor
		while(colorNum == -1) {//While no color has been identified
			//Rotate the motor to a new position
			n++;
			sensorMotor.rotate(-5,true);
			//Fetch the color at that location
			color.fetchSample(colorData, 0);
			double R = colorData[0];
			double G = colorData[1];
			double B = colorData[2];
			//Normalize the RGB values
			double norm = Math.sqrt(R*R + G*G + B*B);
			R /= norm;
			G /= norm;
			B /= norm;
			//Compare (by Euclidean distance) the color to the known colors
			for(int i = 0; i<4; i++) {
				double rMean = means[i][0];
				double gMean = means[i][1];
				double bMean = means[i][2];
				double d =  Math.sqrt(Math.pow(R-rMean, 2) + Math.pow(G-gMean, 2) + Math.pow(B-bMean, 2));
				if(d <= 0.1) {//If the Euclidean distance is sufficiently low, we identify the color
					colorNum = i;
					System.out.println(R+","+G+","+B);
				}
			}
		}
		//Rotate the motor back to its initial position
		sensorMotor.rotate(n*5,true);
		sensorMotor.stop();
		//If the color is the targeted one, beep a second time (first beep after identification
		if(colorNum == Project.TR-1)
			Sound.beep();
		//Display the color name based on the color number identified
		if(colorNum == 3) {
			Project.lcd.drawString("     Red        ", 0, 1);
		}
		else if(colorNum == 2) {
			Project.lcd.drawString("     Green      ", 0, 1);
		}
		else if((colorNum == 0)) {
			Project.lcd.drawString("     Blue       ", 0, 1);
		}
		else{
			Project.lcd.drawString("     Yellow     ", 0, 1);
		}
		Sound.beep();//Beep after color identification
	}
	/**
	 * This is an alternative method of color detection that uses median filter
	 * @return Not used
	 */
	public void gettColor() {
		Project.lcd.clear();
		sensorMotor.setSpeed(50);
		for (int i=0; i < 10; i++) {//Rotate the motor and get the 10 sample values
			color.fetchSample(colorData, 0);
			redData[i] = colorData[0];
			greenData[i] = colorData[1];
			blueData[i] = colorData[2];
			sensorMotor.rotate(18,true);
		}
		sensorMotor.rotate(-180,true);// Rotate back
		//Apply a median filter to the data
		double R = filteredMedian(redData,filterWindowSize);
		double G = filteredMedian(greenData,filterWindowSize);
		double B = filteredMedian(blueData,filterWindowSize);
		//Normalize the median value
		double norm = Math.sqrt(R*R + G*G + B*B);
		R /= norm;
		G /= norm;
		B /= norm;
		//Calculate the euclidean distance from each color
		double d_REDCAN =  Math.sqrt(Math.pow(R-rMean_REDCAN, 2) + Math.pow(G-gMean_REDCAN, 2) + Math.pow(B-bMean_REDCAN, 2));
		double d_GREENCAN =  Math.sqrt(Math.pow(R-rMean_GREENCAN, 2) + Math.pow(G-gMean_GREENCAN, 2) + Math.pow(B-bMean_GREENCAN, 2));
		double d_BLUECAN =  Math.sqrt(Math.pow(R-rMean_BLUECAN, 2) + Math.pow(G-gMean_BLUECAN, 2) + Math.pow(B-bMean_BLUECAN, 2));
		double d_YELLOWCAN =  Math.sqrt(Math.pow(R-rMean_YELLOWCAN, 2) + Math.pow(G-gMean_YELLOWCAN, 2) + Math.pow(B-bMean_YELLOWCAN, 2));
		double[] dData= new double[4] ;		
		dData[0] = d_REDCAN;
		dData[1] = d_GREENCAN;
		dData[2] = d_BLUECAN;
		dData[3] = d_YELLOWCAN;
		//Sort the values and identify the can as being the color of the smallest euclidean distance
		Arrays.sort(dData);		
		if(dData[0] == d_REDCAN) {
			Project.lcd.drawString("     Red        ", 0, 0);
		}
		else if(dData[0] == d_GREENCAN) {
			Project.lcd.drawString("     Green      ", 0, 0);
		}
		else if((dData[0] == d_BLUECAN)) {
			Project.lcd.drawString("     Blue       ", 0, 0);
		}
		else{
			Project.lcd.drawString("     Yellow     ", 0, 0);
		}
	}



	/**
	 * This method was used to acquire data about the colors 
	 * @return Not used
	 */
	public void testData() {
		sensorMotor.setSpeed(50);
		for (int i=0; i < 10; i++) {//Rotate the motor and get the 10 sample values
			color.fetchSample(colorData, 0);
			double R = colorData[0];
			double G = colorData[1];
			double B = colorData[2];
			double norm = Math.sqrt(R*R + G*G + B*B);
			R /= norm;
			G /= norm;
			B /= norm;
			System.out.println(R+","+G+","+B);
			sensorMotor.rotate(18,true);
		}
		sensorMotor.rotate(-180,true);
		sensorMotor.stop();
	}	

	/**
	 * This method was used to get the filtered median from an array of values given the window
	 * size for the filter
	 * @param array   The values to be filtered
	 * @param windowSize  The size of the window of the filter 
	 * @return the median value obtained
	 */
	private double filteredMedian(double[] array, int windowSize) {

		double[] resultArray = new double[array.length];
		double[] edgeProtectedArray = protectArrayEdge(array);

		for (int i = 0; i + windowSize - 1 < edgeProtectedArray.length; i++) {
			double[] windowArray = new double[windowSize];
			windowArray = getWindowArrary(edgeProtectedArray,i,windowSize);
			resultArray[i] = getMedian(windowArray);

		}

		double result = getMedian(resultArray);
		return result;
	}

	/**
	 * This method was used to get the median from an array
	 * @param windowArray   The values to be get the median from
	 * @return the median value obtained
	 */
	private double getMedian(double[] windowArray) {

		double median;
		Arrays.sort(windowArray);
		if (windowArray.length%2 == 1) {
			median = windowArray [(windowArray.length-1)/2];
		}
		else {
			median = 1/2 *(windowArray [(windowArray.length)/2 - 1] + windowArray [(windowArray.length)/2 ]);
		}

		return median;
	}

	/**
	 * This method was used iteratively get the window sample
	 * @param edgeProtectedArray   The Array to get the window from
	 * @param i   The starting position on the Array
	 * @param windowSize    The size of the window sample
	 * @return the window sample
	 */
	private double[] getWindowArrary(double[] edgeProtectedArray, int i, int windowSize) {
		double[] windowArray = new double[windowSize];
		for (int j = 0; j < windowSize; j++) {
			windowArray[j] = edgeProtectedArray[i];
			i++;	
		}
		return windowArray;
	}

	/**
	 * This method was used to get the edge protected array
	 * @param array   The normal array without edge protection
	 * @return the edgeProtectedArray
	 */
	private double[] protectArrayEdge(double[] array) {
		double[] edgeProtectedArray = new double[array.length+2];
		edgeProtectedArray[0] = array[0];
		edgeProtectedArray[array.length+1] = array[array.length-1];

		for (int i = 0; i < array.length; i++) {
			edgeProtectedArray[i+1] = array[i];
		}
		return edgeProtectedArray;

	}

}
