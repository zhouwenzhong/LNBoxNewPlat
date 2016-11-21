package com.lianyao.ftfbox;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.lianyao.ftf.sdk.RtcClient;
import com.lianyao.ftf.sdk.config.RtcBroadcast;
import com.lianyao.ftf.sdk.inter.CalledObserver;
import com.lianyao.ftf.sdk.inter.InitListener;
import com.lianyao.ftf.sdk.uitl.MtcLog;
import com.lianyao.ftf.sdk.uitl.StringUtil;
import com.lianyao.ftfbox.config.Constants;
import com.lianyao.ftfbox.receiver.RtcReceiver;

import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
	private RtcReceiver mReceiver = new RtcReceiver();
	private static List<Activity> activityList = new ArrayList<Activity>();
	public final static String MAIN_ACTIVITY = "com.lianyao.ftfbox.MainActivity";

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("MainApplication is create!");
		//来电广播处理
		IntentFilter intentFilter = new IntentFilter();
		//addAction
		intentFilter.addAction(RtcBroadcast.onIncomingCall);
		registerReceiver(mReceiver, intentFilter);
		Log.e("MainApplication", "registerReceiver");

		RtcClient client = RtcClient.getInstance();
		client.init(this, Constants.APPID, Constants.TICKET,
				new InitListener() {

					@Override
					public void onSuccess() {

					}

					@Override
					public void onError(String message) {

					}
				}, calledObserver);
		client.rtcSetting.setRing(true, R.raw.call_bell);
		client.rtcSetting.setCallRing(true, R.raw.phone_ring);
	}

	@Override
	public void onTerminate(){
		super.onTerminate();
		MtcLog.e("MainApplication", "unregisterReceiver");
		unregisterReceiver(mReceiver);
	}

	public static void setActivity(Activity activity) {
		if(!activityList.contains(activity)) {
			activityList.add(activity);
		}
	}

	public static void removeMainActivity() {
		for(int i = 0; i < activityList.size(); i++) {
			if(activityList.get(i).getClass().getName().equals(MAIN_ACTIVITY)) {
				activityList.clear();
			}
		}
	}

	public static boolean containMainActivity() {
		boolean returnValue = false;
		for(int i = 0; i < activityList.size(); i++) {
			if(activityList.get(i).getClass().getName().equals(MAIN_ACTIVITY)
					&& !activityList.get(i).isFinishing()) {
				returnValue = true;
			}
		}
		return returnValue;
	}



	CalledObserver calledObserver = new CalledObserver() {

		@Override
		public void onIncomingCall(String tid, boolean isAudio,
								   boolean isVideo) {
			Log.i("MainApplication", "onIncomingCall:" + tid);
			if(MainApplication.containMainActivity()) {
				Intent intent = new Intent();
				intent.setAction(RtcBroadcast.onIncomingCall);
				// 来电接收到的还是tid，得改成mobile发送给app
//				if (tid.indexOf("_") > 0) {
//					tid = tid.substring(0, tid.indexOf("_"));
//				}
				intent.putExtra("mobile", StringUtil.getMobileFromUid(tid));
				intent.putExtra("isAudio", isAudio);
				intent.putExtra("isVideo", isVideo);
				MainApplication.this.sendBroadcast(intent);
			}else {
				Intent intent2 = new Intent(MainApplication.this, MainActivity.class);
				intent2.putExtra("isback", true);
				// 来电接收到的还是tid，得改成mobile发送给app
//				if (tid.indexOf("_") > 0) {
//					tid = tid.substring(0, tid.indexOf("_"));
//				}
				intent2.putExtra("nickname", StringUtil.getMobileFromUid(tid));
				intent2.putExtra("mobile", StringUtil.getMobileFromUid(tid));
				intent2.putExtra("isVideo", isVideo);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainApplication.this.startActivity(intent2);
			}
		}

		@Override
		public void onHangUp(String tid) {
			Log.i("MainApplication", "onHangUp:" + tid);
			Intent intent = new Intent();
			intent.putExtra("tid", tid);
			intent.setAction(RtcBroadcast.onHangUp);
			MainApplication.this.sendBroadcast(intent);
		}
	};
}
