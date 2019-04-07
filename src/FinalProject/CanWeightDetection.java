package FinalProject;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import static FinalProject.CanColorDetection.*;

public class CanWeightDetection {
  private EV3MediumRegulatedMotor clawMotor;
  
  public CanWeightDetection(EV3MediumRegulatedMotor clawMotor) {
    this.clawMotor = clawMotor;
  }
  
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
