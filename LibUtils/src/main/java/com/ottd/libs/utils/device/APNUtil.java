package com.ottd.libs.utils.device;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.ottd.libs.utils.CommonUtils;
import com.ottd.libs.utils.TextUtils;


/**
 * 网络操作相关的工具类
 * 
 * @author gengeng
 */
public class APNUtil {
	
	public static final byte GROUP_NETTYPE_2g = 1;
	public static final byte GROUP_NETTYPE_3g = 2;
	public static final byte GROUP_NETTYPE_WIFI = 3;
	public static final byte GROUP_NETTYPE_UNKNOWN = 4;
	public static final byte GROUP_NETTYPE_4g = 5;

	/* 中国移动 */
	public static final int OPERATOR_CHINA_MOBILE = 0;
	/* 中国联通 */
	public static final int OPERATOR_CHINA_UNICOM = 1;
	/* 中国电信 */
	public static final int OPERATOR_CHINA_TELECOM = 2;
	/* 中国铁通 */
	public static final int OPERATOR_CHINA_TIETONG = 3;
	/* 未知运营商 */
	public static final int OPERATOR_UNKNOWN = -1;

	public static boolean isNetworkActive = true;

	// 接入点名称
	public static NetInfo netInfo = new NetInfo();


	public static boolean isNetworkActive(Context mContext) {
		if (netInfo.apn == APN.UN_DETECT) {
			refreshNetwork(mContext);
		}
		return isNetworkActive;
	}

	/* 判断是否是wap类网络 */
	public static boolean isWap() {
		String host = Proxy.getDefaultHost();


		return !TextUtils.ckIsEmpty(host);
	}
	
	/**
	 * 获取网络类型是wifi，3g，还是2g
	 * @return
	 */
	public static int getGroupNetType(Context mContext) {
		if (isWifi(mContext)) {
			return GROUP_NETTYPE_WIFI;
		} else if (is4G(mContext)) {
			return GROUP_NETTYPE_4g;
		}  else if (is3G(mContext)) {
			return GROUP_NETTYPE_3g;
		} else if (is2G(mContext)) {
			return GROUP_NETTYPE_2g;
		} else {
			return GROUP_NETTYPE_UNKNOWN;
		}
	}

	/**
	 * 获取网络类型描述符
	 */
	public static String getGroupNetTypeDesc(Context mContext) {
		if (isWifi(mContext)) {
			return "WIFI";
		} else if (is4G(mContext)) {
			return "4G";
		}  else if (is3G(mContext)) {
			return "3G";
		} else if (is2G(mContext)) {
			return "2G";
		} else {
			return "UNKNOWN";
		}
	}

	/* 判断是否是Wifi类网络 */
	public static boolean isWifi(Context mContext) {
		final APN apn = getApn(mContext);
		return apn == APN.WIFI;
	}

	public static boolean is2G(Context mContext) {
		final APN apn = getApn(mContext);
		return apn == APN.CMNET || apn == APN.CMWAP || apn == APN.UNINET || apn == APN.UNIWAP;
	}

	public static boolean is3G(Context mContext) {
		final APN apn = getApn(mContext);
		return apn == APN.CTWAP || apn == APN.CTNET || apn == APN.WAP3G || apn == APN.NET3G;
	}
	
	public static boolean is4G(Context mContext) {
		final APN apn = getApn(mContext);
		return apn == APN.WAP4G || apn == APN.NET4G;
	}

	public static APN getApn(Context mContext) {
		final NetInfo netInfo = getNetInfo(mContext);
		return netInfo.apn;
	}

	public static byte getApnType(Context mContext) {
		final NetInfo netInfo = getNetInfo(mContext);
		return netInfo.apn.getByteValue();
	}

	public static String getApnName(Context mContext) {
		final NetInfo netInfo = getNetInfo(mContext);
		return netInfo.apn.getStringValue();
	}

	/**
	 * 网络连接变化的时候需要重新刷新一下当前的apn
	 */
	public static void refreshNetwork(Context mContext) {
		netInfo = getNetInfo(mContext);
	}

	public static NetInfo getNetInfo(Context context) {
		NetInfo result = new NetInfo();
		NetworkInfo info = null;
		ConnectivityManager connManager = null;
		try{
			connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// OPPO的机器上这个地方会抛异常crash+
			if( connManager != null )
				info = connManager.getActiveNetworkInfo();
			if ( info == null || !info.isAvailable()) {
				isNetworkActive = false;
				result.apn = APN.NO_NETWORK;
				return result;
			}
		}catch(Throwable e){
			//增加error捕获，部分酷派机器上可能没有getActiveNetworkInfo方法
		}

		isNetworkActive = true;
		if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {// wifi网络判定
			result.apn = APN.WIFI;

			try{
				// 为NetInfo添加路由器mac地址与wifi名称 20140319 by gengeng
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				if (wifiManager != null)
				{
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					if (wifiInfo != null) {
						result.bssid = wifiInfo.getBSSID();
						result.ssid = wifiInfo.getSSID();
					}
				}

			} catch(Throwable e) {
				e.printStackTrace();
			}
			return result;
		} else {
			return getMobileNetInfo(context);
		}
	}

	/* 获取移动网络接入点类型 , context不能为null */
	public static NetInfo getMobileNetInfo(Context context) {
		NetInfo result = new NetInfo();
		final boolean isWap = isWap();
		result.isWap = isWap;										// iswap赋值
		
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String networkOperator = telManager.getNetworkOperator();
		result.networkOperator = networkOperator;					// networkOperator赋值
		final int networkType = telManager.getNetworkType();
		result.networkType = networkType;							// networkType赋值
		
		final int operator = getSimOperator(networkOperator);
		switch (operator) {
		case OPERATOR_CHINA_MOBILE: // 中国移动
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_LTE:
				if (isWap) {
					result.apn = APN.WAP4G;
				} else {
					result.apn = APN.NET4G;
				}
				return result;
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
				if (isWap) {
					result.apn = APN.CMWAP;
				} else {
					result.apn = APN.CMNET;
				}
				return result;
			default:
				if (isWap) {
					result.apn = APN.UNKNOW_WAP;
				} else {
					result.apn = APN.UNKNOWN;
				}
				return result;
			}
		case OPERATOR_CHINA_UNICOM: // 中国联通
			// 先判断是2g还是3g网络
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_LTE:
				if (isWap) {
					result.apn = APN.WAP4G;
				} else {
					result.apn = APN.NET4G;
				}
				return result;
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_HSDPA: 	// 联通3g
			case 15:   							  		// TelephonyManager.NETWORK_TYPE_HSPAP api 13+
				if (isWap) {
					result.apn = APN.WAP3G;
				} else {
					result.apn = APN.NET3G;
				}
				return result;
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE: // 联通2g
				if (isWap) {
					result.apn = APN.UNIWAP;
				} else {
					result.apn = APN.UNINET;
				}
				return result;
			default:
				if (isWap) {
					result.apn = APN.UNKNOW_WAP;
				} else {
					result.apn = APN.UNKNOWN;
				}
				return result;
			}
		case OPERATOR_CHINA_TELECOM: // 中国电信
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_LTE:
				if (isWap) {
					result.apn = APN.WAP4G;
				} else {
					result.apn = APN.NET4G;
				}
				return result;
			default:
				if (isWap) {
					result.apn = APN.CTWAP;
				} else {
					result.apn = APN.CTNET;
				}
				return result;
			}
		default:
			if (isWap) {
				result.apn = APN.UNKNOW_WAP;
			} else {
				result.apn = APN.UNKNOWN;
			}
			return result;
		}
	}

	public static final String SP_PROVIDER_NAME_CHINA_MOBILE = "中国移动";
	public static final String SP_PROVIDER_NAME_CHINA_UNICOM = "中国联通";
	public static final String SP_PROVIDER_NAME_CHINA_TELECOM = "中国电信";
	public static final String SP_PROVIDER_NAME_CHINA_TIETONG = "中国网通";


	public static final int IMSI_CODE_CHINA_MOBILE_GSM = 46000;
	public static final int IMSI_CODE_CHINA_UNICOM_GSM = 46001;
	public static final int IMSI_CODE_CHINA_MOBILE_SCDMA = 46002;
	public static final int IMSI_CODE_CHINA_TELCOM_CDMA = 46003;
	public static final int IMSI_CODE_CHINA_TELCOM = 46005;
	public static final int IMSI_CODE_CHINA_UNICOM = 46006;
	public static final int IMSI_CODE_CHINA_MOBILE = 46007;
	public static final int IMSI_CODE_CHINA_TELCOM_SCDMA = 46011;
	public static final int IMSI_CODE_CHINA_TIETONG_SDM_R= 46020;

	public static int getSimOperator(String networkOperator){
		int ismi = CommonUtils.parseInt(networkOperator,0);
		switch (ismi){
			case IMSI_CODE_CHINA_MOBILE_GSM:
			case IMSI_CODE_CHINA_MOBILE_SCDMA:
			case IMSI_CODE_CHINA_MOBILE:
				return OPERATOR_CHINA_MOBILE;
			case IMSI_CODE_CHINA_UNICOM_GSM:
			case IMSI_CODE_CHINA_UNICOM:
				return OPERATOR_CHINA_UNICOM;
			case IMSI_CODE_CHINA_TELCOM_CDMA:
			case IMSI_CODE_CHINA_TELCOM:
			case IMSI_CODE_CHINA_TELCOM_SCDMA:
				return OPERATOR_CHINA_TELECOM;
			case IMSI_CODE_CHINA_TIETONG_SDM_R:
				return OPERATOR_CHINA_TIETONG;
			default:
				return OPERATOR_UNKNOWN;
		}
	}

	public static String getSimOperator(Context ctx){
		TelephonyManager telManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		final String networkOperator = telManager.getNetworkOperator();
		int simOperator = getSimOperator(networkOperator);
		switch (simOperator){
			case OPERATOR_CHINA_MOBILE:
				return SP_PROVIDER_NAME_CHINA_MOBILE;
			case OPERATOR_CHINA_TELECOM:
				return SP_PROVIDER_NAME_CHINA_TELECOM;
			case OPERATOR_CHINA_UNICOM:
				return SP_PROVIDER_NAME_CHINA_UNICOM;
			case OPERATOR_CHINA_TIETONG:
				return SP_PROVIDER_NAME_CHINA_TIETONG;
			default:
				return "";
		}
	}

	public static String getNetWorkName(Context context){
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm == null) {
			return "MOBILE";
		}
		final NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null) {
			return info.getTypeName();
		} else {
			return "MOBILE";
		}
	}
}
