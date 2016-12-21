package cn.ucai.ttmusic.controller.interfaze;

import java.util.List;

import cn.ucai.ttmusic.model.db.Music;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ItemClickListener {

    List<Music> getCurrentMusicList();

    void onHeaderClick();

    void onItemClick(int position, Music music);
}