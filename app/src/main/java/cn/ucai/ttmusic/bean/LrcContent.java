package cn.ucai.ttmusic.bean;

/******
 * 获得歌词和时间并返回的类
 *******/
public class LrcContent {
    private String Lrc; // 歌词文本
    private int Lrc_time; // // 歌词时间

    public String getLrc() {
        return Lrc;
    }

    public void setLrc(String lrc) {
        Lrc = lrc;
    }

    public int getLrc_time() {
        return Lrc_time;
    }

    public void setLrc_time(int lrc_time) {
        Lrc_time = lrc_time;
    }
}
