package cn.ucai.ttmusic.model.db;


import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ucai.ttmusic.TTApplication;

public class DBManager {

    static DBManager manager;
    DaoMaster daoMaster;
    DaoSession daoSession;

    static MusicDao musicDao;
    static MusicListDao musicListDao;

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

    //////////////////////////////////////收藏////////////////////////////////////////////
    public static MusicDao getMusicDao() {
        if (musicDao == null) {
            musicDao = DBManager.getInstance().getNewSeesion().getMusicDao();
        }
        return musicDao;
    }

    public static List<Music> findMusic(int songId) {
        musicDao = getMusicDao();
        List<Music> find = musicDao.queryBuilder()
                .where(MusicDao.Properties.SongId.eq(songId))
                .build()
                .list();
        return find;
    }

    //保存到收藏
    public static void collectMusic(Music music) {
        musicDao = getMusicDao();
        music.setIsCollected(1);
        music.setCollectTime(new Date().getTime());
        musicDao.insertOrReplace(music);
    }

    //判断是否被收藏
    public static boolean isCollected(int songId) {
        musicDao = getMusicDao();
        List<Music> find = musicDao.queryBuilder()
                .where(MusicDao.Properties.SongId.eq(songId), MusicDao.Properties.IsCollected.eq(1))
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
                .where(MusicDao.Properties.SongId.eq(songId), MusicDao.Properties.IsCollected.eq(1))
                .build()
                .list();
        if (find != null && find.size() == 1) {
            Music music = find.get(0);
            music.setIsCollected(0);
            musicDao.update(music);
        }
    }

    //获取收藏列表
    public static List<Music> getCollects() {
        musicDao = getMusicDao();
        List<Music> find = musicDao.queryBuilder()
                .where(MusicDao.Properties.IsCollected.eq(1))
                .orderDesc(MusicDao.Properties.CollectTime) //按照添加时间降序排列
                .build()
                .list();
        return find;
    }

    //////////////////////////////////////歌单////////////////////////////////////////////
    public static MusicListDao getMusicListDao() {
        if (musicListDao == null) {
            musicListDao = DBManager.getInstance().getNewSeesion().getMusicListDao();
        }
        return musicListDao;
    }

    // 创建歌单
    public static boolean createMusicList(MusicList musicList) {
        musicListDao = getMusicListDao();
        if (!checkListName(musicList.getListName())) {
            musicList.setCreateTime(new Date().getTime());
            musicListDao.insert(musicList);
        }
        return false;
    }

    //判断是否重名
    public static boolean checkListName(String listName) {
        musicListDao = getMusicListDao();
        List<MusicList> find = musicListDao.queryBuilder()
                .where(MusicListDao.Properties.ListName.eq(listName))
                .build()
                .list();
        if (find != null && find.size() > 0) {
            return true;
        }
        return false;
    }

    //获取歌单名称列表
    public static List<String> getListNameList() {
        musicListDao = getMusicListDao();
        List<MusicList> find = musicListDao.queryBuilder()
                .orderDesc(MusicListDao.Properties.CreateTime) //按照添加时间降序排列
                .build()
                .list();
        List<String> nameList = new ArrayList<>();
        if (find.size() > 0) {
            for (MusicList list : find) {
                nameList.add(list.getListName());
            }
        }
        return nameList;
    }

    //添加歌曲到歌单
    public static void addToMusicList(Music music, String listName) {
        musicDao = getMusicDao();
        music.setListName(listName);
        music.setAddTime(new Date().getTime());
        musicDao.insertOrReplace(music);
    }

    //获取歌单下的歌曲
    public static List<Music> getMusicList(String listName) {
        musicDao = getMusicDao();
        List<Music> list = musicDao.queryBuilder()
                .where(MusicDao.Properties.ListName.eq(listName))
                .orderDesc(MusicDao.Properties.AddTime) //按照添加时间降序排列
                .build()
                .list();
        return list;
    }

    //从歌单中移出
    public static void removeFromMusicList(Music music) {
        musicDao = getMusicDao();
        music.setListName(null);
        musicDao.update(music);
    }

    //删除歌单
    public static void deleteMusicList(String listName) {
        musicDao = getMusicDao();
        final List<Music> list = musicDao.queryBuilder()
                .where(MusicDao.Properties.ListName.eq(listName))
                .build()
                .list();
        musicDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (Music music : list) {
                    music.setListName(null);
                    musicDao.update(music);
                }
            }
        });
    }

}
