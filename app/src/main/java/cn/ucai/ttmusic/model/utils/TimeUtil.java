package cn.ucai.ttmusic.model.utils;

public class TimeUtil {

    // 时间格式转换
    public static String toTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
}
