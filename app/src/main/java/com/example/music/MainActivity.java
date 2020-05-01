package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private TextView author;
    private ImageButton play;
    private ImageButton stop;

    //定义一个广播变量
    private ActivityReceiver filter;
    private Intent actionIntent;
    private int status = MusicBoxConstant.IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建一个界面元素变量
        title = (TextView) this.findViewById(R.id.title);
        author = (TextView) this.findViewById(R.id.author);
        play = (ImageButton) this.findViewById(R.id.play);
        stop = (ImageButton) this.findViewById(R.id.stop);

        //需要创建一个Service
        Intent actionIntent = new Intent(this,MusicService.class);
        this.startService(actionIntent);

        //创建Broadcast Receiver
        filter = new ActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicBoxConstant.ACTION_UPDATE);
        this.registerReceiver(filter,intentFilter);

        MusicListener musicListener = new MusicListener();
        play.setOnClickListener(musicListener);
        stop.setOnClickListener(musicListener);

    }

    //用于监听Service传回来的广播
    public class ActivityReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent.getIntExtra("update",-1);
            int current = intent.getIntExtra("current",-1);
            if(current>=0){
                title.setText(MusicBoxConstant.titles[current]);
                author.setText(MusicBoxConstant.authors[current]);
            }

            switch (update){
                case MusicBoxConstant.IDLE:
                    play.setImageResource(R.drawable.play);
                    status = MusicBoxConstant.IDLE;
                    stopService(actionIntent);
                    break;
                case MusicBoxConstant.PLAYING:
                    play.setImageResource(R.drawable.pause);
                    status = MusicBoxConstant.PAUSING;
                    break;
                case  MusicBoxConstant.PAUSING:
                    play.setImageResource(R.drawable.play);
                    status = MusicBoxConstant.PAUSING;
                    break;
            }

        }
    }

    //播放，暂停等按钮点击事件
    class MusicListener implements View.OnClickListener{
        @Override
        public void onClick(View source) {
            Intent intent = new Intent(MusicBoxConstant.ACTION_CTL);
            switch (source.getId()){
                case R.id.play:
                    intent.putExtra("control",1);
                    break;
                case R.id.stop:
                    intent.putExtra("control",2);
                    break;
            }
            //把点击状态Service类里面的广播去处理我们的播放事件
            MainActivity.this.sendBroadcast(intent);

        }
    }
}
