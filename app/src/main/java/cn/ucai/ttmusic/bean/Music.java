package cn.ucai.ttmusic.bean;

import java.io.Serializable;

//歌曲实体类
public class Music implements Serializable {

    private int songId; // 歌曲在整个列表中的id
    private String title;// 歌曲名称
    private String singer;// 歌手
    private String album;//专辑
    private String url;// 获得歌曲完整路径
    private long size;// 获得歌曲大小
    private long time;// 获得歌曲播放时间
    private String name;// 获得歌曲文件的名称
    private int favorite; // 是否收藏在我的最爱

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Music{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                ", time=" + time +
                ", name='" + name + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
