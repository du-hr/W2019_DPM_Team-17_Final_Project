/**
 * This class is used to hold and release cans using the
 * claw structure that is set on the medium motor on the robot
 */
package FinalProject;

import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class ClawMovement {
  private static EV3MediumRegulatedMotor clawMotor;
  private static final int angle = 180;
  
  /**
   * This is the constructor for the class
   * @param clawMotor   the medium motor that holds the claw
   * @return Not used
   */
  public ClawMovement(EV3MediumRegulatedMotor clawMotor){
    ClawMovement.clawMotor = clawMotor;
  }

  /**
   * This method is called to hold a can (close the claw)
   * @return Not used
   */
 public static void holdCan() {
   clawMotor.setSpeed(50);
   clawMotor.rotate(-angle);
   clawMotor.stop(true);
 }

 /**
  * This method is called to release a can (open the claw)
  * @return Not used
  */
  public static void releaseCan() {
    clawMotor.setSpeed(50);
    clawMotor.rotate(angle);
    clawMotor.stop(true);
  }
}
