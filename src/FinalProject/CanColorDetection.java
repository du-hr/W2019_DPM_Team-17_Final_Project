package FinalProject;


import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class CanColorDetection {
  private static SampleProvider colorSensor;
  private static float[] colorData;
  private static EV3LargeRegulatedMotor colorSensorMotor;

  public static int colorCode; // 0: Red; 1: Yellow; 2: Green; 3: Blue
  
 //Parameters related to the readings obtained from testing (data acquisition)
  private static double[][] means =
    {{0.22, 0.73, 0.64}, {0.83, 0.54, 0.16}, {0.31, 0.93, 0.19}, {0.97, 0.18, 0.11}};

  public CanColorDetection(SampleProvider colorSensor, float[] colorData,
      EV3LargeRegulatedMotor colorSensorMotor) {
    CanColorDetection.colorSensorMotor = colorSensorMotor;
    CanColorDetection.colorSensor = colorSensor;
    CanColorDetection.colorData = colorData;
  }

  public static void detectCanColor() {
    colorSensorMotor.setSpeed(50);// Set the speed of the sensor Motor
    int colorNum = -1;// We have not yet identified the color
    int n = -1;// We have not started moving the motor
    while (colorNum == -1) {// While no color has been identified
      // Rotate the motor to a new position
      n++;
      colorSensorMotor.rotate(-5, true);
      // Fetch the color at that location
      colorSensor.fetchSample(colorData, 0);
      double R = colorData[0];
      double G = colorData[1];
      double B = colorData[2];
      // Normalize the RGB values
      double norm = Math.sqrt(R * R + G * G + B * B);
      R /= norm;
      G /= norm;
      B /= norm;
      // Compare (by Euclidean distance) the color to the known colors
      for (int i = 0; i < 4; i++) {
        double rMean = means[i][0];
        double gMean = means[i][1];
        double bMean = means[i][2];
        double d =
            Math.sqrt(Math.pow(R - rMean, 2) + Math.pow(G - gMean, 2) + Math.pow(B - bMean, 2));
        if (d <= 0.1) {// If the Euclidean distance is sufficiently low, we identify the color
          colorNum = i;
          System.out.println(R + "," + G + "," + B);
        }
      }
    }
    // Rotate the motor back to its initial position
    colorSensorMotor.rotate(n * 5, true);
    colorSensorMotor.stop();
    // Display the color name based on the color number identified
    if (colorNum == 1) {
      colorCode = 1;
    } else if (colorNum == 2) {
      colorCode = 2;
    } else if ((colorNum == 0)) {
      colorCode = 3;
    } else {
      colorCode = 0;
    }
  }
}

