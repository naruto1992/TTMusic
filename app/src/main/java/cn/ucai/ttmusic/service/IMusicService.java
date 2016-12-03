package cn.ucai.ttmusic.service;

import android.media.MediaPlayer;

import java.io.Serializable;
import java.util.List;

import cn.ucai.ttmusic.bean.LrcContent;
import cn.ucai.ttmusic.bean.Music;

public interface IMusicService extends Serializable {

    // 获得当前播放进度
    int getCurrentTime();

    // 跳转到指定进度
    void moveToProgress(int progress);

    // 播放指定歌曲
    void playMusic(int position);

    // 下一曲
    void nextMusic();

    // 上一曲
    void frontMusic();

    // 歌曲是否正在播放
    boolean isPlay();

    // 暂停
    boolean pause();

    // 开始播放
    void start();

    // 停止
    void stop();

    // 显示相应歌词
    void showLrc(int position);

    // 获得播放对象
    MediaPlayer getMediaPlayer();

    // 设置播放歌曲的id
    void setCurrentItemId(int currentItemId);

    // 获得当前歌曲在列表中的id
    int getCurrentItemId();

    // 获得歌曲总长度
    int getDuration();

    //设置播放歌曲列表
    void setMusicList(List<Music> musicList);

    // 获得歌曲列表
    List<Music> getMusicList();

    // 获得当前播放的Music
    Music getCurrentMusic();

    // 设置media的状态
    void setState(int state);

    // 返回media当前状态
    int getState();

    // 设置当前播放模式
    void setPlayMode(int mode);

    // 获取当前播放模式
    int getPlayMode();

    // 获取歌词对象
    List<LrcContent> getLrc();

    // 获取当前歌词进度值
    int getLrcIndex();
}
