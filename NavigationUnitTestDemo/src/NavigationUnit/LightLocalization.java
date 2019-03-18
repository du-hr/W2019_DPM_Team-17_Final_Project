package NavigationUnit;

import Odometer.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class LightLocalization {
  private Odometer odometer;
  private SampleProvider colorSensor;
  private float[] colorData;
  private EV3LargeRegulatedMotor leftMotor, rightMotor;
  
  public LightLocalization(Odometer odometer, SampleProvider colorSensor, float[] colorData,
      EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
  // get incoming values for variables
  this.odometer = odometer;
  this.colorSensor = colorSensor;
  this.colorData = colorData;
  }
  
  public static LightLocalization lightLocalizer(Odometer odometer, SampleProvider colorSensor, float[] colorData,
      EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {

    LightLocalization lightLocalizer = new LightLocalization(odometer, colorSensor, colorData, leftMotor,rightMotor);
    return lightLocalizer;
}
  
  public void doLightLocalization() {
    
  }

}
