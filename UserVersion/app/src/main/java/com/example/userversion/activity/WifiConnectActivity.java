package com.example.userversion.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userversion.PublicDefine;
import com.example.userversion.R;
import com.example.userversion.Util.SocketUtil;
import com.example.userversion.Util.Util;
import com.example.userversion.Util.WifiUtil;
import com.example.userversion.bean.Package;

import java.util.Arrays;

/**
 * Created by XJP on 2017/11/14.
 */
public class WifiConnectActivity extends AppCompatActivity implements SocketUtil.SocketImp {


    //二维码显示相关
    TextView tv_qrcode;

    //wifi显示相关
    TextView tv_status;
    ImageView img_wifi;

    //Socket显示相关
    LinearLayout ll_socket;
    TextView tv_socket;
    TextView tv_send;
    TextView tv_receive;

    //WIFI工具类
    WifiUtil wifiUtil;
    //wifi连接工作线程
    HandlerThread handlerThread;
    Handler handler;
    Handler mMainHanler;
    public final int MSG_CONNECT = 0;
    public final int MSG_DELAYSHOW = 1;
    //广播接收
    WifiReceiver wifiReceiver;

    //Socket工具类
    SocketUtil socketUtil;

    //最下面的重试按钮
    Button btn_retry;

    //wifi 状态信息
    int wifi_close = R.string.wifi_close;
    int wifi_opening = R.string.wifi_opening;
    int wifi_already_open = R.string.wifi_already_open;
    int wifi_disconnecting = R.string.wifi_disconnecting;
    int wifi_open_not_connect = R.string.wifi_open_not_connected;
    int wifi_connected_wrong = R.string.wifi_connected_wrong;
    int wifi_connected_right = R.string.wifi_connected_right;
    int wifi_connected = R.string.wifi_connected;
    int wifi_connecting = R.string.wifi_connecting;
    Package pg;
    int STEP = 1;
    int EmailCount = 1;
    int EmailSTEP = 1;

    public final String TAG = this.getClass().getSimpleName();
    public final String WIFI_STATE_CHANGE_ACTION = WifiManager.WIFI_STATE_CHANGED_ACTION;
    public final String NETWORK_STATE_CHANGED_ACTION = WifiManager.NETWORK_STATE_CHANGED_ACTION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initView();

    }

    public void connect() {
        wifiUtil.connect(wifiUtil.createWifiInfo(PublicDefine.SSID, PublicDefine.PASSWORD, PublicDefine.TYPE));
    }

    public void initView() {
        pg = getPackage();
        EmailCount = Util.getPgEmailSize(pg);
        img_wifi = (ImageView) findViewById(R.id.img_wifi);
        tv_status = (TextView) findViewById(R.id.tv_status);
        ll_socket = (LinearLayout) findViewById(R.id.ll_socket);
        btn_retry = (Button) findViewById(R.id.btn_retry);
        wifiReceiver = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WIFI_STATE_CHANGE_ACTION);
        intentFilter.addAction(NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, intentFilter);
        wifiUtil = new WifiUtil(WifiConnectActivity.this);
        handlerThread = new HandlerThread("connect");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_CONNECT:
                        connect();
                        break;

                }
            }
        };
        mMainHanler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_DELAYSHOW:
                        btn_retry.setAlpha(1.0f);
                        btn_retry.setEnabled(true);
                        break;
                }
            }
        };

        socketUtil = new SocketUtil(this, this);

        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_receive = (TextView) findViewById(R.id.tv_receive);
        tv_socket = (TextView) findViewById(R.id.tv_socket);
        tv_qrcode = (TextView) findViewById(R.id.tv_qrcode);
        String code = pg.getQrCode();
        if (code != null) {
            tv_qrcode.setText(code);
        }


        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没有连接上目标wifi,请尝试重连
                if (!TextUtils.equals("\"brize_box\"", wifiUtil.getSSID())) {
                    wifiUtil.removeNowConnectingID();
                    handler.sendMessage(handler.obtainMessage(MSG_CONNECT));
                } else {
                    //连上了，则建立连接
                    btn_retry.setEnabled(false);
                    btn_retry.setAlpha(0.4f);
                    mMainHanler.removeMessages(MSG_DELAYSHOW);
                    mMainHanler.sendMessageDelayed(handler.obtainMessage(MSG_DELAYSHOW), 5000);
                    socketUtil.connect();
                }
            }
        });

    }

    void sendEmail1() {
        String s = pg.getQrCode() + pg.getEmail1();
        socketUtil.send(s, 0x01);
        byte[] b = SocketUtil.getByte(s, 0x01);
        tv_send.append("邮箱1: " + byteToString(b) + "\n");
    }

    void sendEmail2() {
        String s = pg.getQrCode() + pg.getEmail2();
        socketUtil.send(s, 9);
        byte[] b = SocketUtil.getByte(s, 9);
        tv_send.append("邮箱2: " + byteToString(b) + "\n");
    }

    void sendEmail3() {
        String s = pg.getQrCode() + pg.getEmail3();
        socketUtil.send(s, 10);
        byte[] b = SocketUtil.getByte(s, 10);
        tv_send.append("邮箱3: " + byteToString(b) + "\n");
    }

    void sendEmail4() {
        String s = pg.getQrCode() + pg.getEmail4();
        socketUtil.send(s, 11);
        byte[] b = SocketUtil.getByte(s, 11);
        tv_send.append("邮箱4: " + byteToString(b) + "\n");
    }

    void sendEmail5() {
        String s = pg.getQrCode() + pg.getEmail5();
        socketUtil.send(s, 12);
        byte[] b = SocketUtil.getByte(s, 12);
        tv_send.append("邮箱5: " + byteToString(b) + "\n");
    }

    void sendEmail6() {
        String s = pg.getQrCode() + pg.getEmail6();
        socketUtil.send(s, 13);
        byte[] b = SocketUtil.getByte(s, 13);
        tv_send.append("邮箱6: " + byteToString(b) + "\n");
    }

    void sendEmail7() {
        String s = pg.getQrCode() + pg.getEmail7();
        socketUtil.send(s, 14);
        byte[] b = SocketUtil.getByte(s, 14);
        tv_send.append("邮箱7: " + byteToString(b) + "\n");
    }

    void sendEmail8() {
        String s = pg.getQrCode() + pg.getEmail8();
        socketUtil.send(s, 15);
        byte[] b = SocketUtil.getByte(s, 15);
        tv_send.append("邮箱8: " + byteToString(b) + "\n");
    }

    void sendEmail9() {
        String s = pg.getQrCode() + pg.getEmail9();
        socketUtil.send(s, 16);
        byte[] b = SocketUtil.getByte(s, 16);
        tv_send.append("邮箱9: " + byteToString(b) + "\n");
    }

    void sendEmail10() {
        String s = pg.getQrCode() + pg.getEmail10();
        socketUtil.send(s, 0x17);
        byte[] b = SocketUtil.getByte(s, 0x17);
        tv_send.append("邮箱10: " + byteToString(b) + "\n");
    }


    void sendSecond() {
        String s = pg.getQrCode() + pg.getWifiId();
        socketUtil.send(s, 0x02);
        byte[] b = SocketUtil.getByte(s, 0x02);
        tv_send.append("WIFI名称：" + byteToString(b) + "\n");
    }


    void sendThree() {
        String s = pg.getQrCode() + pg.getWifiPsw();
        socketUtil.send(s, 0x03);
        byte[] b = SocketUtil.getByte(s, 0x03);
        tv_send.append("WIFI密码：" + byteToString(b) + "\n");
    }

    void sendFour() {
        String s = pg.getQrCode() + pg.getDistance();
        socketUtil.send(s, 0x04);
        byte[] b = SocketUtil.getByte(s, 0x04);
        tv_send.append("距离：" + byteToString(b) + "\n");
    }

    void sendFive() {
        String s = pg.getQrCode() + pg.getEmailTitle();
        socketUtil.send(s, 0x05);
        byte[] b = SocketUtil.getByte(s, 0x05);
        tv_send.append("标题：" + byteToString(b) + "\n");
    }

    void sendSix() {
        String s = pg.getQrCode() + pg.getEmailContent();
        socketUtil.send(s, 0x06);
        byte[] b = SocketUtil.getByte(s, 0x01);
        tv_send.append("内容：" + byteToString(b) + "\n");
    }


    byte[] getTrueReplyByte(String s, int commond) {
        int length = s.length() + 9;
        Log.d(TAG, "getTrueReplyByte: " + length);
        byte[] trueByte = new byte[length];
        trueByte[0] = (byte) 0xFF;
        trueByte[1] = (byte) 0xAA;
        trueByte[2] = (byte) (length - 3);
        trueByte[3] = (byte) commond;

        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            trueByte[4 + i] = (byte) c[i];
        }
        trueByte[s.length() + 4] = (byte) 0x4F;
        trueByte[s.length() + 5] = (byte) 0x4B;
        byte sum = 0;
        for (int i = 0; i < s.length() + 6; i++) {
            sum += trueByte[i];
        }
        trueByte[s.length() + 6] = (byte) sum;
        trueByte[s.length() + 7] = (byte) 0xFF;
        trueByte[s.length() + 8] = (byte) 0x55;
        return trueByte;
    }

    void send() {
        SharedPreferences sharedPreference = getSharedPreferences(PublicDefine.SP_INFO, MODE_PRIVATE);
        String email = sharedPreference.getString("email", "");
        String code = getIntent().getStringExtra("qr_code");
        String s = code + "000000" + email;
        socketUtil.send(s, 0x01);
        byte[] b = SocketUtil.getByte(s, 1);
        tv_send.setText("");
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hex = hex.toUpperCase();
            tv_send.append(hex + " ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendMessage(handler.obtainMessage(MSG_CONNECT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
        socketUtil.setDone();
    }

    @Override
    public void onSuccess() {
        tv_socket.setText(R.string.connection_success);
        btn_retry.setEnabled(false);
        btn_retry.setAlpha(0.4f);
        mMainHanler.removeMessages(MSG_DELAYSHOW);
        mMainHanler.sendMessageDelayed(handler.obtainMessage(MSG_DELAYSHOW), 5000);
        STEP = 1;
        EmailSTEP=1;
        tv_receive.setText("");
        tv_send.setText("");
        sendEmail1();
    }

    @Override
    public void onTimeout() {
        tv_socket.setText(R.string.connection_timed_out);
    }

    @Override
    public void onFail() {
        tv_socket.setText(R.string.connection_failed);
    }

    @Override
    public void onResult(byte[] b) {
        tv_receive.append(byteToString(b) + "\n");
        switch (STEP) {
            case 1:
                    if(EmailSTEP==1){
                        checkHelper(b,0x01);
                    }else{
                        checkHelper(b,EmailSTEP+7);
                    }
                break;
            case 2:
                if (checkReply2(b)) {
                    STEP = 3;
                    sendThree();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.wifi_name_not, Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (checkReply3(b)) {
                    STEP = 4;
                    sendFour();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.wifi_not_psw, Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (checkReply4(b)) {
                    STEP = 5;
                    sendFive();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.distance_not, Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                if (checkReply5(b)) {
                    sendSix();
                    STEP = 6;
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.fail_in_email_title, Toast.LENGTH_SHORT).show();
                }
                break;
            case 6:
                if (checkReply6(b)) {
                  //  Toast.makeText(WifiConnectActivity.this, "全部通过", Toast.LENGTH_SHORT).show();
                    pg.setDelivery(true);
                    pg.updateAll("time = ?", pg.getTime() + "");
                    Intent intent = new Intent(WifiConnectActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(WifiConnectActivity.this, R.string.fail_in_email_content, Toast.LENGTH_SHORT).show();
                }
                STEP = 1;
                break;
        }
    }

    public String byteToString(byte[] b) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hex = hex.toUpperCase();
            s.append(hex + " ");
        }
        return s.toString();
    }

    boolean checkReply_Email(byte[] b, int cmd) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, cmd);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    void checkHelper(byte[] b, int cmd) {
        if (checkReply_Email(b, cmd)) {
            if (EmailSTEP == EmailCount) {
                STEP = 2;
                sendSecond();
            } else {
                EmailSTEP++;
                switch (EmailSTEP) {
                    case 1:
                        sendEmail1();
                        break;
                    case 2:
                        sendEmail2();
                        break;
                    case 3:
                        sendEmail3();
                        break;
                    case 4:
                        sendEmail4();
                        break;
                    case 5:
                        sendEmail5();
                        break;
                    case 6:
                        sendEmail6();
                        break;
                    case 7:
                        sendEmail7();
                        break;
                    case 8:
                        sendEmail8();
                        break;
                    case 9:
                        sendEmail9();
                        break;
                    case 10:
                        sendEmail10();
                        break;
                }
            }
        } else {
            Toast.makeText(WifiConnectActivity.this, R.string.fail_in_email, Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkReply2(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x02);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReply3(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x03);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReply4(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x04);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }


    boolean checkReply5(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x05);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReply6(byte[] b) {
        String s = pg.getQrCode();
        byte[] rightByte = getTrueReplyByte(s, 0x06);
        if (Arrays.equals(rightByte, b)) {
            return true;
        } else {
            return false;
        }
    }


    boolean checkReply(byte[] b) {
        int length = 23;
        if (b.length < 23) {
            return false;
        }
        byte[] trueByte = new byte[length];
        trueByte[0] = (byte) 0xFF;
        trueByte[1] = (byte) 0xAA;
        trueByte[2] = (byte) (length - 3);
        trueByte[3] = (byte) 0x08;
        String s = getIntent().getStringExtra("qr_code") + "000000";
        char[] c = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            trueByte[4 + i] = (byte) c[i];
        }
        trueByte[s.length() + 4] = (byte) 0x4F;
        trueByte[s.length() + 5] = (byte) 0x4B;
        byte sum = 0;
        for (int i = 0; i < 20; i++) {
            sum += trueByte[i];
        }
        trueByte[s.length() + 6] = (byte) sum;
        trueByte[s.length() + 7] = (byte) 0xFF;
        trueByte[s.length() + 8] = (byte) 0x55;
        for (int i = 0; i < 23; i++) {
            if (b[i] != trueByte[i]) {
                Log.d(TAG, "checkReply: b" + b[i] + " t " + trueByte[i]);
                return false;
            }
        }
        return true;
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tv_socket.setText("");
            switch (intent.getAction()) {

                case WIFI_STATE_CHANGE_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                    if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                        Log.i(TAG, "正在关闭");
                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
                        Log.i(TAG, "正在打开");
                        tv_status.setText(wifi_opening);
                        img_wifi.setImageLevel(1);
                    } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                        tv_status.setText(wifi_close);
                        img_wifi.setImageLevel(0);
                        Log.i(TAG, "已经关闭");
                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                        Log.i(TAG, "已经打开");
                        tv_status.setText(wifi_already_open);
                        img_wifi.setImageLevel(1);
                    } else {
                        Log.i(TAG, "未知状态");
                    }
                    break;
                case NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info == null) {
                        return;
                    }
                    NetworkInfo.State state = info.getState();
                    //CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN
                    switch (state) {
                        case DISCONNECTING:
                            tv_status.setText(wifi_disconnecting);
                            Log.i(TAG, "onReceive: 正在断开");
                            break;
                        case DISCONNECTED:
                            if (wifiUtil.isOpen()) {
                                tv_status.setText(wifi_open_not_connect);
                                Log.i(TAG, "onReceive: 已经打开,未连接");
                            }
                            break;
                        case CONNECTING:
                            tv_status.setText(wifi_connecting);
                            Log.i(TAG, "onReceive: 正在连接");
                            break;
                        case CONNECTED:
                            tv_status.setText(wifi_connected);
                            Log.i(TAG, "onReceive: " + wifiUtil.getSSID());
                            Log.i(TAG, "onReceive: 已连接");
                            if (TextUtils.equals("\"brize_box\"", wifiUtil.getSSID())) {
                                tv_status.setText(wifi_connected_right);
                                tv_status.append(wifiUtil.getSSID());
                                img_wifi.setImageLevel(2);
                                ll_socket.setVisibility(View.VISIBLE);
                                socketUtil.connect();
                            } else {
                                tv_status.setText(wifi_connected_wrong);
                                tv_status.append(wifiUtil.getSSID());
                                img_wifi.setImageLevel(1);
                                //wifiUtil.removeNowConnectingID();
                            }
                            Log.i("xjp", "onReceive: " + wifiUtil.getSSID());
                            break;
                        default:
                            break;
                    }

                default:
                    break;

            }
        }
    }

    public static void startTrans(Context context, Package pg) {
        Intent intent = new Intent(context, WifiConnectActivity.class);
        intent.putExtra("pg_data", pg);
        context.startActivity(intent);
    }

    public Package getPackage() {
        Package pg = (Package) getIntent().getSerializableExtra("pg_data");
        return pg;
    }

}
