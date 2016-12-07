package cn.ucai.ttmusic.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.activity.PlayActivity;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.service.IMusicService;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2016/12/7.
 */

public class NotificationUtil {

    Context context;
    NotificationCompat.Builder mBuilder;
    NotificationManager manager;

    public NotificationUtil(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void initNotification(Context context, IMusicService musicService) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        Music music = musicService.getCurrentMusic();
        if (music != null) {
            views.setTextViewText(R.id.ntf_music, music.getTitle());
            views.setTextViewText(R.id.ntf_singer, music.getSinger());
        }
        if (musicService.isPlay()) {
            views.setImageViewResource(R.id.ntf_play, R.drawable.pause_button);
        } else {
            views.setImageViewResource(R.id.ntf_play, R.drawable.play_button);
        }
        views.setOnClickPendingIntent(R.id.ntf_front, sendBroadcast(context, I.BroadCast.MUSIC_FRONT));
        views.setOnClickPendingIntent(R.id.ntf_play, sendBroadcast(context, I.BroadCast.MUSIC_PLAY));
        views.setOnClickPendingIntent(R.id.ntf_next, sendBroadcast(context, I.BroadCast.MUSIC_NEXT));
        views.setOnClickPendingIntent(R.id.ntf_cancel, sendBroadcast(context, I.BroadCast.NOTIFY_CANCEL));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContent(views);

        //点击跳转至播放界面
        Intent notificationIntent = new Intent(context, PlayActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = builder.setContentIntent(pi).build();
        notification.flags = Notification.FLAG_INSISTENT;

        notificationManager.notify(999, notification);
        ToastUtil.show(context, "初始化通知");
    }

    private static PendingIntent sendBroadcast(Context context, String action) {
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify(Context context) {
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("测试标题")
                .setContentText("测试内容")
                .setTicker("测试通知来啦")//通知首次出现在通知栏，带上升动画效果的
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.icon);
    }

    /**
     * 显示大视图风格通知栏
     */
    public void showBigStyleNotify() {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[5];
        inboxStyle.setBigContentTitle("大视图内容:");
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContent(views)
                .setStyle(inboxStyle)//设置风格
                .setTicker("测试通知来啦");// 通知首次出现在通知栏，带上升动画效果的
        manager.notify(999, mBuilder.build());
    }
}
