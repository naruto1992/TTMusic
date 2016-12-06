package cn.ucai.ttmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;
import java.util.Random;

import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.bean.LrcContent;
import cn.ucai.ttmusic.bean.Music;

public class MusicService extends Service {

    MediaPlayer mediaPlayer; // 音乐播放对象
    List<Music> songs; // 要播放的歌曲集合

    int currentItem = 0; //当前播放第几首歌
    int playMode = 0; // 播放模式,0为普通模式，1为单曲循环，2为随机播放
    int playState = 0; // 当前状态，0为初始化，1为播放，2为暂停
    int currentProgress = 0; //当前播放进度

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 服务创建的时候新建播放器
        mediaPlayer = new MediaPlayer();
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
            return true;
        }

        //播放
        @Override
        public void start() {
            mediaPlayer.start();
            playState = I.PlayState.IS_PLAY;
        }

        //停止播放
        @Override
        public void stop() {
            mediaPlayer.stop();
            playState = I.PlayState.IS_INIT;
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

}
