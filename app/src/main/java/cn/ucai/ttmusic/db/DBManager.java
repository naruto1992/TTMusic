package cn.ucai.ttmusic.db;


import java.util.Date;
import java.util.List;

import cn.ucai.ttmusic.TTApplication;

public class DBManager {

    static DBManager manager;
    DaoMaster daoMaster;
    DaoSession daoSession;

    static MusicDao musicDao;

    public DBManager() {
        if (manager == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(TTApplication.getContext(), "ttmusic.db", null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            daoSession = daoMaster.newSession();
        }
    }

    public static DBManager getInstance() {
        if (manager == null) {
            synchronized (DBManager.class) {
                if (manager == null) {
                    manager = new DBManager();
                }
            }
        }
        return manager;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoSession getNewSeesion() {
        daoSession = daoMaster.newSession();
        return daoSession;
    }

    //////////////////////////////////////我是分割线////////////////////////////////////////////
    //我的收藏
    public static MusicDao getMusicDao() {
        if (musicDao == null) {
            musicDao = DBManager.getInstance().getNewSeesion().getMusicDao();
        }
        return musicDao;
    }

    //保存到收藏
    public static void collectMusic(Music music) {
        musicDao = getMusicDao();
        music.setCollect(1);
        music.setCollectTime(new Date().getTime());
        musicDao.insert(music);
    }

    //判断是否被收藏
    public static boolean isCollected(int songId) {
        musicDao = getMusicDao();
        List<Music> find = musicDao.queryBuilder()
                .where(MusicDao.Properties.SongId.eq(songId))
                .build()
                .list();
        if (find != null && find.size() == 1) {
            return true;
        }
        return false;
    }

    //取消收藏
    public static void cancelCollect(int songId) {
        musicDao = getMusicDao();
        List<Music> find = musicDao.queryBuilder()
                .where(MusicDao.Properties.SongId.eq(songId))
                .build()
                .list();
        for (Music music : find) {
            musicDao.delete(music);
        }
    }

    //获取收藏列表
    public static List<Music> getCollects() {
        musicDao = getMusicDao();
        List<Music> find = musicDao.queryBuilder()
                .orderDesc(MusicDao.Properties.CollectTime) //按照添加时间降序排列
                .build()
                .list();
        return find;
    }
}
