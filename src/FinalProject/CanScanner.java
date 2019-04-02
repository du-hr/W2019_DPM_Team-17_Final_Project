package FinalProject;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;

public class CanScanner implements Runnable {
  private EV3LargeRegulatedMotor leftMotor;
  private EV3LargeRegulatedMotor rightMotor;
  private SampleProvider usSensor;
  private float[] usData;
  private static final double TIGGER_DISTANCE = 30.48;
  public static boolean isScanning = false;
  
  // constructor
  public CanScanner(SampleProvider usSensor, float[] usData, 
      EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
    this.usSensor = usSensor;
    this.usData = usData;
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
  }

  @Override
  public void run() {
    usSensor.fetchSample(usData, 0);
    while (isScanning = true) {
      if (usData[0] <= TIGGER_DISTANCE) {
        leftMotor.stop();
        rightMotor.stop();
        Sound.beepSequenceUp();
        isScanning = false;
      }
    }
    
  }

}
