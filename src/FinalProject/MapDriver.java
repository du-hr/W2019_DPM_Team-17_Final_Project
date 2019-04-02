package FinalProject;


import static FinalProject.Navigation.*;
import Odometer.OdometryCorrection;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import Odometer.OdometerExceptions;
import static FinalProject.Main.*;


public class MapDriver{
  private EV3LargeRegulatedMotor leftMotor;
  private EV3LargeRegulatedMotor rightMotor;
  //default constructor
  public MapDriver(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
  }
  
  public void drive() throws OdometerExceptions{
    // repeat for 5 times
    for (int i = 0; i<5; i++) {
    moveToStartingPosition();
    OdometryCorrection.isCorrecting = true;
    moveToBridge();
    OdometryCorrection.isCorrecting = false;
    travelThroughBridge();
    OdometryCorrection.isCorrecting = true;
    moveToSearchZone();
    OdometryCorrection.isCorrecting = false;
    CanScanner.isScanning = true;
    searchCan();
    detectCanColor();
    detectCanWeight();
    moveCan();
    }
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
  }
  
  public void searchCan() {
    //TODO tile size search
    travelTo(SZ_LLx,SZ_LLy);
    turnTo(0); // Position the heading of the robot to positive y axis
    int SZ_length = SZ_URy - SZ_LLy;
    int SZ_width = SZ_URx - SZ_LLx;
    int i = 0;
    while (i< SZ_length) {
      turnTo(90);
      turnTo(0);
      i++;
      travelTo(SZ_LLx,SZ_LLy+i);
    }
    
  }
  
  private void detectCanColor() {
    detectCanColor();
  }
  
  private void detectCanWeight() {
    detectCanWeight();
  }
  
  public void moveCan() {
    
  }
}
