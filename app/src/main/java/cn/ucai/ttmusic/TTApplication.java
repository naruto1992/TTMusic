package cn.ucai.ttmusic;

import android.app.Application;
import android.content.Context;

import java.util.List;

import cn.ucai.ttmusic.bean.Music;

public class TTApplication extends Application {

    Context appContext;
    static TTApplication application;
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

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
