package cn.ucai.ttmusic.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import cn.ucai.ttmusic.bean.Music;

public class MusicUtil {

    public static List<Music> getMusicData(Context context) {
        // 创建一个Music的对象集合
        List<Music> musicList = new ArrayList<Music>();
        // 获得内容提供者
        ContentResolver cr = context.getContentResolver();
        if (cr != null) {
            // 获取手机里的所有歌曲
            Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (null == cursor) {
                return null;
            }
            if (cursor.moveToFirst()) {
                do {
                    // 创建一个歌曲对象
                    Music m = new Music();
                    // 获取id
                    int songId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    // 获得歌手名称
                    String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    // 如果为<unknown>则显示为未知
                    if ("<unknown>".equals(singer)) {
                        singer = "未知";
                    }
                    //获得专辑名称
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    // 如果为<unknown>则显示为未知
                    if ("<unknown>".equals(album)) {
                        album = "未知";
                    }
                    // 歌曲文件大小
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    // 歌曲总播放时间
                    long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    // 获得歌曲路径
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    // 歌曲文件的名称
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    // 获得歌曲名称
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    // 获取歌曲文件格式后缀׺
                    String sbr = name.substring(name.lastIndexOf(".") + 1);
                    // ֧支持mp3格式
                    if (sbr.equals("mp3")) {
                        m.setSongId(songId);
                        m.setTitle(title);
                        m.setSinger(singer);
                        m.setAlbum(album);
                        m.setSize(size);
                        m.setTime(time);
                        m.setUrl(url);
                        m.setName(name);
                        m.setFavorite(0);
                        musicList.add(m);
                    }
                } while (cursor.moveToNext());
            }
        }
        return musicList;
    }
}
