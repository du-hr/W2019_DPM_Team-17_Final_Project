package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Project.*;

import ca.mcgill.ecse211.odometer.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

import static ca.mcgill.ecse211.project.Navigation.*;
public class MapDriver {
	Navigation navigation;

  public MapDriver(Odometer odo, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
	  navigation = new Navigation(odo, leftMotor, rightMotor);    
  }
  
  public void drive() {
    moveToTunnel();
    moveToIsland();
    moveToSearchZone();
    searchCan();
    detectCan();
    moveCan();
  }
  
  public void moveToTunnel() {
    navigation.travelTo(1.5 * TILE_SIZE, 1.5 * TILE_SIZE);// no odometry correction here
    
    double x = 1.5 * TILE_SIZE;
    double y = (TN_LLy + 0.5) * TILE_SIZE;
    
    navigation.travelTo(x,y);
    
  }
  
  
  public void moveToIsland() {
    //ClawMovement.holdCan(); // to be fit to cross the bridge
    double x = (TN_URx + 0.5) * TILE_SIZE;
    double y = (TN_LLy + 0.5) * TILE_SIZE;
    travelTo(x,y);
    //ClawMovement.releaseCan();
    
    
  }
  public void moveToSearchZone() {
    
    double x = (TN_URx + 0.5) * TILE_SIZE;
    double y = (SZ_LLy + 0.5) * TILE_SIZE;
    navigation.travelTo(x,y);
    y = (SZ_LLy + 0.5) * TILE_SIZE;;
    x = (SZ_LLx - 0.5) * TILE_SIZE;
    navigation.travelTo(x,y);
    x = (SZ_LLx) * TILE_SIZE;
    y = (SZ_LLy) * TILE_SIZE;
    navigation.travelTo(x,y);
    turnTo(-135);
    
  }
  public void searchCan() {
    
  }
  public void detectCan() {
    
  }
  public void moveCan() {
    
  }

}
