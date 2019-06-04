package com.example.administrator.huarongdao;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    Button Qz[] = new Button[10];
    //棋盘布局
    int BG[][] = new int[5][4];
    ConstraintLayout mLayout;
    AbsoluteLayout absLayout;
    TextView txt1;
    TextView TextOfTime;
    //屏幕宽度
    float SW;
    float x1, x2, y1, y2;
    int Step=0;
    int mTime=0;
    //关卡等级
    int level=1;
    //声音开关
    boolean voiceSwitch=true;
    Intent musicIntent;
    Timer timer = new Timer();

//    //当音效播放完毕时，release掉
//    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
//        @Override
//        public void onCompletion(MediaPlayer mediaPlayer) {
//            // Now that the sound file has finished playing, release the media player resources.
//            if (mMediaPlayer != null) {
//                mMediaPlayer.release();
//                mMediaPlayer = null;
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Qz[0] = (Button) findViewById(R.id.Qz1);
        Qz[1] = (Button) findViewById(R.id.Qz2);
        Qz[2] = (Button) findViewById(R.id.Qz3);
        Qz[3] = (Button) findViewById(R.id.Qz4);
        Qz[4] = (Button) findViewById(R.id.Qz5);
        Qz[5] = (Button) findViewById(R.id.Qz6);
        Qz[6] = (Button) findViewById(R.id.Qz7);
        Qz[7] = (Button) findViewById(R.id.Qz8);
        Qz[8] = (Button) findViewById(R.id.Qz9);
        Qz[9] = (Button) findViewById(R.id.Qz10);

        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        txt1 = (TextView) findViewById(R.id.step);
        TextOfTime = (TextView) findViewById(R.id.time);

        absLayout = (AbsoluteLayout) findViewById(R.id.Qp);

        for (int i = 0; i < 10; i++)
            Qz[i].setOnTouchListener(new mTouch());

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++)
                BG[i][j] = 1;
        BG[4][1] = 0;
        BG[4][2] = 0;

        //计步
        boolean post = txt1.post(new Runnable() {
            @Override
            public void run() {
                txt1.setText("步数：0步");
                SW = mLayout.getWidth();
                init(level);
            }
        });

        //计时

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mTime++;
                TextOfTime.setText("时间：" + mTime + "秒");
            }
        },1000,1000);

        musicIntent = new Intent(MainActivity.this,MusicServer.class);
        if (voiceSwitch)
            startService(musicIntent);
//        manu();

    }

    protected void onStop(){
        Intent intent = new Intent(MainActivity.this,MusicServer.class);
        stopService(intent);
        super.onStop();
    }
    //选关
    public void selectLevel(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择关卡");
        final View v =  getLayoutInflater().inflate(R.layout.selectlevel,null);
        builder.setView(v);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void setLevel1(View view) {
        this.level = 1;
        init(level);
        mTime=0;
        Step=0;
        BG[4][1] = 0;
        BG[4][2] = 0;
        txt1.setText("步数：0步");
    }
    public void setLevel2(View view) {
        this.level = 2;
        init(level);
        mTime=0;
        Step=0;
        BG[4][1] = 0;
        BG[4][2] = 0;
        txt1.setText("步数：0步");

    }
    public void setLevel3(View view) {
        this.level = 3;
        init(level);
        mTime=0;
        Step=0;
        BG[4][1] = 0;
        BG[4][2] = 0;
        txt1.setText("步数：0步");
    }
    public void setLevel4(View view) {
        this.level = 4;
        BG[4][3] = 0;
        BG[4][2] = 0;
        init(level);
        mTime=0;
        Step=0;
        txt1.setText("步数：0步");
    }

    public void setLevel5(View view) {
        this.level = 5;
        BG[4][1] = 0;
        BG[4][2] = 0;
        init(level);
        mTime=0;
        Step=0;
        txt1.setText("步数：0步");
    }

    public void switchVoice(View view) {
        if (voiceSwitch==true) {
            voiceSwitch=false;
            stopService(musicIntent);
        }
        else {
            voiceSwitch=true;
            startService(musicIntent);
        }
    }

    public void screenShot(View view) {

        Activity activity=MainActivity.this;
        ScreenShot myShot=new ScreenShot();
        boolean statue = myShot.shoot(activity);
        if (statue==true){
            Toast.makeText(this,"截图成功，保存在相册",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"截图失败",Toast.LENGTH_SHORT).show();
        }
    }

    public void reSet(View view) {
        if (level==1) setLevel1(view);
        if (level==2) setLevel2(view);
        if (level==3) setLevel3(view);
        if (level==4) setLevel4(view);
        if (level==5) setLevel5(view);
    }



    public class mTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            int type; // 1 bing    2  zhangfei  3 guanyu 4 caocao
            int r, c;
            if (v.getWidth() == v.getHeight()) {
                if (v.getHeight() > 300)
                    type = 4;
                else
                    type = 1;

            } else {
                if (v.getHeight() > v.getWidth())
                    type = 2;
                else
                    type = 3;
            }

            r = (int) (v.getY() / 270f);
            c = (int) (v.getX() / 270f);
            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
                y1 = event.getY();
            }
            //上下左右移动
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                x2 = event.getX();
                y2 = event.getY();

                if (y1 - y2 > 30  &&  y1-y2>abs(x1-x2)) //"向上滑:"
                {

                    switch (type) {

                        case 1:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 3:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPois(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r + 1][c] = BG[r + 1][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;

                    }

                } else if (y2 - y1 > 30  &&y2-y1>abs(x1-x2)) //向下滑
                {
                    switch (type) {
                        case 1:
                            if (r < 4 && BG[r + 1][c] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 1][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (r < 3 && BG[r + 2][c] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 2][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }

                            break;
                        case 3:
                            if (r < 4 && BG[r + 1][c] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 1][c] = BG[r + 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (r < 3 && BG[r + 2][c] == 0 && BG[r + 2][c + 1] == 0) {
                                SetPois(v, r + 1, c);
                                BG[r + 2][c] = BG[r + 2][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                                if (r + 1 == 3 && c == 1) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("曹操成功逃脱");
                                    builder.setMessage("共用：        "+Step+"步        "+mTime+"秒");

                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            selectLevel(v);
                                        }
                                    });
                                    builder.show();
                                }
                            }
                            break;
                    }
                } else if (x1 - x2 > 30  &&x1-x2>abs(y1-y2)) //向左滑
                {
                    switch (type) {
                        case 1:
                            if (c > 0 && BG[r][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r + 1][c - 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 3:
                            if (c > 0 & BG[r][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPois(v, r, c - 1);
                                BG[r][c - 1] = BG[r + 1][c - 1] = 1;
                                BG[r][c + 1] = BG[r + 1][c + 1] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                                if (r + 1 == 3 && c == 1) {
                                    txt1.setText("你赢了！共用" + Step + "步");
                                }
                            }
                            break;
                    }
                } else if (x2 - x1 > 30  &&x2-x1>abs(y1-y2)) //向右滑
                {
                    switch (type) {
                        case 1:
                            if (c < 3 && BG[r][c + 1] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 2:
                            if (c < 3 & BG[r][c + 1] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r + 1][c + 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 3:
                            if (c < 2 && BG[r][c + 2] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 2] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                            }
                            break;
                        case 4:
                            if (c < 2 && BG[r][c + 2] == 0 && BG[r + 1][c + 2] == 0) {
                                SetPois(v, r, c + 1);
                                BG[r][c + 2] = BG[r + 1][c + 2] = 1;
                                BG[r][c] = BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("步数：" + Step + "步");
                                if (r + 1 == 3 && c == 1) {
                                    txt1.setText("你赢了！共用" + Step + "步");
                                }
                            }
                            break;
                    }
                }
                if (voiceSwitch==true){
                    mMediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.voice);
                    mMediaPlayer.start();
                    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                        }
                    };
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
            return true;
        }
    }


    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    void SetSize(Button v, int w, int h) {
        v.setWidth(w*(int)SW/4);//(w * dip2px(getApplicationContext(),SW/4));
        v.setHeight(h * dip2px(getApplicationContext(), SW / 4));
    }

    void SetPois(View v, int r, int c) {
        v.setX(c * SW / 4f);
        v.setY(r * SW / 4f);
    }

    void init(int level) {
        SetSize(Qz[0], 1, 1);
        SetSize(Qz[1], 1, 1);
        SetSize(Qz[2], 1, 1);
        SetSize(Qz[3], 1, 1);
        SetSize(Qz[4], 1, 2);
        SetSize(Qz[5], 1, 2);
        SetSize(Qz[6], 1, 2);
        SetSize(Qz[7], 1, 2);
        SetSize(Qz[8], 2, 1);
        SetSize(Qz[9], 2, 2);
        switch (level) {
            case 1:
                //一夫当关
                SetPois(Qz[0], 4, 0);
                SetPois(Qz[1], 3, 1);
                SetPois(Qz[2], 3, 2);
                SetPois(Qz[3], 4, 3);
                SetPois(Qz[4], 0, 0);
                SetPois(Qz[5], 0, 3);
                SetPois(Qz[6], 2, 0);
                SetPois(Qz[7], 2, 3);
                SetPois(Qz[8], 2, 1);
                SetPois(Qz[9], 0, 1);
                break;

            case 2:
                //第二关：横刀立马
                SetPois(Qz[0], 2, 0);
                SetPois(Qz[1], 3, 1);
                SetPois(Qz[2], 3, 2);
                SetPois(Qz[3], 2, 3);
                SetPois(Qz[4], 0, 0);
                SetPois(Qz[5], 0, 3);
                SetPois(Qz[6], 3, 0);
                SetPois(Qz[7], 3, 3);
                SetPois(Qz[8], 2, 1);
                SetPois(Qz[9], 0, 1);
                break;

            case 3:
                //第三关：齐头并进
                SetPois(Qz[0], 0, 0);
                SetPois(Qz[1], 0, 3);
                SetPois(Qz[2], 3, 1);
                SetPois(Qz[3], 3, 2);
                SetPois(Qz[4], 1, 0);
                SetPois(Qz[5], 1, 3);
                SetPois(Qz[6], 3, 0);
                SetPois(Qz[7], 3, 3);
                SetPois(Qz[8], 2, 1);
                SetPois(Qz[9], 0, 1);
                break;

            case 4:
                //第四关：兵分三路
                SetPois(Qz[0], 2, 2);
                SetPois(Qz[1], 2, 3);
                SetPois(Qz[2], 3, 2);
                SetPois(Qz[3], 3, 3);
                SetPois(Qz[4], 0, 2);
                SetPois(Qz[5], 0, 3);
                SetPois(Qz[6], 3, 0);
                SetPois(Qz[7], 3, 1);
                SetPois(Qz[8], 2, 0);
                SetPois(Qz[9], 0, 0);
                break;

            case 5:
                //成功案例
                SetPois(Qz[0], 4, 0);
                SetPois(Qz[1], 0, 1);
                SetPois(Qz[2], 0, 2);
                SetPois(Qz[3], 4, 3);
                SetPois(Qz[4], 0, 0);
                SetPois(Qz[5], 0, 3);
                SetPois(Qz[6], 2, 0);
                SetPois(Qz[7], 2, 3);
                SetPois(Qz[8], 1, 1);
                SetPois(Qz[9], 2, 1);
        }
    }
}
