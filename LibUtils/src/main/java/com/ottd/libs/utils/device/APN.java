package com.ottd.libs.utils.device;

public enum APN {

	UN_DETECT(0), WIFI(1), CMWAP(2), CMNET(3), UNIWAP(4), UNINET(5), WAP3G(6),
	NET3G(7), CTWAP(8), CTNET(9), UNKNOWN(10), UNKNOW_WAP(11), NO_NETWORK(12), WAP4G(13), NET4G(14);


	private int value = 0;
	private String mValueStr = "";
	private APN(int val) {
		this.value = val;
		switch (val){
			case 1:
				this.mValueStr = "wifi";
				break;
			case 2:
				this.mValueStr = "cmwap";
				break;
			case 3:
				this.mValueStr = "cmnet";
				break;
			case 4:
				this.mValueStr = "uniwap";
				break;
			case 5:
				this.mValueStr = "uninet";
				break;
			case 6:
				this.mValueStr = "3gwap";
				break;
			case 7:
				this.mValueStr = "3gnet";
				break;
			case 8:
				this.mValueStr = "ctwap";
				break;
			case 9:
				this.mValueStr = "ctnet";
				break;
			case 10:
				this.mValueStr = "unknow";
				break;
			case 11:
				this.mValueStr = "wap";
				break;
			case 12:
				this.mValueStr = "net";
				break;
			case 13:
				this.mValueStr = "4gwap";
				break;
			case 14:
				this.mValueStr = "4gnet";
				break;
			default:
				this.mValueStr = "unknow";
		}
	}

	public byte getByteValue(){
		return (byte)this.value;
	}

	public String getStringValue(){
		return this.mValueStr;
	}

}
