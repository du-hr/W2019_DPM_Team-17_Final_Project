
public class CanRetriever {

  public static void holdCan() {
    // TODO Auto-generated method stub
    Main.leftMotor.forward();
    Main.clawMotor.setSpeed(50);
    Main.clawMotor.rotate(270);
    Main.clawMotor.stop();
  }
  public static void releaseCan() {
    Main.clawMotor.setSpeed(50);
    Main.clawMotor.rotate(-270);
    Main.clawMotor.stop();
  }

}
