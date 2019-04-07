package FinalProject;

import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class ClawMovement {
  private static EV3MediumRegulatedMotor clawMotor;
  private static final int angle = 180;
  
  public ClawMovement(EV3MediumRegulatedMotor clawMotor){
    ClawMovement.clawMotor = clawMotor;
  }

 public static void holdCan() {
   clawMotor.setSpeed(50);
   clawMotor.rotate(-angle);
   clawMotor.stop(true);
 }

  public static void releaseCan() {
    clawMotor.setSpeed(50);
    clawMotor.rotate(angle);
    clawMotor.stop(true);
  }
}
