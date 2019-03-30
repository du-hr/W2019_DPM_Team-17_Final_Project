package FinalProject;


import static FinalProject.Navigation.*;
import Odometer.OdometryCorrection;
import Odometer.OdometerExceptions;
import static FinalProject.Main.*;
import static FinalProject.ClawMovement.*;


public class MapDriver extends Thread{
  private OdometryCorrection odometryCorrection;
  Thread odoCorrectionThread = new Thread(odometryCorrection);
  
  
  //default constructor
  public MapDriver() {
  }

  public void startOdometerCorrection() throws OdometerExceptions{
    //odoCorrectionThread.stop(); 
  }
  
  public void stopOdometerCorrection() throws OdometerExceptions{
   //odoCorrectionThread.start();  
  }
  public void run() {
    try {
      moveToTunnel();
    } catch (OdometerExceptions e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    travelThroughBridge();
    moveToSearchZone();
    searchCan();
    detectCanColor();
    detectCanWeight();
    moveCan();
  }


  // assume positive y axis is 0 degree axis
  public void moveToTunnel() throws OdometerExceptions {
    stopOdometerCorrection();
    holdCan();
    travelTo(1.5, 1); // travel to start point after doing localization
    turnTo(0); //turn heading to the positive y axis
    startOdometerCorrection();
    travelTo(1.5, TN_LLy); // approach the bridge from y direction
    stopOdometerCorrection();
    travelTo(2,TN_LLy+0.5);
    turnTo(90); //turn heading to the positive x axis
    startOdometerCorrection();
    travelTo(TN_LLx,TN_LLy+0.5);// approach the bridge from x direction
    stopOdometerCorrection();
  }


  public void travelThroughBridge() {
  }

  public void moveToSearchZone() {
  }

  public void searchCan() {

  }
  
  private void detectCanWeight() {


  }

  private void detectCanColor() {
    
  }
  public void moveCan() {
    
  }
}
