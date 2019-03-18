package NavigationUnit;

import static NavigationUnit.Main.*;
import static NavigationUnit.Navigation.*;
public class MapDriver extends Thread {

  public MapDriver() {
    
  }
  
  @Override
  public void run() {
      travelTo(0,2);
      travelTo(1,1);
      travelTo(2,2);
      travelTo(2,1);
      travelTo(1,0);
      
  }
  
  public void moveToTunnel() {
  }
  
  public void moveToIsland() {
    ClawMovement.holdCan(); // to be fit to cross the bridge
    
  }
  public void moveToSearchZone() {
    
  }
  public void search

}
