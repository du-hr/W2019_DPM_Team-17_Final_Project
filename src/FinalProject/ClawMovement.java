package FinalProject;

import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class ClawMovement {
  
  private EV3MediumRegulatedMotor clawMotor;
  
  public ClawMovement(EV3MediumRegulatedMotor clawMotor){
    
    this.clawMotor = clawMotor;
    
  }
  
  public static ClawMovement getClawMovement(EV3MediumRegulatedMotor clawMotor) {

      ClawMovement clawMover = new ClawMovement(clawMotor);
      return clawMover;
  }


  
 public static void holdCan() {
   
 }
}
