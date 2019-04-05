package FinalProject;

import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class CanWeightDetection {
  private EV3MediumRegulatedMotor clawMotor;
  
  public CanWeightDetection(EV3MediumRegulatedMotor clawMotor) {
    this.clawMotor = clawMotor;
  }
  
  public static void detectCanWeight() {
    ClawMovement.holdCan();
  }
}
