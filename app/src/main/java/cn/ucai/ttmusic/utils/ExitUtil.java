package cn.ucai.ttmusic.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import cn.ucai.ttmusic.service.MusicService;

public class ExitUtil {

    public static void exit(final Context context) {
        new AlertDialog.Builder(context)
                .setMessage("确定要退出吗？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.stopService(new Intent(context, MusicService.class));
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(startMain);
                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }
}
