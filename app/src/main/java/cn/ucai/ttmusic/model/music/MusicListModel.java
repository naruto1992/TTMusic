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
        List<String> titles = DBManager.getListNameList();
        for (int i = 0; i < titles.size(); i++) {
            MusicListBean bean = new MusicListBean();
            String title = titles.get(i);
            bean.setList_title(title);
            bean.setMusicList(DBManager.getMusicList(title));
        }
        return musicList;
    }

}
