package cn.ucai.ttmusic.model.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import cn.ucai.ttmusic.controller.service.MusicService;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ExitUtil {

    public static void exit(final Context context) {
        DialogBuilder builder = new DialogBuilder(context);
        builder.setTitle("提示")
                .setMessage("确定要退出吗？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //关闭服务
                        context.stopService(new Intent(context, MusicService.class));
                        //关闭通知
                        NotificationManager manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                        manger.cancelAll();
                        //关闭进程
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }
}
