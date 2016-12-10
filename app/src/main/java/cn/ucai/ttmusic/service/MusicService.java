package cn.ucai.ttmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.activity.PlayActivity;
import cn.ucai.ttmusic.bean.LrcContent;
import cn.ucai.ttmusic.bean.Music;

public class MusicService extends Service {

    MediaPlayer mediaPlayer; // 音乐播放对象
    List<Music> songs; // 要播放的歌曲集合

    int currentItem = 0; //当前播放第几首歌
    int playMode = 0; // 播放模式,0为普通模式，1为单曲循环，2为随机播放
    int playState = 0; // 当前状态，0为初始化，1为播放，2为暂停
    int currentProgress = 0; //当前播放进度

    NotificationManager manger;
    RemoteViews remoteViews;

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 服务创建的时候新建播放器
        mediaPlayer = new MediaPlayer();
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public class ServiceBinder extends Binder implements IMusicService {

        //获取当前播放进度
        @Override
        public int getCurrentTime() {
            // 如果当前正在播放就返回当前进度
            if (mediaPlayer.isPlaying()) {
                return mediaPlayer.getCurrentPosition();
            } else {
                return currentProgress;
            }
        }

        @Override
        public void setCurrentTime(int progress) {
            currentProgress = progress;
        }

        //跳到指定进度
        @Override
        public void moveToProgress(int progress) {
            mediaPlayer.seekTo(progress);
            currentProgress = progress;
        }

        //播放歌曲
        @Override
        public void playMusic(int position) {
            play(songs, position);
        }

        //下一首
        @Override
        public void nextMusic() {
            playNextMusic();
        }

        //上一首
        @Override
        public void frontMusic() {
            switch (playMode) {
                case I.PlayMode.MODE_NORMAL:
                    if (--currentItem < 0) {
                        currentItem = songs.size() - 1;
                    }
                    play(songs, currentItem);
                    break;
                case I.PlayMode.MODE_SINGLE:
                    play(songs, currentItem);
                    break;
                case I.PlayMode.MODE_SHUFFLE:
                    play(songs, new Random().nextInt(songs.size()) + 1);
                    break;
            }
        }

        //是否正在播放
        @Override
        public boolean isPlay() {
            return mediaPlayer.isPlaying();
        }

        //暂停
        @Override
        public boolean pause() {
            mediaPlayer.pause();
            playState = I.PlayState.IS_PAUSE;
            initNotification();
            return true;
        }

        //播放
        @Override
        public void start() {
            mediaPlayer.start();
            playState = I.PlayState.IS_PLAY;
            initNotification();
        }

        //停止播放
        @Override
        public void stop() {
            mediaPlayer.stop();
            playState = I.PlayState.IS_INIT;
            initNotification();
        }

        //获取播放器对象
        @Override
        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }

        @Override
        public int getCurrentItemId() {
            return currentItem;
        }

        @Override
        public void setCurrentItemId(int currentMusicItem) {
            currentItem = currentMusicItem;
        }

        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        @Override
        public void setMusicList(List<Music> musicList) {
            songs = musicList;
        }

        @Override
        public List<Music> getMusicList() {
            if (songs == null) {
                songs = TTApplication.getInstance().getMusicList();
            }
            return songs;
        }

        @Override
        public Music getCurrentMusic() {
            return songs.get(currentItem);
        }

        @Override
        public int getState() {
            return playState;
        }

        @Override
        public void setState(int state) {
            playState = state;
        }

        @Override
        public void setPlayMode(int mode) {
            playMode = mode;
        }

        @Override
        public int getPlayMode() {
            return playMode;
        }

        @Override
        public List<LrcContent> getLrc() {
            return null;
        }

        @Override
        public void showLrc(int position) {
        }

        @Override
        public int getLrcIndex() {
            return 0;
        }

    }

    private void play(List<Music> musicList, int position) {
        try {
            // 重置mediaplay
            mediaPlayer.reset();
            // 设置播放资源
            mediaPlayer.setDataSource(musicList.get(position).getUrl().toString());
            // 准备播放
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
            // 设置为正在播放
            playState = 1;
            currentItem = position;
            // 显示歌词
            // showLrcs(position);
            // 设置播放完毕监听器
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    // 如果播放完毕就直接下一曲
                    playNextMusic();
                }
            });
            initNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playNextMusic() {
        switch (playMode) {
            case I.PlayMode.MODE_NORMAL:
                if (++currentItem >= songs.size()) {
                    currentItem = 0;
                }
                play(songs, currentItem);
                break;
            case I.PlayMode.MODE_SINGLE:
                play(songs, currentItem);
                break;
            case I.PlayMode.MODE_SHUFFLE:
                play(songs, new Random().nextInt(songs.size()) + 1);
                break;
        }
    }

    private void initNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("正在播放音乐");
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setShowWhen(false);
        Music music = songs.get(currentItem);
        if (Build.VERSION.SDK_INT >= 23) {
            remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_v23);
            setIntent(remoteViews);
            builder.setContent(remoteViews);
        } else {
            remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
            remoteViews.setTextViewText(R.id.ntf_music, music.getTitle());
            remoteViews.setTextViewText(R.id.ntf_singer, music.getSinger());
            setIntent(remoteViews);
            builder.setCustomBigContentView(remoteViews);
        }
        //点击跳转至播放界面
        Intent notificationIntent = new Intent(this, PlayActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = builder.setContentIntent(pi).build();
//        notification.bigContentView = remoteViews;

        manger.notify(I.Notification.NOTIFY_ID, notification);
    }

    private void setIntent(RemoteViews remoteViews){
        if (playState == I.PlayState.IS_PAUSE) {
            remoteViews.setImageViewResource(R.id.ntf_play, R.drawable.play_button);
        } else {
            remoteViews.setImageViewResource(R.id.ntf_play, R.drawable.pause_button);
        }
        remoteViews.setOnClickPendingIntent(R.id.ntf_play, playOrPause());
        remoteViews.setOnClickPendingIntent(R.id.ntf_front, getIntent(this, I.BroadCast.MUSIC_FRONT, 0, 0));
        remoteViews.setOnClickPendingIntent(R.id.ntf_next, getIntent(this, I.BroadCast.MUSIC_NEXT, 0, 0));
        remoteViews.setOnClickPendingIntent(R.id.ntf_cancel, getIntent(this, I.BroadCast.NOTIFY_CANCEL, 0, 0));
    }

    private PendingIntent playOrPause() {
        if (playState == I.PlayState.IS_PAUSE) {
            return getIntent(this, I.BroadCast.MUSIC_PLAY, 0, 0);
        } else {
            return getIntent(this, I.BroadCast.MUSIC_PAUSE, 0, 0);
        }
    }

    private PendingIntent getIntent(Context context, String action, int code, int flag) {
        Intent intent = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, code, intent, flag);
        return pi;
    }

}
