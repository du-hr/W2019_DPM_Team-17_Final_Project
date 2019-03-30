package FinalProject;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class CanWeightDetection {
  private EV3LargeRegulatedMotor leftMotor, rightMotor, colorSensorMotor;
  private EV3MediumRegulatedMotor clawMotor;
  
  public CanWeightDetection(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, EV3LargeRegulatedMotor colorSensorMotor,
  EV3MediumRegulatedMotor clawMotor) {
    this.clawMotor = clawMotor;
    this.colorSensorMotor = colorSensorMotor;
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
  }
  
  public static CanWeightDetection getCanWeightDetector(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, EV3LargeRegulatedMotor colorSensorMotor,
      EV3MediumRegulatedMotor clawMotor) {

    CanWeightDetection canWeightDetector = new CanWeightDetection(leftMotor, rightMotor, colorSensorMotor, clawMotor);
    return canWeightDetector;
}
  
  public void doWeightDetection() {
    // TODO Coordinate the relative position of claw, color sensor, left, and right wheel to prepare
    // for weight detection
    
  }
}
