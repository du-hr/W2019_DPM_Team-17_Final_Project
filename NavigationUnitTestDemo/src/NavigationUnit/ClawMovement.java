package NavigationUnit;

import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class ClawMovement {
  
  private EV3MediumRegulatedMotor clawMotor;
  
  public ClawMovement(EV3MediumRegulatedMotor clawMotor){
    
    this.clawMotor = clawMotor;
    
  }
  
  public static ClawMovement getClawMovement(EV3MediumRegulatedMotor clawMotor) {

      ClawMovement claw = new ClawMovement(clawMotor);
      return claw;
  }


  
 public static void holdCan() {
   
 }
}
