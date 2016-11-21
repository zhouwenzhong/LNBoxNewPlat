package com.lianyao.ftfbox.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * 跟App相关的辅助类
 */
public class AppUtil {

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getDeviceIMEI(Context context) {
	    String deviceId;
	    if (isPhone(context)) {
	        TelephonyManager telephony = (TelephonyManager) context
	                .getSystemService(Context.TELEPHONY_SERVICE);
	        deviceId = telephony.getDeviceId();
	    } else {
	        deviceId = Settings.Secure.getString(context.getContentResolver(),
	                Settings.Secure.ANDROID_ID);
	  
	    }
	    return deviceId;
	}
	
	public static boolean isPhone(Context context) {
	    TelephonyManager telephony = (TelephonyManager) context
	            .getSystemService(Context.TELEPHONY_SERVICE);
	    if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
	        return false;
	    } else {
	        return true;
	    }
	}


}