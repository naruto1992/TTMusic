package cn.ucai.ttmusic.controller.activity;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.service.IMusicService;

public abstract class BaseActivity extends AppCompatActivity {

    IMusicService musicService; //当前播放服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
