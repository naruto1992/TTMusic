package cn.ucai.ttmusic.activity;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.ttmusic.service.IMusicService;
import cn.ucai.ttmusic.utils.StatusBarUtil;

public abstract class BaseActivity extends AppCompatActivity {

    IMusicService musicService; //当前播放服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setStatusBarResource(this, android.R.color.transparent);
    }

}
