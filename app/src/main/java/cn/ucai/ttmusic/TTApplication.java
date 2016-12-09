package cn.ucai.ttmusic;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import java.util.List;

import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.service.IMusicService;

public class TTApplication extends Application {

    static TTApplication application;

    IMusicService musicService;
    List<Music> musicList;

    public static TTApplication getInstance() {
        if (application == null) {
            application = new TTApplication();
        }
        return application;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public IMusicService getMusicService() {
        return musicService;
    }

    public void setMusicService(IMusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
