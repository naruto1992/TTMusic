package cn.ucai.ttmusic.controller.interfaze;

import cn.ucai.ttmusic.model.db.Music;

public interface IShowLrc {

    // 显示相应歌词
    void showLrc(Music music);

    // 获取当前歌词进度值
    int getLrcIndex();
}
