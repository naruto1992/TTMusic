package cn.ucai.ttmusic.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.List;

import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.activity.PlayActivity;
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
                    int position = data.getInt(I.BroadCast.MUSIC_POSITION, 0);
                    musicService.pause();
                    musicService.playMusic(position);
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
                    notificationManager.cancel(999);
                    break;
            }
            initNotification(context, action);
        }
    }

    private void initNotification(Context context, String action) {
        NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("正在播放音乐");
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setShowWhen(false);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.ntf_music, "Notification_music");
        remoteViews.setTextViewText(R.id.ntf_singer, "Notification_singer");
        switch (action) {
            case I.BroadCast.MUSIC_PLAY:
            case I.BroadCast.MUSIC_FRONT:
            case I.BroadCast.MUSIC_NEXT:
                remoteViews.setImageViewResource(R.id.ntf_play, R.drawable.pause_button);
                break;
            case I.BroadCast.MUSIC_PAUSE:
                remoteViews.setImageViewResource(R.id.ntf_play, R.drawable.play_button);
                break;
            case I.BroadCast.NOTIFY_CANCEL:
                break;
        }

        remoteViews.setOnClickPendingIntent(R.id.ntf_play, sendBroadcast(context, I.BroadCast.MUSIC_PLAY, 0, 0));
        remoteViews.setOnClickPendingIntent(R.id.ntf_front, sendBroadcast(context, I.BroadCast.MUSIC_FRONT, 0, 0));
        remoteViews.setOnClickPendingIntent(R.id.ntf_next, sendBroadcast(context, I.BroadCast.MUSIC_NEXT, 0, 0));
        remoteViews.setOnClickPendingIntent(R.id.ntf_cancel, sendBroadcast(context, I.BroadCast.NOTIFY_CANCEL, 0, 0));

        //点击跳转至播放界面
        Intent notificationIntent = new Intent(context, PlayActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        builder.setCustomBigContentView(remoteViews);
        Notification notification = builder.setContentIntent(pi).build();
        manger.notify(999, notification);
    }

    private PendingIntent sendBroadcast(Context context, String action, int code, int flag) {
        Intent front = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, code, front, flag);
        return pi;
    }
}
