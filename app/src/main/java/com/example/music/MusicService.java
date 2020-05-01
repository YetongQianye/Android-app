package com.example.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    private MyReceiver serviceReceiver;
    private MediaPlayer mediaPlayer;

    private int status = MusicBoxConstant.IDLE;

    private AssetManager assetManager;
    private String[] music = new String[]{"daoshu.mp3","hgs.mp3","ndgn.mp3","Without-You.mp3"};
    private int current = 0;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        assetManager = this.getAssets();
        serviceReceiver = new MyReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicBoxConstant.ACTION_CTL);
        registerReceiver(serviceReceiver,filter);

        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){
        //当我们这个音乐播放器播放接收的时候会执行这个方法
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                current++;
                if (current > 3){
                    current = 0;
                }
                Intent sendIntent = new Intent(MusicBoxConstant.ACTION_UPDATE);
                sendIntent.putExtra(MusicBoxConstant.TOKEN_CURRENT,current);
                sendBroadcast(sendIntent);
                prepareAndPlay(music[current]);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    public class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //control=1播放，2=暂停
            int control = intent.getIntExtra("control",-1);
            switch (control){
                case MusicBoxConstant.PLAY_OR_PAUSE:
                    if(status == MusicBoxConstant.IDLE){
                        //播放音乐，切传送更新UI的广播
                        prepareAndPlay(music[current]);
                        status = MusicBoxConstant.PLAYING;
                    }else if (status == MusicBoxConstant.PLAYING){
                        mediaPlayer.pause();
                        status = MusicBoxConstant.PAUSING;
                    }else if (status == MusicBoxConstant.PAUSING){
                        mediaPlayer.start();
                        status = MusicBoxConstant.PLAYING;
                    }
                    break;
                case MusicBoxConstant.STOP:
                    if (status == MusicBoxConstant.PLAYING ||status == MusicBoxConstant.PAUSING){
                        mediaPlayer.stop();
                        status = MusicBoxConstant.IDLE;
                    }
            }

            Intent sendIntent = new Intent(MusicBoxConstant.ACTION_UPDATE);
            sendIntent.putExtra(MusicBoxConstant.TOKEN_UPDATE,status);
            sendIntent.putExtra(MusicBoxConstant.TOKEN_CURRENT,current);
            MusicService.this.sendBroadcast(sendIntent);
        }
    }

    //实现播放类
    private void prepareAndPlay(String music){
        try {
            AssetFileDescriptor afd = assetManager.openFd(music);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (Exception ioe){
            ioe.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}
