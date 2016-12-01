package cn.ucai.ttmusic;

import android.app.Application;

import java.util.List;

import cn.ucai.ttmusic.bean.Music;

/**
 * Created by Administrator on 2016/11/30.
 */

public class TTApplication extends Application {

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
    }
}
