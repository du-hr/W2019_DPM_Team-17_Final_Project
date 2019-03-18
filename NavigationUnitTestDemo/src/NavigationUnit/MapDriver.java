package NavigationUnit;

import static NavigationUnit.Main.*;
import static NavigationUnit.Navigation.*;
public class MapDriver extends Thread {

  public MapDriver() {
    
  }
  
  @Override
  public void run() {
    moveToTunnel();
    moveToIsland();
    moveToSearchZone();
    searchCan();
    detectCan();
    moveCan();
  }
  
  public void moveToTunnel() {
    travelTo(1.5 * TILE_SIZE, 1.5 * TILE_SIZE);// no odometry correction here
    
    double x = 1.5 * TILE_SIZE;
    double y = (TNR_LL[2] + 0.5) * TILE_SIZE;
    
    travelTo(x,y);
    
  }
  
  
  public void moveToIsland() {
    ClawMovement.holdCan(); // to be fit to cross the bridge
    double x = (TNR_UR[0] + 0.5) * TILE_SIZE;
    double y = (TNR_LL[2] + 0.5) * TILE_SIZE;
    travelTo(x,y);
    ClawMovement.releaseCan();
    
    
  }
  public void moveToSearchZone() {
    
    double x = (TNR_UR[0] + 0.5) * TILE_SIZE;
    double y = (SZR_LL[1] + 0.5) * TILE_SIZE;
    travelTo(x,y);
    y = (SZR_LL[1] + 0.5) * TILE_SIZE;;
    x = (SZR_LL[0] - 0.5) * TILE_SIZE;
    travelTo(x,y);
    x = (SZR_LL[0]) * TILE_SIZE;
    y = (SZR_LL[1]) * TILE_SIZE;
    travelTo(x,y);
    turnTo(-135);
    
  }
  public void searchCan() {
    
  }
  public void detectCan() {
    
  }
  public void moveCan() {
    
  }

}
