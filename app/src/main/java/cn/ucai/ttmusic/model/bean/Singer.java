package cn.ucai.ttmusic.model.bean;

import java.io.Serializable;

public class Singer implements Serializable{

    private static final long serialVersionUID = -6873923863131551774L;
    private String singerName;
    private int musicCount;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    @Override
    public String toString() {
        return "Singer{" +
                "singerName='" + singerName + '\'' +
                ", musicCount='" + musicCount + '\'' +
                '}';
    }
}
