package cn.ucai.ttmusic.controller.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.controller.interfaze.IMusicService;

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
                case I.BroadCast.MUSIC_INIT:
                    if (intent == null || intent.getExtras() == null) {
                        return;
                    }
                    Bundle data = intent.getExtras();
                    List<Music> list = (List<Music>) data.getSerializable(I.BroadCast.MUSIC_LIST);
                    if (musicService.getMusicList() != list) {
                        musicService.setMusicList(list);
                    }
                    int position = data.getInt(I.BroadCast.MUSIC_POSITION, 0);
                    musicService.setCurrentItemId(position);
//                    if (musicService.isPlay()) {
//                        musicService.pause();
//                    }
                    musicService.playMusic(position);
                    break;
                //播放
                case I.BroadCast.MUSIC_PLAY:
                    musicService.start();
                    break;
                //上一首
                case I.BroadCast.MUSIC_FRONT:
                    musicService.frontMusic();
                    break;
                //下一首
                case I.BroadCast.MUSIC_NEXT:
                    musicService.nextMusic();
                    break;
                //暂停
                case I.BroadCast.MUSIC_PAUSE:
                    musicService.pause();
                    break;
                //取消通知
                case I.BroadCast.NOTIFY_CANCEL:
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(I.Notification.NOTIFY_ID);
                    break;
            }
        }
    }

}
