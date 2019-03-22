package ca.mcgill.ecse211.project;

import java.util.Map;

import ca.mcgill.ecse211.WiFiClient.WifiConnection;

class Wifi {
	
	private static final String SERVER_IP = "192.168.2.8";
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
	    	  Project.corner = ((Long) data.get("RedCorner")).intValue();
	    	  Project.TN_LLx =((Long) data.get("TNR_LL_x")).intValue();
	    	  Project.TN_LLy =((Long) data.get("TNR_LL_y")).intValue();
	    	  Project.TN_URx =((Long) data.get("TNR_UR_x")).intValue();
	    	  Project.TN_URy =((Long) data.get("TNR_UR_y")).intValue();
	    	  Project.LLx =((Long) data.get("Red_LL_x")).intValue();
	    	  Project.LLy =((Long) data.get("Red_LL_y")).intValue();
	    	  Project.URx =((Long) data.get("Red_UR_x")).intValue();
	    	  Project.URy =((Long) data.get("Red_UR_y")).intValue();
	    	  Project.SZ_LLx =((Long) data.get("SZR_LL_x")).intValue();
	    	  Project.SZ_LLy =((Long) data.get("SZR_LL_y")).intValue();
	    	  Project.SZ_URx =((Long) data.get("SZR_UR_x")).intValue();
	    	  Project.SZ_URy =((Long) data.get("SZR_UR_y")).intValue();
	      }
	      else if(TEAM_NUMBER == greenTeam) {
	    	  Project.corner = ((Long) data.get("GreenCorner")).intValue();
	    	  Project.TN_LLx =((Long) data.get("TNG_LL_x")).intValue();
	    	  Project.TN_LLy =((Long) data.get("TNG_LL_y")).intValue();
	    	  Project.TN_URx =((Long) data.get("TNG_UR_x")).intValue();
	    	  Project.TN_URy =((Long) data.get("TNG_UR_y")).intValue();
	    	  Project.LLx =((Long) data.get("Green_LL_x")).intValue();
	    	  Project.LLy =((Long) data.get("Green_LL_y")).intValue();
	    	  Project.URx =((Long) data.get("Green_UR_x")).intValue();
	    	  Project.URy =((Long) data.get("Green_UR_y")).intValue();
	    	  Project.SZ_LLx =((Long) data.get("SZG_LL_x")).intValue();
	    	  Project.SZ_LLy =((Long) data.get("SZG_LL_y")).intValue();
	    	  Project.SZ_URx =((Long) data.get("SZG_UR_x")).intValue();
	    	  Project.SZ_URy =((Long) data.get("SZG_UR_y")).intValue();
	      }
	    }catch (Exception e) {
	        System.err.println("Error: " + e.getMessage());
	    }
	}

}
