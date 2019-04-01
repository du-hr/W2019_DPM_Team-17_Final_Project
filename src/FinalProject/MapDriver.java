package FinalProject;


import static FinalProject.Navigation.*;
import Odometer.OdometryCorrection;
import lejos.robotics.SampleProvider;
import Odometer.OdometerExceptions;
import static FinalProject.Main.*;


public class MapDriver{
  private static SampleProvider usSensor;
  private static float[] usData;
  //default constructor
  public MapDriver(SampleProvider usSensor, float[] usData) {
    MapDriver.usSensor = usSensor;
    MapDriver.usData = usData;
  }

  public void drive() throws OdometerExceptions{
    OdometryCorrection odometryCorrection = new OdometryCorrection();
    Thread odoCorrectionThread = new Thread(odometryCorrection);
    moveToStartingPosition();
    odoCorrectionThread.start();
    moveToBridge();
    odoCorrectionThread.interrupt();
    travelThroughBridge();
    odoCorrectionThread.start();
    moveToSearchZone();
    odoCorrectionThread.interrupt();
    searchCan();
    detectCanColor();
    detectCanWeight();
    moveCan();
  }


  private void moveToStartingPosition() {
    ClawMovement.holdCan();
    travelTo(1.5, 1); // travel to start point after doing localization
    turnTo(0); //turn heading to the positive y axis
  }

  // positive y axis is 0 degree axis
  public void moveToBridge() throws OdometerExceptions {
    travelTo(1.5, TN_LLy); // approach the bridge from y direction
    travelTo(2,TN_LLy+0.5);
    turnTo(90); //turn heading to the positive x axis
    travelTo(TN_LLx,TN_LLy+0.5);// approach the bridge from x direction
  }


  public void travelThroughBridge() {
    travelTo(TN_URx+1,TN_LLy + 0.5);
    ClawMovement.releaseCan();
  }

  public void moveToSearchZone() throws OdometerExceptions {
    travelTo(SZ_LLx-1,TN_LLy + 0.5);
    travelTo(SZ_LLx-0.5,TN_LLy);
    travelTo(SZ_LLx-0.5,SZ_LLy);
    turnTo(0); //turn heading to the positive y axis
  }

  public void searchCan() {
    usSensor.fetchSample(usData, 0);
    //TODO tile size search
  }
  
  private void detectCanColor() {
  }
  
  private void detectCanWeight() {
  }
  
  public void moveCan() {
    
  }
}
