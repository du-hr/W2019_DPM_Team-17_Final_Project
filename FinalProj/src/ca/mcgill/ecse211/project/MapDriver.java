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
    navigation.travelTo(x,y);
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
	  /*While turning, turn on US sensor and search for cans*/
	  /*I don't know how you want to do it, maybe call another method, or maybe just add code in here
	   * The goal is to detect cans
	   * Also, after you grab the can, MAKE SURE YOU MOVE BACKWARD
	   */
	  if(SZ_URy - SZ_LLy == 2)
	  {
		  int tile = SZ_URx - SZ_LLx;
		  double x, y;
		  switch(tile)
		  {
		  case 2:
			  
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  break;
			  
		  case 3:
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = SZ_URx * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  navigation.turnLeft2(90);	//DO NOT PERFORM SEARCH HERE
			  navigation.turnLeft2(180);	//Perform search here
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  break;
			  
		  case 4:
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = SZ_URx * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  break;
			  
		  default:
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point  
		  }
	  }
	  else if(SZ_URy - SZ_LLy == 3)
	  {
		  int tile = SZ_URx - SZ_LLx;
		  double x, y;
		  switch(tile)
		  {
		  case 2:
			  
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = SZ_URy * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnLeft2(90);	//DO NOT PERFORM SEARCH HERE
			  
			  navigation.turnLeft2(180);	//Perform search here
			  
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  break;
			  
		  case 3:
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = SZ_URy * TILE_SIZE;
			  navigation.travelTo(x, y);
			  navigation.turnLeft2(90);	//DO NOT PERFORM SEARCH HERE
			  navigation.turnLeft2(180);	//Perform search here
			  
			  x = (SZ_LLx + 2) * TILE_SIZE;
			  y = SZ_URy * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnRight2(180);	//perform search
			  
			  x = (SZ_LLx + 2) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  break;
			  
		  case 4:
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = SZ_URy * TILE_SIZE;
			  navigation.travelTo(x, y);
			  navigation.turnLeft2(90);	//DO NOT PERFORM SEARCH HERE
			  navigation.turnLeft2(180);	//Perform search here
			  
			  x = (SZ_LLx + 3) * TILE_SIZE;
			  y = SZ_URy * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnRight2(180);	//perform search
			  
			  x = (SZ_LLx + 3) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  
			  break;
			  
		  default:
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point  
		  }
	  }
	  else if(SZ_URy - SZ_LLy == 4)
	  {
		  int tile = SZ_URx - SZ_LLx;
		  double x, y;
		  switch(tile)
		  {
		  case 2:
			  
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 2) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnLeft2(90);		//DO NOT PERFORM SEARCH HERE
			  navigation.turnRight2(180);	//perform search here
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 3) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnLeft2(90);		//DO NOT PERFORM SEARCH HERE
			  navigation.turnRight2(180);	//perform search here
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  break;
			  
		  case 3:
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 3) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = (SZ_LLx + 2) * TILE_SIZE;
			  y = (SZ_LLy + 3) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnRight2(360);	//perform search
			  
			  x = (SZ_LLx + 2) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  
			  break;
			  
		  case 4:
			  navigation.turnTo(90);	//you search while turning

			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight(360);
			  
			  x = (SZ_LLx + 1) * TILE_SIZE;
			  y = (SZ_LLy + 3) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  x = (SZ_LLx + 3) * TILE_SIZE;
			  y = (SZ_LLy + 3) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  navigation.turnRight2(360);	//perform search
			  
			  x = (SZ_LLx + 3) * TILE_SIZE;
			  y = (SZ_LLy + 1) * TILE_SIZE;
			  navigation.travelTo(x, y);
			  
			  /*
			   * Perform 360 degree searching. Again, do whatever you like
			   */
			  navigation.turnRight2(360);
			  
			  
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point
			  
			  break;
			  
		  default:
			  navigation.travelTo(SZ_URx*TILE_SIZE, SZ_URy*TILE_SIZE);	//goto finish point  
		  }    
  }
  }
  public void detectCan() {
    
  }
  public void moveCan() {
    
  }

}
