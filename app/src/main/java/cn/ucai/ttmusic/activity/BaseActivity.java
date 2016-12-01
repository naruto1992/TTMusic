package cn.ucai.ttmusic.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ucai.ttmusic.utils.StatusBarUtil;

/**
 * Created by Administrator on 2016/11/30.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarResource(this, android.R.color.transparent);
    }
}
