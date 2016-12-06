package cn.ucai.ttmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.service.IMusicService;

public class MusciReceiver extends BroadcastReceiver {

    TTApplication application;
    IMusicService musicService;

    @Override
    public void onReceive(Context context, Intent intent) {
        application = TTApplication.getInstance();
        musicService = application.getMusicService();

        String action = intent.getAction();
        if (musicService != null) {
            switch (action) {
                //播放
                case I.BroadCast.MUSIC_PLAY:
                    if (intent == null || intent.getExtras() == null) {
                        return;
                    }
                    Bundle data = intent.getExtras();
                    List<Music> list = (List<Music>) data.getSerializable(I.BroadCast.MUSIC_LIST);
                    if (musicService.getMusicList() != list) {
                        musicService.setMusicList(list);
                    }
                    //application.setMusicList(list);
                    int position = data.getInt(I.BroadCast.MUSIC_POSITION, 0);
                    musicService.pause();
                    musicService.playMusic(position);
                    break;
                //上一首
                case I.BroadCast.MUSIC_FRONT:
                    break;
                //下一首
                case I.BroadCast.MUSIC_NEXT:
                    break;
                //暂停
                case I.BroadCast.MUSIC_PAUSE:
                    break;
                //取消通知
                case I.BroadCast.NOTIFY_CANCEL:
                    break;
            }
        }
    }
}
