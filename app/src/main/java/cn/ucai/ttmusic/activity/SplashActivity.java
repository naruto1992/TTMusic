package cn.ucai.ttmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import java.util.List;

import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.utils.MusicUtil;

public class SplashActivity extends BaseActivity {

    Context mContext;
    static final long DELAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Music> musicList = MusicUtil.getMusicData(mContext);
                if (musicList != null) {
                    TTApplication application = TTApplication.getInstance();
                    application.setMusicList(musicList);
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }

}
