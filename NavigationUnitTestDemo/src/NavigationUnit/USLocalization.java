package NavigationUnit;

import Odometer.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class USLocalization {
  private Odometer odometer;
  private SampleProvider usSensor;
  private float[] usData;
  private EV3LargeRegulatedMotor leftMotor, rightMotor;
  
  public USLocalization(Odometer odometer, SampleProvider usSensor, float[] usData,
      EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
  // get incoming values for variables
  this.odometer = odometer;
  this.usSensor = usSensor;
  this.usData = usData;
  }
  
  public static USLocalization lightLocalizer(Odometer odometer, SampleProvider usSensor, float[] usData,
      EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {

    USLocalization usLocalizer = new USLocalization(odometer, usSensor, usData, leftMotor,rightMotor);
    return usLocalizer;
}
  
  public void doUSLocalization() {
    
  }

}
