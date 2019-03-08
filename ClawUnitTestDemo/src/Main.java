import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class Main {

  public static final EV3LargeRegulatedMotor leftMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
  public static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  public static final EV3MediumRegulatedMotor clawMotor =
      new EV3MediumRegulatedMotor(LocalEV3.get().getPort("B"));
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();

  public static void main(String[] args) {
    // TODO Auto-generated method stub

      int buttonChoice;
      
      do {
        lcd.drawString("     Start      ", 0, 0);
        buttonChoice = Button.waitForAnyPress(); // Record choice (left or right press)
      } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
        
      CanRetriever.holdCan();
      while (Button.waitForAnyPress() != Button.ID_ESCAPE);
      System.exit(0);
  }

}
