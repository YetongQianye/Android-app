package com.example.music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MusicBoxConstant {
    //music box status flag:
    //0x01:没有播放，0x02:正在播放，0x03:暂停
    public static final int IDLE = 0x01;
    public static final int PLAYING = 0x02;
    public static final int PAUSING = 0x03;

    public static String[] titles = new String[]{
        "倒数",
        "胡广生",
        "你的姑娘",
        "Without You"
    };

    public static String[] authors = new String[]{
            "邓紫棋",
            "任素汐",
            "隔壁老樊",
            "艾维奇"
    };

    public static final String ACTION_CTL = "com.example.ACTION_CTL";
    public static final String ACTION_UPDATE = "com.example.ACTION_UPDATE";

    public static final String TOKEN_CONTROL = "control";
    public static final String TOKEN_UPDATE = "update";
    public static final String TOKEN_CURRENT = "current";

    public static final int STOP = 2;
    public static final int PLAY_OR_PAUSE = 1;

    public static final String MUSIC_SERVICE = "com.example.MUSIC_SERVICE";


}
