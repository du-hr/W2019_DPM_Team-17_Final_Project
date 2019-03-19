package NavigationUnit;

import java.util.Map;
import static NavigationUnit.Main.*;
import ca.mcgill.ecse211.WiFiClient.WifiConnection;

class Wifi {
	
	private static final String SERVER_IP = "192.168.2.20";
	private static final int TEAM_NUMBER = 17;
	private static final boolean ENABLE_DEBUG_WIFI_PRINT = true;

	@SuppressWarnings("rawtypes")

	public Wifi() {
		this.getData();
	}
	
	public void getData() {
		WifiConnection conn = new WifiConnection(SERVER_IP, TEAM_NUMBER, ENABLE_DEBUG_WIFI_PRINT);
	    try {
	      Map data = conn.getData();
	      int redTeam = ((Long) data.get("RedTeam")).intValue();
	      int greenTeam = ((Long)data.get("GreenTeam")).intValue();
	      if(TEAM_NUMBER == redTeam) {
	    	  Corner = ((Long) data.get("RedCorner")).intValue();
	    	  TN_LL[0] =((Long) data.get("TNR_LL_x")).intValue();
	    	  TN_LL[1] =((Long) data.get("TNR_LL_y")).intValue();
	    	  TN_UR[0] =((Long) data.get("TNR_UR_x")).intValue();
	    	  TN_UR[1] =((Long) data.get("TNR_UR_y")).intValue();
	    	  LL[0] =((Long) data.get("Red_LL_x")).intValue();
	    	  LL[1] =((Long) data.get("Red_LL_y")).intValue();
	    	  UR[0] =((Long) data.get("Red_UR_x")).intValue();
	    	  UR[1] =((Long) data.get("Red_UR_y")).intValue();
	    	  SZ_LL[0] =((Long) data.get("SZR_LL_x")).intValue();
	    	  SZ_LL[1] =((Long) data.get("SZR_LL_y")).intValue();
	    	  SZ_UR[0] =((Long) data.get("SZR_UR_x")).intValue();
	    	  SZ_UR[1] =((Long) data.get("SZR_UR_y")).intValue();
	      }
	      else if(TEAM_NUMBER == greenTeam) {
	    	  Corner = ((Long) data.get("GreenCorner")).intValue();
	    	  TN_LL[0] =((Long) data.get("TNG_LL_x")).intValue();
	    	  TN_LL[1] =((Long) data.get("TNG_LL_y")).intValue();
	    	  TN_UR[0] =((Long) data.get("TNG_UR_x")).intValue();
	    	  TN_UR[1] =((Long) data.get("TNG_UR_y")).intValue();
	    	  LL[0] =((Long) data.get("Green_LL_x")).intValue();
	    	  LL[1] =((Long) data.get("Green_LL_y")).intValue();
	    	  UR[0] =((Long) data.get("Green_UR_x")).intValue();
	    	  UR[1] =((Long) data.get("Green_UR_y")).intValue();
	    	  SZ_LL[0] =((Long) data.get("SZG_LL_x")).intValue();
	    	  SZ_LL[1] =((Long) data.get("SZG_LL_y")).intValue();
	    	  SZ_UR[0] =((Long) data.get("SZG_UR_x")).intValue();
	    	  SZ_UR[1] =((Long) data.get("SZG_UR_y")).intValue();
	      }
	    }catch (Exception e) {
	        System.err.println("Error: " + e.getMessage());
	    }
	}

}