package FinalProject;


import static FinalProject.Navigation.*;
import java.util.concurrent.TimeUnit;
import Odometer.OdometryCorrection;
import Odometer.Odometer;
import Odometer.OdometerExceptions;
import static FinalProject.Main.*;
import static FinalProject.CanScanner.*;


public class MapDriver{
  private static int counter;
  private static int counter_1;
  private static int counter_2;
  private static int counter_3;
  private static int counter_4;
  private Odometer odometer;
  //default constructor
  public MapDriver(Odometer odometer) {
    this.odometer = odometer;
  }
  
  public void drive() throws OdometerExceptions, InterruptedException{
    // repeat for 5 times
    for (int i=0; i<5; i++) {
    moveToStartingPosition();
    OdometryCorrection.isCorrecting = true;
    moveToBridge();
    OdometryCorrection.isCorrecting = false;
    travelThroughBridge();
    OdometryCorrection.isCorrecting = true;
    moveToSearchZone();
    OdometryCorrection.isCorrecting = false;
    searchCan();
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
    OdometryCorrection.isCorrecting = false;
    travelTo(SZ_LLx,SZ_LLy);
    turnTo(0);
  }
  
  public void searchCan() throws InterruptedException, OdometerExceptions {
    int SZ_length = SZ_URy - SZ_LLy;
    int SZ_width = SZ_URx - SZ_LLx;
    
    // search on line 1
    if(counter < SZ_length) {
      // start from the previous scanning position
      travelTo(SZ_LLx,SZ_LLy+counter_1);
      // Position the heading of the robot to positive y axis
      turnTo(0);
      // start the CanScanner thread
      isScanning = true;
      // let the program sleep for 3 seconds to leave 
      // enough time for scanning
      TimeUnit.SECONDS.sleep(3);
      // detected a can while scanning
      if (isScanning == false) {
        moveToCan(CanScanner.detectedCanHeading,CanScanner.detectedCanDistance);
        doColorDetection();
        doWeightDetection();
        moveCanBack();
        return;
      }
      // did not find a can during the scan
      else {
        // manually stop the CanScanner thread after 3 seconds
        isScanning = false;
        // reset the heading to prepare for the next scanning in the next tile
        turnTo(0);
        counter_1++;
        travelTo(SZ_LLx,SZ_LLy+counter_1);
        counter++;
        if (counter_1 < SZ_length) {
          searchCan(); // recursive method
        }
      }
    }
    
    // search on line 2
    else if (counter >= SZ_length && counter < SZ_length + SZ_width) {
      travelTo(SZ_LLx,SZ_URy);
      // start from the previous scanning position
      travelTo(SZ_LLx+counter_2,SZ_URy);
      // Position the heading of the robot to positive x axis
      turnTo(90);
      // start the CanScanner thread
      isScanning = true;
      // let the program sleep for 3 seconds to leave 
      // enough time for scanning
      TimeUnit.SECONDS.sleep(3);
      // detected a can while scanning
      if (isScanning == false) {
        moveToCan(CanScanner.detectedCanHeading,CanScanner.detectedCanDistance);
        doColorDetection();
        doWeightDetection();
        moveCanBack();
        return;
      }
      // did not find a can during the scan
      else {
        // manually stop the CanScanner thread after 3 seconds
        isScanning = false;
        // reset the heading to prepare for the next scanning in the next tile
        turnTo(90);
        counter_2++;
        travelTo(SZ_LLx+counter_2,SZ_URy);
        counter++;
        if (counter_2 < SZ_width) {
          searchCan(); // recursive method
        }
      }
    }
    
    //search on line 3
    else if (counter >= SZ_length + SZ_width && counter < 2*SZ_length + SZ_width) {
      travelTo(SZ_LLx,SZ_URy);
      travelTo(SZ_URx,SZ_URy);
      // start from the previous scanning position
      travelTo(SZ_URx,SZ_URy-counter_3);
      // Position the heading of the robot to negative y axis
      turnTo(180);
      // start the CanScanner thread
      isScanning = true;
      // let the program sleep for 3 seconds to leave 
      // enough time for scanning
      TimeUnit.SECONDS.sleep(3);
      // detected a can while scanning
      if (isScanning == false) {
        moveToCan(CanScanner.detectedCanHeading,CanScanner.detectedCanDistance);
        doColorDetection();
        doWeightDetection();
        moveCanBack();
        return;
      }
      // did not find a can during the scan
      else {
        // manually stop the CanScanner thread after 3 seconds
        isScanning = false;
        // reset the heading to prepare for the next scanning in the next tile
        turnTo(180);
        counter_3++;
        travelTo(SZ_URx,SZ_URy-counter_3);
        counter++;
        if (counter_3 < SZ_length) {
          searchCan(); // recursive method
        }
      }
    }
    
    // search on line 4
    else if (counter >= 2*SZ_length + SZ_width && counter <  2*SZ_length + 2*SZ_width) {
      travelTo(SZ_URx,SZ_LLy);
      // start from the previous scanning position
      travelTo(SZ_URx-counter_4,SZ_LLy);
      // Position the heading of the robot to negative x axis
      turnTo(-90);
      // start the CanScanner thread
      isScanning = true;
      // let the program sleep for 3 seconds to leave 
      // enough time for scanning
      TimeUnit.SECONDS.sleep(3);
      // detected a can while scanning
      if (isScanning == false) {
        moveToCan(CanScanner.detectedCanHeading,CanScanner.detectedCanDistance);
        doColorDetection();
        doWeightDetection();
        moveCanBack();
        return;
      }
      // did not find a can during the scan
      else {
        // manually stop the CanScanner thread after 3 seconds
        isScanning = false;
        // reset the heading to prepare for the next scanning in the next tile
        turnTo(-90);
        counter_4++;
        travelTo(SZ_URx-counter_4,SZ_LLy);
        counter++;
        if (counter_4 < SZ_width) {
          searchCan(); // recursive method
        }
      }
    }
  }
  

  // TODO NOT COMPLETED
  private void moveToCan(double detectedCanHeading, double detectedCanDistance) {
    
    double[] odoData = odometer.getXYT();
    double currentX = odoData[0];
    double currentY = odoData[1];
    double canX = currentX + detectedCanDistance * Math.sin(detectedCanHeading);
    double canY = currentY + detectedCanDistance * Math.cos(detectedCanHeading);
    travelTo(canX,canY);
    travelTo(currentX,currentY); // move back
  }

  private void doColorDetection() {
    CanColorDetection.detectCanColor();
  }
  
  private void doWeightDetection() {
    CanWeightDetection.detectCanWeight();
  }
  
  public void moveCanBack() {
  }
}
