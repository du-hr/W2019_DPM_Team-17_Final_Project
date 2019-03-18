package NavigationUnit;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class MapDriver {
  private EV3MediumRegulatedMotor clawMotor;
  private EV3LargeRegulatedMotor leftMotor, rightMotor, colorSensorMotor;
  ClawMovement claw = ClawMovement.getClawMovement(clawMotor);
  //CanWeightDetection weightDetector = CanWeightDetection.getCanWeightDetection(leftMotor, rightMotor, colorSensorMotor, clawMotor);
  
  public MapDriver() {
    
  }
  public void holdCan() {
    // TODO to be fit to cross the bridge
    claw.holdCan();
  }
  
  // put it in Main
  //public void detectWeight() {
   // weightDetector.doWeightDetection();
 // }

}
