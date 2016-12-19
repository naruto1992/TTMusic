package cn.ucai.ttmusic;

import android.app.Application;
import android.content.Context;

import java.util.List;

import cn.ucai.ttmusic.db.Music;
import cn.ucai.ttmusic.service.IMusicService;

public class TTApplication extends Application {

    static TTApplication application;
    static Context context;

    IMusicService musicService;
    List<Music> musicList;

    public static TTApplication getInstance() {
        if (application == null) {
            application = new TTApplication();
        }
        return application;
    }

    public static Context getContext() {
        return context;
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
        context = this;
    }
}
