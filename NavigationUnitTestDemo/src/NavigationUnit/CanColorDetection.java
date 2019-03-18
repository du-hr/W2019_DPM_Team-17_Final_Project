package NavigationUnit;


import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class CanColorDetection {
  private SampleProvider colorSensor;
  private float[] colorData;
  private EV3LargeRegulatedMotor colorSensorMotor;
  
  public CanColorDetection(SampleProvider colorSensor, float[] colorData,
      EV3LargeRegulatedMotor colorSensorMotor) {
    this.colorSensorMotor = colorSensorMotor;
    this.colorSensor = colorSensor;
    this.colorData = colorData;
  }
  
  public static CanColorDetection getCanColorDetection(SampleProvider colorSensor, float[] colorData,
      EV3LargeRegulatedMotor colorSensorMotor) {

    CanColorDetection canColorDetector = new CanColorDetection(colorSensor, colorData, colorSensorMotor);
    return canColorDetector;
}
  public void detectColor() {
  }
  
}