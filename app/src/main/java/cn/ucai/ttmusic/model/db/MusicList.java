package cn.ucai.ttmusic.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MusicList {
    @Id(autoincrement = true)
    private Long listId;
    @Property(nameInDb = "LIST_NAME")
    @Unique
    private String listName;
    @Property(nameInDb = "LIST_COVER_URL")
    private String listCoverUrl;
    @Property(nameInDb = "CREATE_TIME")
    private long createTime;

    @Generated(hash = 861046078)
    public MusicList(Long listId, String listName, String listCoverUrl, long createTime) {
        this.listId = listId;
        this.listName = listName;
        this.listCoverUrl = listCoverUrl;
        this.createTime = createTime;
    }

    @Generated(hash = 1135234633)
    public MusicList() {
    }

    public Long getListId() {
        return this.listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public String getListName() {
        return this.listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListCoverUrl() {
        return this.listCoverUrl;
    }

    public void setListCoverUrl(String listCoverUrl) {
        this.listCoverUrl = listCoverUrl;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
