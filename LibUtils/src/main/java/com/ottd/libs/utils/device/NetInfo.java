package com.ottd.libs.utils.device;


public class NetInfo {

	public APN apn = APN.UN_DETECT;

	public String networkOperator = "";

	public int networkType = -1;

	public boolean isWap = false;
	
	public String bssid = ""; // 路由器mac地址

	public String ssid = ""; // wifi名称

}
