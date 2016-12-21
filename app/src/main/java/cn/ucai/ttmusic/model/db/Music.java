package cn.ucai.ttmusic.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.w3c.dom.ProcessingInstruction;

import java.io.Serializable;

import org.greenrobot.greendao.DaoException;

//歌曲实体类
@Entity
public class Music implements Serializable {

    private static final long serialVersionUID = -1951456243420799821L;
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "SONG_ID")
    @Unique
    private int songId; // 歌曲在整个列表中的id
    @Property(nameInDb = "SONG_TITLE")
    private String title;// 歌曲名称
    @Property(nameInDb = "SONG_SINGER")
    private String singer;// 歌手
    @Property(nameInDb = "SONG_ALBUM")
    private String album;//专辑
    @Property(nameInDb = "SONG_URL")
    private String url;// 获得歌曲完整路径
    @Property(nameInDb = "SONG_SIZE")
    private long size;// 获得歌曲大小
    @Property(nameInDb = "SONG_TIME")
    private long time;// 获得歌曲播放时间
    @Property(nameInDb = "SONG_NAME")
    private String name;// 获得歌曲文件的名称
    @Property(nameInDb = "IS_COLLECTED")
    private int isCollected;
    @Property(nameInDb = "CollectTime")
    private long collectTime;
    @Property(nameInDb = "LIST_NAME")
    private String listName;
    @Property(nameInDb = "ADD_TIME")
    private long addTime;

    @Generated(hash = 31623025)
    public Music(Long id, int songId, String title, String singer, String album,
                 String url, long size, long time, String name, int isCollected,
                 long collectTime, String listName, long addTime) {
        this.id = id;
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        this.album = album;
        this.url = url;
        this.size = size;
        this.time = time;
        this.name = name;
        this.isCollected = isCollected;
        this.collectTime = collectTime;
        this.listName = listName;
        this.addTime = addTime;
    }

    @Generated(hash = 1263212761)
    public Music() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(int isCollected) {
        this.isCollected = isCollected;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
}
