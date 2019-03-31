package FinalProject;


import static FinalProject.Navigation.*;
import Odometer.OdometryCorrection;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import Odometer.OdometerExceptions;
import static FinalProject.Main.*;


public class MapDriver{
  private static final Port usPort = LocalEV3.get().getPort("S1");
  //default constructor
  public MapDriver() {
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

  // assume positive y axis is 0 degree axis
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
    // Setup ultrasonic sensor
    // 1. Create a port object attached to a physical port (done above)
    // 2. Create a sensor instance and attach to port
    // 3. Create a sample provider instance for the above and initialize operating
    // mode
    // 4. Create a buffer for the sensor data
    @SuppressWarnings("resource") // Because we don't bother to close this resource
    SensorModes usSensor = new EV3UltrasonicSensor(usPort);
    SampleProvider usValue = usSensor.getMode("Distance"); // usValue provides samples from this instance
    float[] usData = new float[usValue.sampleSize()]; // usData is the buffer in which data are returned
    //25
    
    
    
    

  }
  
  private void detectCanColor() {
  }
  
  private void detectCanWeight() {
  }
  
  public void moveCan() {
    
  }
}
