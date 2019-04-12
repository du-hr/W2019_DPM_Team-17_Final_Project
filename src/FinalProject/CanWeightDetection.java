/**
 * This class is used to detect the weight of a can and give feedback concerning
 * the weight and color of the can through beeps as specified in the requirements document
 */
package FinalProject;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import static FinalProject.CanColorDetection.*;

public class CanWeightDetection {
  private EV3MediumRegulatedMotor clawMotor;
  
  /**
   * This is the constructor for the class
   * @param clawMotor    the medium motor used to hold cans
   * @return Not used
   */
  public CanWeightDetection(EV3MediumRegulatedMotor clawMotor) {
    this.clawMotor = clawMotor;
  }
  
  /**
   * This is the method that performs the identification and gives the feedback
   * @return Not used
   */
  public static void detectCanWeight() {
    ClawMovement.holdCan();
    if (colorCode == 0) {
      Sound.playTone(500,1000);
      Sound.pause(300);
      Sound.playTone(500,1000);
      Sound.pause(300);
      Sound.playTone(500,1000);
      Sound.pause(300);
      Sound.playTone(500,1000);
      Sound.pause(300);
    }
    else if (colorCode == 1) {
      Sound.playTone(500,1000);
      Sound.pause(300);
      Sound.playTone(500,1000);
      Sound.pause(300);
      Sound.playTone(500,1000);
      Sound.pause(300);
    }
    else if (colorCode == 2) {
      Sound.playTone(500,1000);
      Sound.pause(300);
      Sound.playTone(500,1000);
      Sound.pause(300);
    }
    else {
      Sound.playTone(500,1000);
      Sound.pause(300);
    }
  }
}
