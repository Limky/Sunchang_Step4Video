package com.example.sqisoft.step4video.Activity.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.sqisoft.step4video.Activity.Manager.DataManager;
import com.example.sqisoft.step4video.Activity.Socket.TCPServer;
import com.example.sqisoft.step4video.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.example.sqisoft.step4video.R.id.view;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    private Animation animFadein, animFadeout;

     int duration ;
     int colorFrom;
     int colorTo;

    ImageView imageView;
     TransitionDrawable transition;
     Uri video;
   // TextView pathTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataManager.getInstance().setActivity(this);

        /**
         * 영상을 출력하기 위한 비디오뷰
         * SurfaceView를 상속받아 만든 클래스
         * 웬만하면 VideoView는 그때 그때 생성해서 추가 후 사용
         * 화면 전환 시 여러 UI가 있을 때 화면에 제일 먼저 그려져서 보기에 좋지 않을 때가 있다
         * 예제에서 xml에 추가해서 해봄
         */

        //레이아웃 위젯 findViewById
        videoView = (VideoView) findViewById(view);
        imageView = (ImageView) findViewById(R.id.textView);
       // pathTextView = (TextView) findViewById(R.id.path);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout);

        String localIP = getLocalIpAddress();
        String WiFiIp = getWiFiIpAddress();

        System.out.println( "+++++++++ IP Address : "+localIP+"\n+++++++++ WIFI Address : "+WiFiIp);

        Thread desktopServerThread = new Thread(new TCPServer(WiFiIp));
        desktopServerThread.start();

    }


    private void changeVideo(int seleted){

        switch (seleted){

            case  1 :
                video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bmw);

                videoView.setVideoURI(video);
                videoView.requestFocus();
                break;

            case  2 :
                 video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.benz);

                videoView.setVideoURI(video);
                videoView.requestFocus();
                break;

            case  3 :
                video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.genesis);

                videoView.setVideoURI(video);
                videoView.requestFocus();
                break;

            case  4 :
                video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.landrover);

                videoView.setVideoURI(video);
                videoView.requestFocus();
                break;


        }

        int checkExistence = getApplicationContext().getResources().getIdentifier("my_resource_name", "drawable", getApplicationContext().getPackageName());

      //  pathTextView.setText(video.toString());
    }

    private void fadeoutEffect(){
        duration = 1000;
        colorFrom = Color.parseColor("#10000000");
        colorTo = Color.parseColor("#000000");
        ColorDrawable[] color = {new ColorDrawable(colorFrom), new ColorDrawable(colorTo)};
        transition = new TransitionDrawable(color);
        videoView.setBackground(transition);
        transition.startTransition(duration);
    }



    private void play(){
        if(videoView.isPlaying()) {
            videoView.pause();
            imageView.setBackgroundColor(Color.BLACK);
            imageView.startAnimation(animFadeout);
            changeVideo(seleted);
            videoView.start();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                }
            }, 1500);

        }else {
            imageView.startAnimation(animFadeout);
            changeVideo(seleted);
            videoView.start();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                }
            }, 1500);
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageView.setBackgroundColor(Color.BLACK);
                seleted++;
                if(seleted == 5)seleted = 1;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        play(); //재귀호출~
                    }
                }, 1500);

            }
        });

    }

    private void playloof(){
        Log.w("********** playloof ","seleted = "+seleted);

            imageView.startAnimation(animFadeout);
            changeVideo(seleted);
            videoView.start();

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                }
            }, 1500);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageView.setBackgroundColor(Color.BLACK);
                seleted++;
                if(seleted == 5)seleted = 1;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        playloof(); //재귀호출~
                    }
                }, 1500);

            }
        });

    }

    int seleted;
    boolean loof;
    public void playVideo(String str){
     seleted = Integer.parseInt(str);

        Log.w("********** playVideo","");
        if(seleted == 0){
            seleted = 1;
            this.runOnUiThread(new Runnable() {
                public void run() {
               //    loof = true;
                    playloof();
                }});
        }else{
         //   loof = false;
            this.runOnUiThread(new Runnable() {
                public void run() {
                    play();
                }});

        }


    }



    public String getWiFiIpAddress(){
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;
    }


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());

                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

}



//
//
//    findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        if(videoView.isPlaying()) {
//
//        new Handler().postDelayed(new Runnable()
//        {
//@Override
//public void run()
//        {
//        imageView.startAnimation(animFadeout);
//        videoView.pause();
//        changeVideo(1);
//        videoView.start();
//        }
//        }, 1000);// 0.1초 정도 딜레이를 준 후 시작
//
//        }else {
//        imageView.startAnimation(animFadeout);
//        changeVideo(1);
//        videoView.start();
//        new Handler().postDelayed(new Runnable()
//        {
//@Override
//public void run()
//        {
//        imageView.setBackgroundColor(Color.TRANSPARENT);
//        }
//        }, 1500);
//        }
//
//        Toast.makeText(MainActivity.this,
//        "동영상 1 재생.", Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        if(videoView.isPlaying()) {
//        // videoView.setAnimation(animFadein);
//
//        fadeoutEffect();
//        new Handler().postDelayed(new Runnable()
//        {
//@Override
//public void run()
//        {
//        //여기에 딜레이 후 시작할 작업들을 입력
//        videoView.pause();
//        changeVideo(2);
//        videoView.start();
//        transition.resetTransition();
//        }
//        }, 1000);// 0.1초 정도 딜레이를 준 후 시작
//        }else{
//        changeVideo(2);
//        videoView.start();
//        }
//        Toast.makeText(MainActivity.this,
//        "동영상 2 재생.", Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        if(videoView.isPlaying()) {
//        // videoView.setAnimation(animFadein);
//        fadeoutEffect();
//        new Handler().postDelayed(new Runnable()
//        {
//@Override
//public void run()
//        {
//        //여기에 딜레이 후 시작할 작업들을 입력
//        videoView.pause();
//        changeVideo(3);
//        videoView.start();
//        transition.resetTransition();
//        }
//        }, 1000);// 0.1초 정도 딜레이를 준 후 시작
//        }else {
//        changeVideo(3);
//        videoView.start();
//        }
//
//        Toast.makeText(MainActivity.this,
//        "동영상 3 재생.", Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        if(videoView.isPlaying()) {
//        // videoView.setAnimation(animFadein);
//        fadeoutEffect();
//        new Handler().postDelayed(new Runnable()
//        {
//@Override
//public void run()
//        {
//        //여기에 딜레이 후 시작할 작업들을 입력
//        videoView.pause();
//        changeVideo(4);
//        videoView.start();
//        transition.resetTransition();
//        }
//        }, 1000);// 0.1초 정도 딜레이를 준 후 시작
//        }else {
//        changeVideo(4);
//        videoView.start();
//        }
//
//        Toast.makeText(MainActivity.this,
//        "동영상 4 재생.", Toast.LENGTH_SHORT).show();
//        }
//        });
