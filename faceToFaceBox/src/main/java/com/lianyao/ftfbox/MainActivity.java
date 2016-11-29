package com.lianyao.ftfbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lianyao.ftf.sdk.RtcClient;
import com.lianyao.ftf.sdk.config.CallError;
import com.lianyao.ftf.sdk.config.CallState;
import com.lianyao.ftf.sdk.config.RtcBroadcast;
import com.lianyao.ftf.sdk.inter.CallStateListener;
import com.lianyao.ftf.sdk.inter.LoginCallback;
import com.lianyao.ftf.sdk.inter.RegisterCallBack;
import com.lianyao.ftf.sdk.layered.RtcCallManager;
import com.lianyao.ftf.sdk.uitl.MtcLog;
import com.lianyao.ftf.sdk.view.PercentFrameLayout;
import com.lianyao.ftfbox.adapter.MovieAdapter;
import com.lianyao.ftfbox.config.Constants;
import com.lianyao.ftfbox.config.UpdateInfo;
import com.lianyao.ftfbox.domain.Contact;
import com.lianyao.ftfbox.domain.view.DyneTextView;
import com.lianyao.ftfbox.domain.view.MovieLayout;
import com.lianyao.ftfbox.domain.view.ShowOp;
import com.lianyao.ftfbox.util.AppUtil;
import com.lianyao.ftfbox.util.CommonUtil;
import com.lianyao.ftfbox.util.Logger;
import com.lianyao.ftfbox.util.NetUtil;
import com.lianyao.ftfbox.util.SPUtil;
import com.lianyao.ftfbox.util.ToastUtil;
import com.lianyao.ftfbox.util.http.RestInterface;
import com.lianyao.ftfbox.util.http.RestRequest;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

import org.webrtc.SurfaceViewRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements OnClickListener, ShowOp, CallStateListener {
    private ImageView startActivity_bg_img;
    // 更新版本要用到的一些信息
    /**
     * 更新
     */
    String downloadUrl = "";

    private UpdateInfo info = new UpdateInfo();
    ;
    private ProgressDialog pBar;
    @SuppressLint("HandlerLeak")

    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (isNeedUpdate()) {   //在下面的代码段
                showUpdateDialog();  //下面的代码段
            } else {
                return;
            }
        }

        ;
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + info.getName());
        builder.setMessage(info.getFeature());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info.getTargetUrl());     //在下面的代码段
                } else {
                    Toast.makeText(MainActivity.this, "升级失败！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private boolean isNeedUpdate() {
        int code;
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            code = 0;
        }
        if (info.getCode() <= code) {
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    void downFile(final String url) {
        pBar = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.show();
        new Thread() {
            public void run() {
                HttpURLConnection urlcon = null;
                URL urll;
                try {
                    urll = new URL(url);
                    urlcon = (HttpURLConnection) urll.openConnection();
                    int length = (int) urlcon.getContentLength();   //获取文件大小
                    pBar.setMax(length);
                    InputStream is = urlcon.getInputStream();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                Constants.APP_FILE);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[4096];   //缓冲区
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            pBar.setProgress(process);       //这里就是关键的实时更新进度了！
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }.start();
    }

    void down() {
        handler1.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    //安装文件，一般固定写法
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), Constants.APP_FILE)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public class GetUpdateInfoResult implements RestInterface {
        @Override
        public void onSuccess(JSONObject json) {
            String result = json.getString("result");
            if (!result.equals("1")) {
                return;
            } else {
                JSONObject obj = json.getJSONObject("obj");
                int code = obj.getIntValue("code");
                downloadUrl = obj.getString("downLoadUrl");
                String name = obj.getString("name");
                String feature = obj.getString("feature");
                String url = obj.getString("downLoadUrl");

                info.setCode(code);
                info.setName(name);
                info.setFeature(feature);
                info.setTargetUrl(url);
                handler1.sendEmptyMessage(0);
            }
        }

        public void onLoading(long total, long current, boolean isUploading) {

        }

        public void onFailure(HttpException error, String msg) {

        }
    }

    private TextView tv_number;
    private EditText edit_num;
    private DyneTextView dtv_number1;
    private DyneTextView dtv_number2;
    private DyneTextView dtv_number3;
    private DyneTextView dtv_number4;
    private DyneTextView dtv_number5;
    private DyneTextView dtv_number6;
    private DyneTextView dtv_number7;
    private DyneTextView dtv_number8;
    private DyneTextView dtv_number9;
    private DyneTextView dtv_number0;
    private DyneTextView dtv_numberc;
    private DyneTextView dtv_numberd;
    private Button dtv_kuaijie;
    private DyneTextView dtv_yeyey;
    private DyneTextView dtv_baba;
    private DyneTextView dtv_erzi;
    private DyneTextView dtv_sunzi;
    private DyneTextView dtv_haoyou1;
    private DyneTextView dtv_nainai;
    private DyneTextView dtv_mama;
    private DyneTextView dtv_nver;
    private DyneTextView dtv_suner;
    private DyneTextView dtv_haoyou2;
    private DyneTextView dtv_cancle;
    private DyneTextView dtv_yuyin;
    private DyneTextView dtv_shipin;
    private DyneTextView dtv_shanchu;
    private DyneTextView dtv_cancle2;
    private ImageView tv_yuyin;
    private ImageView tv_shipin;
    private static DyneTextView dtv_jietingshi;
    private DyneTextView dtv_jietingfou; // 语音视频接听取消（挂断）
    private String boxNum = "";
    private MovieLayout movieLayout;
    private MovieAdapter adapter;
    private static View view = null; // 快捷拨号弹出层 View
    private View opView = null; // 语音视频删除 弹出层View
    private static View yuyinshipinView = null; // 语音视频接听弹出层
    private PopupWindow popupWindow, oppopupWindow;
    private static PopupWindow avpupupWindow;
    private DbUtils db;
    private JSONObject userJson;
    private Contact contact;
    private int opFlag = -1;
    private static int avFlag = 1; // 1语音 2视频
    private boolean isOnline = false;
    private TextView tv_online_status;
    private static boolean showTime = false;
    private boolean show = false;

    /**
     * 音视频通话
     */
    private PercentFrameLayout localRenderLayout;
    private PercentFrameLayout remoteRenderLayout;
    private SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRender;
    private RtcClient client = RtcClient.getInstance();
    private RtcCallManager rtcCallManager = client.getCallManager();
    private DyneTextView dtv_cut_call;

    private static ImageView img_ren;
    // 目前语音视频使用一个现实界面，如果语音单独界面开启，这个控件就是现实语音界面手机号的
    private TextView tv_nickname_yy;
    private static TextView tv_number_yy;
    private DyneTextView dtv_cut_yycall;
    private Handler handler = new Handler();
    private long time = 0;
    private LinearLayout ll_phone;  //通话中拨号盘
    private LinearLayout ll_number; //普通拨号盘
    private TextView tv_phonenumber;//通话号码
    private TextView tv_phonetime;//通话号码
    private ImageView tv_phone_halfscreen; //半屏
    private ImageView tv_phone_bigsmallscreen;//大小屏
    private ImageView tv_phone_fullscreen;//全屏
    private DyneTextView dtv_exit_full_call; //退出全屏
    private DyneTextView tv_phone_over;         //挂断
    private ImageView tv_phone_speaker;         //静音
    private ImageView tv_phone_mike;         //speaker
    private ImageView tv_phone_camera;     //关闭摄像头
    private HorizontalScrollView ll_bottom;
    private boolean bIsAudioEnabled = true;
    //	private boolean bSpeaker = true;
    private boolean bIsCamera = true;
    private boolean isMianti = true;
//    private MyReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_index);
        MainApplication.setActivity(this);
//        myReceiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("myIncomingCall");
//        registerReceiver(myReceiver, intentFilter);
//        if (!AppUtil.isPhone(this)) {
        init();

        client.rtcSetting.setRing(true, R.raw.call_bell);
        client.rtcSetting.setCallRing(true, R.raw.phone_ring);

        boxNum = (String) SPUtil.get(MainActivity.this, "boxNum", boxNum);
        db = DbUtils.create(this, Constants.DB_FILE, 1, null);

        JSONObject param = new JSONObject();
        param.put("deviceId", AppUtil.getDeviceIMEI(this));
        getRequest().setRestPost(new BoxNumResult(), "getBoxNum.json",
                param);

        try {
            adapter = new MovieAdapter(this);
            List<Contact> list = db.findAll(Contact.class);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    adapter.addObject(list.get(i));
                }
                movieLayout.setAdapter(adapter);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        IntentFilter intentFilter = new IntentFilter(RtcBroadcast.onHangUp);
        registerReceiver(broadcastReceiver, intentFilter);
//        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("该安装包仅适用于机顶盒");
//            builder.setMessage("您好，该安装包为机顶盒版安装包，手机版可去下载对应的手机版本。");
//            builder.setPositiveButton("确定",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//                            finish();
//                        }
//                    });
//            builder.show();
//        }

        // 自动检查有没有新版本 如果有新版本就提示更新
        JSONObject vparam = new JSONObject();
        vparam.put("type", "2"); //盒子
        getRequest().setRestPost(new GetUpdateInfoResult(), "getVersionInfo.json", vparam);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dtv_number1:
                edit_num.setText(edit_num.getText().toString() + "1");
                break;
            case R.id.dtv_number2:
                edit_num.setText(edit_num.getText().toString() + "2");
                break;
            case R.id.dtv_number3:
                edit_num.setText(edit_num.getText().toString() + "3");
                break;
            case R.id.dtv_number4:
                edit_num.setText(edit_num.getText().toString() + "4");
                break;
            case R.id.dtv_number5:
                edit_num.setText(edit_num.getText().toString() + "5");
                break;
            case R.id.dtv_number6:
                edit_num.setText(edit_num.getText().toString() + "6");
                break;
            case R.id.dtv_number7:
                edit_num.setText(edit_num.getText().toString() + "7");
                break;
            case R.id.dtv_number8:
                edit_num.setText(edit_num.getText().toString() + "8");
                break;
            case R.id.dtv_number9:
                edit_num.setText(edit_num.getText().toString() + "9");
                break;
            case R.id.dtv_number0:
                edit_num.setText(edit_num.getText().toString() + "0");
                break;
            case R.id.dtv_numberc:
                edit_num.setText("");
                break;
            case R.id.dtv_numberd:
                String telNum = edit_num.getText().toString();
                if (!CommonUtil.isEmpty(telNum)) {
                    edit_num.setText(telNum.substring(0, telNum.length() - 1));
                }
                break;
            case R.id.dtv_yeyey:
                saveContact(0);
                break;
            case R.id.dtv_baba:
                saveContact(1);
                break;
            case R.id.dtv_erzi:
                saveContact(2);
                break;
            case R.id.dtv_sunzi:
                saveContact(3);
                break;
            case R.id.dtv_haoyou1:
                saveContact(4);
                break;
            case R.id.dtv_nainai:
                saveContact(5);
                break;
            case R.id.dtv_mama:
                saveContact(6);
                break;
            case R.id.dtv_nver:
                saveContact(7);
                break;
            case R.id.dtv_suner:
                saveContact(8);
                break;
            case R.id.dtv_haoyou2:
                saveContact(9);
                break;
            case R.id.dtv_cancle:
                userJson = null;
                popupWindow.dismiss();
                break;
            case R.id.dtv_yuyin:
                shipintongxin(contact, 1);
                break;
            case R.id.dtv_shipin:
                shipintongxin(contact, 2);
                break;
            case R.id.dtv_shanchu:
                try {
                    db.deleteById(Contact.class, contact.getId());
                    adapter = new MovieAdapter(this);
                    List<Contact> list = db.findAll(Contact.class);
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            adapter.addObject(list.get(i));
                        }
                    }
                    movieLayout.setAdapter(adapter);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                oppopupWindow.dismiss();
                break;

            case R.id.dtv_kuaijie:
                String telNumber = edit_num.getText().toString();
                if (CommonUtil.isEmpty(telNumber)) {
                    ToastUtil.showShort(this, "要添加的号码不能为空");
                } else {
                    opFlag = 1;
                    JSONObject param = new JSONObject();
                    param.put("mobile", telNumber);
                    getRequest().setRestPost(new FriendInfoResult(),
                            "getUserInfoByMobile.json", param);
                }
                break;

            case R.id.tv_yuyin:
                String telNumber2 = edit_num.getText().toString();
                if (CommonUtil.isEmpty(telNumber2)) {
                    ToastUtil.showShort(this, "请输入通信号码");
                } else {
                    opFlag = 2;
                    JSONObject param = new JSONObject();
                    param.put("mobile", telNumber2);
                    getRequest().setRestPost(new FriendInfoResult(),
                            "getUserInfoByMobile.json", param);
                }
                break;

            case R.id.tv_shipin:
                String telNumber3 = edit_num.getText().toString();
                if (CommonUtil.isEmpty(telNumber3)) {
                    ToastUtil.showShort(this, "请输入通信号码");
                } else {
                    opFlag = 3;
                    JSONObject param = new JSONObject();
                    param.put("mobile", telNumber3);
                    getRequest().setRestPost(new FriendInfoResult(),
                            "getUserInfoByMobile.json", param);
                }
                break;

            case R.id.dtv_cut_call:
            case R.id.dtv_cut_yycall:
            case R.id.tv_phone_over:
                callOff();
                break;

        }
    }

    @SuppressLint("SimpleDateFormat")
    private void saveContact(int imgTag) {
        try {
            Contact contact = db.findFirst(Selector.from(Contact.class).where(
                    "mobile", "=", userJson.getString("name")));
            if (contact != null) {
                contact.setCreateDate(new SimpleDateFormat("yyyyMMddHHmmss")
                        .format(new Date()));
                db.deleteById(Contact.class, contact.getId());
            }
            contact = new Contact();
            contact.setCreateDate(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date()));
            contact.setImgTag(imgTag);
            contact.setMobile(userJson.getString("name"));
            if (imgTag == 0) {
                contact.setName("爷爷");
            } else if (imgTag == 1) {
                contact.setName("爸爸");
            } else if (imgTag == 2) {
                contact.setName("儿子");
            } else if (imgTag == 3) {
                contact.setName("孙子");
            } else if (imgTag == 4) {
                contact.setName("好友");
            } else if (imgTag == 5) {
                contact.setName("奶奶");
            } else if (imgTag == 6) {
                contact.setName("妈妈");
            } else if (imgTag == 7) {
                contact.setName("女儿");
            } else if (imgTag == 8) {
                contact.setName("孙女");
            } else if (imgTag == 9) {
                contact.setName("好友");
            }
            contact.setNickname(userJson.getString("nickname"));
            contact.setTid(userJson.getString("tid"));
            contact.setUid(userJson.getString("uid"));
            db.save(contact);
        } catch (DbException e) {
            e.printStackTrace();
        }
        adapter = new MovieAdapter(this);
        try {
            List<Contact> list = db.findAll(Contact.class);
            for (int i = 0; i < list.size(); i++) {
                adapter.addObject(list.get(i));
            }
            movieLayout.setAdapter(adapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
        popupWindow.dismiss();
    }

    private void init() {
        view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.activity_select, (ViewGroup) null, false);
        opView = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.activity_operation, (ViewGroup) null, false);
        yuyinshipinView = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.activity_yuyinshipin, (ViewGroup) null, false);
        dtv_jietingshi = (DyneTextView) yuyinshipinView
                .findViewById(R.id.dtv_jietingshi);
        dtv_jietingfou = (DyneTextView) yuyinshipinView
                .findViewById(R.id.dtv_jietingfou);

        // 接听
        dtv_jietingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                localRender.release();
                remoteRender.release();
//                updateVideo(new int[]{59, 59, 40, 40}, new int[]{0, 0, 100, 100});
                updateVideo(new int[]{51, 0, 100, 100}, new int[]{0, 0, 49, 100});
                rtcCallManager.setLocalSurfaceView(localRender);
                rtcCallManager.setRemoteSurfaceView(remoteRender);
                rtcCallManager.addCallStateListener(MainActivity.this);
                rtcCallManager.setUserName(mobile);
                if (avFlag == 2) {
                    rtcCallManager.answerCall(MainActivity.this);
                } else {
                    rtcCallManager.answerAudioCall(MainActivity.this);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callingView();
                    }
                });
            }
        });
        dtv_jietingfou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                rtcCallManager.stopCall(mobile);
//                findViewById(R.id.img_zhedang).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_bottom).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_video).setVisibility(View.GONE);
                findViewById(R.id.rl_yuyin).setVisibility(View.GONE);
                avpupupWindow.dismiss();
            }
        });
        tv_number = (TextView) findViewById(R.id.tv_number);
        edit_num = (EditText) findViewById(R.id.edit_num);
        dtv_number1 = (DyneTextView) findViewById(R.id.dtv_number1);
        dtv_number2 = (DyneTextView) findViewById(R.id.dtv_number2);
        dtv_number3 = (DyneTextView) findViewById(R.id.dtv_number3);
        dtv_number4 = (DyneTextView) findViewById(R.id.dtv_number4);
        dtv_number5 = (DyneTextView) findViewById(R.id.dtv_number5);
        dtv_number6 = (DyneTextView) findViewById(R.id.dtv_number6);
        dtv_number7 = (DyneTextView) findViewById(R.id.dtv_number7);
        dtv_number8 = (DyneTextView) findViewById(R.id.dtv_number8);
        dtv_number9 = (DyneTextView) findViewById(R.id.dtv_number9);
        dtv_number0 = (DyneTextView) findViewById(R.id.dtv_number0);
        dtv_numberc = (DyneTextView) findViewById(R.id.dtv_numberc);
        dtv_numberd = (DyneTextView) findViewById(R.id.dtv_numberd);
        dtv_kuaijie = (Button) findViewById(R.id.dtv_kuaijie);
        tv_yuyin = (ImageView) findViewById(R.id.tv_yuyin);
        tv_shipin = (ImageView) findViewById(R.id.tv_shipin);
        dtv_yeyey = (DyneTextView) view.findViewById(R.id.dtv_yeyey);
        dtv_baba = (DyneTextView) view.findViewById(R.id.dtv_baba);
        dtv_erzi = (DyneTextView) view.findViewById(R.id.dtv_erzi);
        dtv_sunzi = (DyneTextView) view.findViewById(R.id.dtv_sunzi);
        dtv_haoyou1 = (DyneTextView) view.findViewById(R.id.dtv_haoyou1);
        dtv_nainai = (DyneTextView) view.findViewById(R.id.dtv_nainai);
        dtv_mama = (DyneTextView) view.findViewById(R.id.dtv_mama);
        dtv_nver = (DyneTextView) view.findViewById(R.id.dtv_nver);
        dtv_suner = (DyneTextView) view.findViewById(R.id.dtv_suner);
        dtv_haoyou2 = (DyneTextView) view.findViewById(R.id.dtv_haoyou2);
        dtv_cancle = (DyneTextView) view.findViewById(R.id.dtv_cancle);
        dtv_yuyin = (DyneTextView) opView.findViewById(R.id.dtv_yuyin);
        dtv_shipin = (DyneTextView) opView.findViewById(R.id.dtv_shipin);
        dtv_shanchu = (DyneTextView) opView.findViewById(R.id.dtv_shanchu);
        dtv_cancle2 = (DyneTextView) opView.findViewById(R.id.dtv_cancle);
        movieLayout = (MovieLayout) findViewById(R.id.movieLayout);
        dtv_number1.setOnClickListener(this);
        dtv_number2.setOnClickListener(this);
        dtv_number3.setOnClickListener(this);
        dtv_number4.setOnClickListener(this);
        dtv_number5.setOnClickListener(this);
        dtv_number6.setOnClickListener(this);
        dtv_number7.setOnClickListener(this);
        dtv_number8.setOnClickListener(this);
        dtv_number9.setOnClickListener(this);
        dtv_number0.setOnClickListener(this);
        dtv_numberc.setOnClickListener(this);
        dtv_numberd.setOnClickListener(this);
        dtv_kuaijie.setOnClickListener(this);
        dtv_yeyey.setOnClickListener(this);
        dtv_baba.setOnClickListener(this);
        dtv_erzi.setOnClickListener(this);
        dtv_sunzi.setOnClickListener(this);
        dtv_haoyou1.setOnClickListener(this);
        dtv_nainai.setOnClickListener(this);
        dtv_mama.setOnClickListener(this);
        dtv_nver.setOnClickListener(this);
        dtv_suner.setOnClickListener(this);
        dtv_haoyou2.setOnClickListener(this);
        dtv_cancle.setOnClickListener(this);
        tv_yuyin.setOnClickListener(this);
        tv_shipin.setOnClickListener(this);
        dtv_cancle2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                contact = null;
                oppopupWindow.dismiss();
            }
        });
        dtv_yuyin.setOnClickListener(this);
        dtv_shipin.setOnClickListener(this);
        dtv_shanchu.setOnClickListener(this);
        movieLayout.setOnShowListener(this);

        dtv_cut_call = (DyneTextView) findViewById(R.id.dtv_cut_call);
        dtv_cut_call.setOnClickListener(this);
        localRenderLayout = (PercentFrameLayout) findViewById(R.id.local_video_layout);
        remoteRenderLayout = (PercentFrameLayout) findViewById(R.id.remote_video_layout);
        localRender = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        localRender.setZOrderMediaOverlay(true);
        remoteRender = (SurfaceViewRenderer) findViewById(R.id.remote_video_view);
        img_ren = (ImageView) findViewById(R.id.img_ren);
        tv_nickname_yy = (TextView) findViewById(R.id.tv_nickname_yy);
        tv_number_yy = (TextView) findViewById(R.id.tv_number_yy);
        dtv_cut_yycall = (DyneTextView) findViewById(R.id.dtv_cut_yycall);
        dtv_cut_yycall.setOnClickListener(this);

        tv_number.setText(String.format(getString(R.string.my_number),
                boxNum));
        tv_online_status = (TextView) findViewById(R.id.tv_online_status);

        ll_number = (LinearLayout) findViewById(R.id.ll_number);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        tv_phone_halfscreen = (ImageView) findViewById(R.id.tv_phone_halfscreen);
        tv_phone_halfscreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ll_video).setVisibility(View.VISIBLE);
                dtv_exit_full_call.setVisibility(View.GONE);
                dtv_cut_call.setVisibility(View.GONE);
                movieLayout.setVisibility(View.GONE);
                movieLayout.setFocusable(false);
                movieLayout.setEnabled(false);
                updateVideo(new int[]{51, 0, 100, 100}, new int[]{0, 0, 49, 100});
            }
        });


        tv_phone_bigsmallscreen = (ImageView) findViewById(R.id.tv_phone_bigsmallscreen);
        tv_phone_bigsmallscreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ll_video).setVisibility(View.VISIBLE);
                dtv_exit_full_call.setVisibility(View.GONE);
                dtv_cut_call.setVisibility(View.GONE);
                movieLayout.setFocusable(false);
                movieLayout.setEnabled(false);
                movieLayout.setVisibility(View.GONE);
                updateVideo(new int[]{59, 59, 40, 40}, new int[]{0, 0, 100, 100});
            }
        });

        tv_phone_fullscreen = (ImageView) findViewById(R.id.tv_phone_fullscreen);
        tv_phone_fullscreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_number.setVisibility(View.GONE);
                ll_phone.setVisibility(View.GONE);
                dtv_exit_full_call.setVisibility(View.VISIBLE);
                dtv_cut_call.setVisibility(View.VISIBLE);
                movieLayout.setEnabled(false);
                movieLayout.setFocusable(false);
                movieLayout.setVisibility(View.GONE);
                updateVideo(new int[]{0, 0, 0, 0}, new int[]{0, 0, 100, 100});
            }
        });

        dtv_exit_full_call = (DyneTextView) findViewById(R.id.dtv_exit_full_call);
        dtv_exit_full_call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_phone.setVisibility(View.VISIBLE);
                dtv_exit_full_call.setVisibility(View.GONE);
                dtv_cut_call.setVisibility(View.GONE);
                movieLayout.setFocusable(false);
                movieLayout.setVisibility(View.VISIBLE);
                movieLayout.setVisibility(View.GONE);
//                updateVideo(new int[]{59, 59, 40, 40}, new int[]{0, 0, 100, 100});
                updateVideo(new int[]{51, 0, 100, 100}, new int[]{0, 0, 49, 100});

            }
        });

        tv_phone_over = (DyneTextView) findViewById(R.id.tv_phone_over);
        tv_phone_over.setOnClickListener(this);

        tv_phonenumber = (TextView) findViewById(R.id.tv_phonenumber);
        tv_phonetime = (TextView) findViewById(R.id.tv_phonetime);
//        updateVideo(new int[]{59, 59, 40, 40}, new int[]{0, 0, 100, 100});
        updateVideo(new int[]{51, 0, 100, 100}, new int[]{0, 0, 49, 100});

        tv_phone_speaker = (ImageView) findViewById(R.id.tv_phone_speaker);
//		tv_phone_mute.setNextFocusRightId(R.id.tv_phone_camera);
        tv_phone_speaker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isMianti = !isMianti;
                if (isMianti) tv_phone_speaker.setBackgroundResource(R.drawable.bg_btn_speaker);
                else tv_phone_speaker.setBackgroundResource(R.drawable.bg_btn_speaker_no);
                rtcCallManager.getVideoCallHelper().speaker(isMianti);
            }
        });

        tv_phone_mike = (ImageView) findViewById(R.id.tv_phone_mike);
        tv_phone_mike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bIsAudioEnabled = !bIsAudioEnabled;
                if (bIsAudioEnabled) {
                    tv_phone_mike.setBackgroundResource(R.drawable.bg_btn_mike);
                    rtcCallManager.getVideoCallHelper().restoreAudio();
                }
                if (!bIsAudioEnabled) {
                    tv_phone_mike.setBackgroundResource(R.drawable.bg_btn_mike_no);
                    rtcCallManager.getVideoCallHelper().stopAudio();
                }
            }
        });

        tv_phone_camera = (ImageView) findViewById(R.id.tv_phone_camera);
        tv_phone_camera.setFocusable(true);
        tv_phone_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bIsCamera = !bIsCamera;
                if (bIsCamera) {
                    tv_phone_camera.setBackgroundResource(R.drawable.bg_btn_camera);
                    rtcCallManager.getVideoCallHelper().restoreVideo();
                } else {
                    tv_phone_camera.setBackgroundResource(R.drawable.bg_btn_camera_no);
                    rtcCallManager.getVideoCallHelper().stopVideo();
                }
            }
        });
        if (mTimer == null) {
            mTimer = new Timer();
        }
        setTimerTask();

        boolean isback = getIntent().getBooleanExtra("isback", false);
        if (isback) {
            Intent intent = new Intent();
            intent.putExtra("nickname", getIntent().getStringExtra("mobile"));
            intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
            intent.putExtra("isVideo", getIntent().getBooleanExtra("isVideo", false));
            intent.setAction("myIncomingCall");
            sendBroadcast(intent);
        }
    }

    private void updateVideo(int[] local, int[] remote) {
        remoteRenderLayout.setPosition(remote[0], remote[1], remote[2],
                remote[3]);
        localRenderLayout.setPosition(local[0], local[1], local[2], local[3]);
        localRenderLayout.requestLayout();
        remoteRenderLayout.requestLayout();
    }

    /**
     * 返回键处理
     */
    @Override
    public void onBackPressed() {
        if (oppopupWindow != null)
            oppopupWindow.dismiss();
        if (popupWindow !=null)
            popupWindow.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (oppopupWindow != null)
                oppopupWindow.dismiss();
            if (popupWindow !=null)
                popupWindow.dismiss();
        }
        return false;
    }

    private class FriendInfoResult implements RestInterface {
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
        }

        @Override
        public void onSuccess(JSONObject json) {
            if ("2".equals(json.getString("result"))) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(MainActivity.this, "号码不存在");
                    }
                });
            } else {
                if (opFlag == 1) {
                    userJson = json.getJSONObject("obj");
                    if (MainActivity.this.isFinishing()) {
                        return;
                    }
                    popupWindow = new PopupWindow(view,
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT, false);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                } else if (opFlag == 2) { // 语音通信
                    userJson = json.getJSONObject("obj");
                    contact = new Contact();
                    contact.setMobile(userJson.getString("name"));
                    contact.setNickname(userJson.getString("nickname"));
                    contact.setTid(userJson.getString("tid"));
                    contact.setUid(userJson.getString("uid"));
                    shipintongxin(contact, 1);
                } else if (opFlag == 3) { // 视频通信
                    userJson = json.getJSONObject("obj");
                    contact = new Contact();
                    contact.setMobile(userJson.getString("name"));
                    contact.setNickname(userJson.getString("nickname"));
                    contact.setTid(userJson.getString("tid"));
                    contact.setUid(userJson.getString("uid"));
                    shipintongxin(contact, 2);
                }
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
        }
    }

    private class BoxNumResult implements RestInterface, RegisterCallBack {
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
        }

        @Override
        public void onSuccess(JSONObject json) {
            boxNum = json.getString("obj");
            tv_number.setText(String.format(getString(R.string.my_number),
                    boxNum));
            // 没有这个用户，需要先sdk注册，再inter注册
            if ("2".equals(json.getString("result"))) {
                RtcClient.getInstance().register(boxNum, this);
            } else {
                JSONObject param = new JSONObject();
                param.put("mobile", boxNum);
                param.put("password", "123456");
                getRequest()
                        .setRestPost(new LoginResult(), "login.json", param);
            }
        }

        /**
         * 注册成功
         */
        @Override
        public void onSuccess(org.json.JSONObject jsonObject) {
            Logger.e(jsonObject.toString());
            JSONObject jsonUser = JSONObject.parseObject(jsonObject.toString());
            JSONObject jsonTm = jsonUser.getJSONObject("tm");
            String tid = jsonTm.getString("tid");
            String token = jsonTm.getString("token");
            String uid = jsonUser.getString("uid");

            JSONObject param = new JSONObject();
            param.put("imei", AppUtil.getDeviceIMEI(MainActivity.this));
            param.put("mobile", boxNum);
            param.put("nickname", boxNum);
            param.put("password", "123456");
            param.put("type", "2");
            param.put("uid", uid);
            param.put("tid", tid);
            param.put("token", token);
            getRequest()
                    .setRestPost(new RegisteResult(), "registe.json", param);
        }

        /**
         * 调用本地服务器失败
         */
        @Override
        public void onFailure(HttpException error, String msg) {
        }

        /**
         * sip注册失败
         */
        @Override
        public void onError(String message) {
        }

    }

    private class LoginResult implements RestInterface {
        @Override
        public void onLoading(long total, long current, boolean isUploading) {

        }

        @Override
        public void onSuccess(JSONObject json) {
            if ("2".equals(json.getString("result"))) {
                ToastUtil.showShort(MainActivity.this,
                        json.getString("message"));
            } else {
                JSONObject jsonUser = json.getJSONObject("obj");
                SPUtil.put(MainActivity.this, "id", jsonUser.getString("id"));
                RtcClient.getInstance().login(boxNum, jsonUser.getString("token"),
                        new LoginClass(jsonUser.getString("token")));

            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {

        }

    }

    class LoginClass implements LoginCallback {
        String token;

        public LoginClass(String token) {
            this.token = token;
        }

        @Override
        public void onUpdateToken(String tInfo) {
            // 保存新获取到的token
            JSONObject param = new JSONObject();
            param.put("id",
                    SPUtil.get(MainActivity.this, "id", "-1"));
            param.put("token", token);
            getRequest().setRestPost(new UpdateTokenClass(),
                    "updateToken.json", param);
        }

        @Override
        public void onSussess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onFailure(int state, String message) {
        }

    }

    class UpdateTokenClass implements RestInterface {
        @Override
        public void onLoading(long total, long current, boolean isUploading) {

        }

        @Override
        public void onSuccess(JSONObject json) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onFailure(HttpException error, String msg) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.removeMainActivity();
        unregisterReceiver(broadcastReceiver);
        if (findViewById(R.id.ll_bottom).getVisibility() == View.GONE) {
            rtcCallManager.stopCall(contact.getMobile());
//            findViewById(R.id.img_zhedang).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_bottom).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_video).setVisibility(View.GONE);
            findViewById(R.id.rl_yuyin).setVisibility(View.GONE);
        }
    }

    private class RegisteResult implements RestInterface {

        @Override
        public void onLoading(long total, long current, boolean isUploading) {

        }

        @Override
        public void onSuccess(JSONObject json) {
            JSONObject param = new JSONObject();
            param.put("mobile", boxNum);
            param.put("password", "123456");
            getRequest().setRestPost(new LoginResult(), "login.json", param);
        }

        @Override
        public void onFailure(HttpException error, String msg) {

        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (boxNum != null && !"".equals(boxNum)) {
            JSONObject param = new JSONObject();
            param.put("mobile", boxNum);
            param.put("password", "123456");
            getRequest()
                    .setRestPost(new LoginResult(), "login.json", param);
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    // 异步请求对象
    public RestRequest getRequest() {
        if (!NetUtil.isConnected(this)) {
            Toast.makeText(this, "请检查网络连接", Toast.LENGTH_LONG).show();
        }
        return RestRequest.getInstance(this);
    }

    @Override
    public void show(Contact contact) {
        if (MainActivity.this.isFinishing()) {
            return;
        }
        this.contact = contact;
        oppopupWindow = new PopupWindow(opView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, false);
        ImageView opImg = (ImageView) opView.findViewById(R.id.img_ren);
        TextView tv_nickname = (TextView) opView.findViewById(R.id.tv_nickname);
        TextView tv_number = (TextView) opView.findViewById(R.id.tv_number);
        dtv_shipin.setFocusable(true);
        dtv_shipin.requestFocus();
        tv_number.setText(contact.getMobile());
        int imgTag = contact.getImgTag();
        if (imgTag == 0) {
            opImg.setBackgroundResource(R.drawable.ye);
            tv_nickname.setText("爷爷");
        } else if (imgTag == 1) {
            opImg.setBackgroundResource(R.drawable.ba);
            tv_nickname.setText("爸爸");
        } else if (imgTag == 2) {
            opImg.setBackgroundResource(R.drawable.er);
            tv_nickname.setText("儿子");
        } else if (imgTag == 3) {
            opImg.setBackgroundResource(R.drawable.sun);
            tv_nickname.setText("孙子");
        } else if (imgTag == 4) {
            opImg.setBackgroundResource(R.drawable.hao);
            tv_nickname.setText("好友");
        } else if (imgTag == 5) {
            opImg.setBackgroundResource(R.drawable.nai);
            tv_nickname.setText("奶奶");
        } else if (imgTag == 6) {
            opImg.setBackgroundResource(R.drawable.ma);
            tv_nickname.setText("妈妈");
        } else if (imgTag == 7) {
            opImg.setBackgroundResource(R.drawable.nv);
            tv_nickname.setText("女儿");
        } else if (imgTag == 8) {
            opImg.setBackgroundResource(R.drawable.sn);
            tv_nickname.setText("孙女");
        } else if (imgTag == 9) {
            opImg.setBackgroundResource(R.drawable.hy);
            tv_nickname.setText("好友");
        }
        oppopupWindow.setOutsideTouchable(true);
        oppopupWindow.setFocusable(true);
        oppopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callOffView();
                }
            });
        }
    };

    // types 1语音 2视频
    private void shipintongxin(Contact contact, Integer types) {
        if (findViewById(R.id.ll_bottom).getVisibility() == View.GONE) {
            ToastUtil.showShort(this, "通话中，请勿直接拨打");
            return;
        }
        avFlag = types;
        if (oppopupWindow != null)
            oppopupWindow.dismiss();
//        findViewById(R.id.img_zhedang).setVisibility(View.GONE);
        findViewById(R.id.ll_bottom).setVisibility(View.GONE);
        findViewById(R.id.ll_video).setVisibility(View.VISIBLE);
        findViewById(R.id.rl_yuyin).setVisibility(View.GONE);
        localRender.release();
        remoteRender.release();
//        updateVideo(new int[]{59, 59, 40, 40}, new int[]{0, 0, 100, 100});
        updateVideo(new int[]{51, 0, 100, 100}, new int[]{0, 0, 49, 100});
        rtcCallManager.setLocalSurfaceView(localRender);
        rtcCallManager.setRemoteSurfaceView(remoteRender);
        rtcCallManager.addCallStateListener(this);
        if (types == 2) {
            rtcCallManager.makeCall(contact.getMobile(), this);
        } else {
            rtcCallManager.makeAudioCall(contact.getMobile(), this);
        }
    }

    @Override
    public void onCallStateChanged(final CallState callState, final CallError error) {
        Logger.e(callState + "--" + error);
        if (callState != null || error != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = (callState == null) ? error + "" : callState + "";
//                    ToastUtil.showShort(MainActivity.this, msg);
                }
            });
        }
        String msg = "";
        if (callState != null) {
            switch (callState) {
                case CONNECTING:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(MainActivity.this, "正在拨号，请稍等");
                            callingView();
                        }
                    });
                    break;

                case DISCONNNECTED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(MainActivity.this, "通话结束");
                            callOffView();
                        }
                    });
                    break;
                case CONNECTED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(MainActivity.this, "对方已响铃");
                            callingView();
                        }
                    });
                    break;
                case ACCEPTED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(MainActivity.this, "对方已接听");
                        }
                    });
                    showTime = true;
                    handler.postDelayed(updateTime, 1000);
                    break;
                default:
                    break;
            }
        } else if (error != null) {
            switch (error) {
                case REJECTED:
                    msg = "对方已挂断";
                    break;
                case ERROR_NO_DATA:
                    msg = "对方号码不存在";
                    break;
                case ERROR_TRANSPORT:
                    msg = "数据传输错误";
                    break;
                case ERROR_INAVAILABLE:
                    msg = "对方不在线";
                    break;
                case ERROR_BUSY:
                    msg = "对方正在通话中";
                    break;
                case ERROR_NONENTITY:
                    msg = "对方号码不存在";
                    break;
                case ERROR_NORESPONSE:
                    msg = "对方未接通";
                    break;
                case ERROR_LOCAL_VERSION_SMALLER:
                    break;
                case ERROR_PEER_VERSION_SMALLER:
                    break;

                default:
                    break;
            }
            final String message = msg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(message != null) {
                        ToastUtil.showShort(MainActivity.this, message);
                    }
                    callOffView();
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            boolean isback = getIntent().getBooleanExtra("isback", false);
            if (isback && !show) {
                show = true;
                System.out.println("onWindowFocusChanged: isback");
                incomingCall(getIntent());
//                Intent intent = new Intent();
//                intent.putExtra("nickname", getIntent().getStringExtra("mobile"));
//                intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
//                intent.putExtra("isVideo", getIntent().getBooleanExtra("isVideo", false));
//                intent.setAction("myIncomingCall");
//                sendBroadcast(intent);
            }
        }
    }

    /**
     * 在线状态
     */
    private void setOnLine(boolean isOnline) {
        if (isOnline) {
            tv_online_status.setText("(在线)");
            tv_online_status.setTextColor(Color.GREEN);
        } else {
            tv_online_status.setText("(离线)");
            tv_online_status.setTextColor(Color.GRAY);
        }
    }

    public static String mobile;

    public static class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RtcBroadcast.onIncomingCall)) {
                MtcLog.e("MyReceiver", "myIncomingCall");
                incomingCall(intent);
            }
        }
    }

    private static void incomingCall(Intent intent) {
        mobile = intent.getStringExtra("mobile");
        if (avpupupWindow != null && avpupupWindow.isShowing()) {
            avpupupWindow.dismiss();
        }
        // boolean isAudio = intent.getBooleanExtra("isAudio", false);
        boolean isVideo = intent.getBooleanExtra("isVideo", false);
        if (isVideo) {
            avFlag = 2;
            ((ImageView) yuyinshipinView
                    .findViewById(R.id.img_yuyinshipin))
                    .setBackgroundResource(R.drawable.img_shipin);
        } else {
            avFlag = 1;
            ((ImageView) yuyinshipinView
                    .findViewById(R.id.img_yuyinshipin))
                    .setBackgroundResource(R.drawable.img_yuyin);
            tv_number_yy.setText(mobile);
            img_ren.setBackgroundResource(R.drawable.hao);
        }
        ((TextView) yuyinshipinView.findViewById(R.id.tv_yuyinshipin))
                .setText(mobile);
        dtv_jietingshi.setFocusable(true);
        dtv_jietingshi.requestFocus();
        avpupupWindow = new PopupWindow(yuyinshipinView,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                false);
        avpupupWindow.setOutsideTouchable(true);
        avpupupWindow.setFocusable(true);
        if (view != null) {
            avpupupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 更新通话时间
     */
    private Runnable updateTime = new Runnable() {

        @Override
        public void run() {
            time += 1;
            tv_phonetime.setText(CommonUtil.formatTimeBySecond(time));
            // li：1s后启动这个线程刷新时间，到控件上；1s后再发送循环；
            if (showTime) handler.postDelayed(this, 1000);
        }
    };

    private void callingView() {
        if (avpupupWindow != null && avpupupWindow.isShowing()) {
            avpupupWindow.dismiss();
        }
        time = 0;
        if (contact != null) {
            tv_phonenumber.setText(contact.getMobile());
        } else {
            tv_phonenumber.setText(mobile);
        }
        tv_phonetime.setText(CommonUtil.formatTimeBySecond(time));
        if (avFlag == 2) {
            findViewById(R.id.ll_video).setVisibility(View.VISIBLE);
            findViewById(R.id.rl_yuyin).setVisibility(View.GONE);
        } else {

            findViewById(R.id.rl_yuyin).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_video).setVisibility(View.GONE);
        }

        if (popupWindow != null) popupWindow.dismiss();
        if (oppopupWindow != null) oppopupWindow.dismiss();

        findViewById(R.id.ll_bottom).setVisibility(View.GONE);
//        findViewById(R.id.img_zhedang).setVisibility(
//                View.GONE);
        ll_number.setVisibility(View.GONE);
        ll_phone.setVisibility(View.VISIBLE);
        movieLayout.setFocusable(false);
        movieLayout.setEnabled(false);
        movieLayout.setVisibility(View.GONE);

        if (avFlag == 2) clearAudioCallView();
        else audioCallView();
    }

    private void audioCallView() {
        tv_phone_bigsmallscreen.setEnabled(false);
        tv_phone_bigsmallscreen.setFocusable(false);
        tv_phone_fullscreen.setEnabled(false);
        tv_phone_fullscreen.setFocusable(false);
        tv_phone_halfscreen.setEnabled(false);
        tv_phone_halfscreen.setFocusable(false);
        tv_phone_camera.setEnabled(false);
        tv_phone_camera.setFocusable(false);
    }

    private void clearAudioCallView() {
        tv_phone_bigsmallscreen.setEnabled(true);
        tv_phone_bigsmallscreen.setFocusable(true);
        tv_phone_fullscreen.setEnabled(true);
        tv_phone_fullscreen.setFocusable(true);
        tv_phone_halfscreen.setEnabled(true);
        tv_phone_halfscreen.setFocusable(true);
        tv_phone_camera.setEnabled(true);
        tv_phone_camera.setFocusable(true);
    }

    //电话挂断处理
    private void callOffView() {
        mobile = null;
        contact = null;
        time = 0;
        showTime = false;
        if (avpupupWindow != null && avpupupWindow.isShowing()) {
            avpupupWindow.dismiss();
        }
        findViewById(R.id.ll_video).setVisibility(View.GONE);
        findViewById(R.id.rl_yuyin).setVisibility(View.GONE);
//        findViewById(R.id.img_zhedang).setVisibility(
//                View.VISIBLE);
        findViewById(R.id.ll_bottom).setVisibility(View.VISIBLE);
        ll_number.setVisibility(View.VISIBLE);
        ll_phone.setVisibility(View.GONE);
        movieLayout.setVisibility(View.VISIBLE);
        movieLayout.setFocusable(true);
        movieLayout.setEnabled(true);
        bIsCamera = true;
        bIsAudioEnabled = true;
        isMianti = true;
        tv_phone_speaker.setBackgroundResource(R.drawable.bg_btn_speaker);
        tv_phone_mike.setBackgroundResource(R.drawable.bg_btn_mike);
        tv_phone_camera.setBackgroundResource(R.drawable.bg_btn_camera);
    }

    private void callOff() {
        if (contact == null || contact.getMobile() == null)
            rtcCallManager.stopCall(mobile);
        else
            rtcCallManager.stopCall(contact.getMobile());
        callOffView();
    }

    private Timer mTimer;

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 0, 5000);
    }

    @SuppressLint("HandlerLeak")
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    if (!NetUtil.isConnected(MainActivity.this)) {
                        isOnline = false;
                        setOnLine(false);
                        break;
                    }
                    isOnline = rtcCallManager.getRegisterStatus();
                    setOnLine(isOnline);
                    break;
                default:
                    break;
            }
        }
    };
}
