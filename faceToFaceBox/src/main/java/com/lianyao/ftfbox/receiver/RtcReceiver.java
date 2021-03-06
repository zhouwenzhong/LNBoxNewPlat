package com.lianyao.ftfbox.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lianyao.ftf.sdk.config.RtcBroadcast;
import com.lianyao.ftf.sdk.uitl.MtcLog;
import com.lianyao.ftfbox.MainActivity;

public class RtcReceiver extends BroadcastReceiver {

	public RtcReceiver() {
		MtcLog.e("RtcReceiver is created!");
	}

	@Override
	public void onReceive(Context arg0, Intent intent) {
		if (intent.getAction().equals(RtcBroadcast.onIncomingCall)) {
			MtcLog.e("RtcReceiver", "onIncomingCall");
			String mobile = intent.getStringExtra("mobile");
			boolean isVideo = intent.getBooleanExtra("isVideo", false);
			Intent intent2 = new Intent(arg0, MainActivity.class);
			intent2.putExtra("isback", true);
			intent2.putExtra("nickname", mobile);
			intent2.putExtra("mobile", mobile);
			intent2.putExtra("isVideo", isVideo);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			arg0.startActivity(intent2);
		}
	}
}
