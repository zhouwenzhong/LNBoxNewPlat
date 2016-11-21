package com.lianyao.ftfbox.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lianyao.ftf.sdk.fork.NativeRuntime;
import com.lianyao.ftf.sdk.uitl.FileUtils;
import com.lianyao.ftf.sdk.uitl.MtcLog;

/**
 * Created by zhouwz on 16/7/22.
 */
public class AutoStartService extends Service {

    public AutoStartService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        (new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    NativeRuntime.startService(getPackageName() + "/"
                                    + "com.lianyao.ftf.sdk.realize.FtfService",
                            FileUtils.createRootPath(AutoStartService.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MtcLog.e("MainApplication", "unregisterReceiver");
    }

}
