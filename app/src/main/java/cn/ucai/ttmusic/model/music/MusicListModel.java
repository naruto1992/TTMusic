package cn.ucai.ttmusic.model.music;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.ttmusic.bean.MusicListBean;
import cn.ucai.ttmusic.model.db.DBManager;
import cn.ucai.ttmusic.model.db.Music;

public class MusicListModel {

    public static List<Music> getFavorites() {
        return DBManager.getCollects();
    }

    public static List<MusicListBean> getMusicList() {
        List<MusicListBean> musicList = new ArrayList<>();
        MusicListBean favorites = new MusicListBean();
        favorites.setList_title("我喜欢的音乐");
        favorites.setMusicList(getFavorites());
        musicList.add(favorites);
        return musicList;
    }

}
