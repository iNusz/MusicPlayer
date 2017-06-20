package com.seunghoshin.android.musicplayer_2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by SeungHoShin on 2017. 6. 19..
 */

public class Player {

    public static final int STOP = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static MediaPlayer player = null;
    public static int playerStatus = STOP;


    public static void play(Uri musicUri, Context context) {
        // 1. 미디어 플레이어 사용하기
        //Uri musicUri = datas.get(position).musicUri;

        // 음악재생의 중복 막아주기
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(context, musicUri);

        // 2. 설정
        player.setLooping(false); //반복여부

        // 3. 시작
        player.start();

        playerStatus = PLAY;

    }

    public static void pause() {
        player.pause();
        playerStatus = PAUSE;
    }

    public static void replay(){
        player.start();
        playerStatus = PLAY;
    }

}
