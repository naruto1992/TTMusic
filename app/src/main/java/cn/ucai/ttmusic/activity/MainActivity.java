package cn.ucai.ttmusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.adapter.MyPagerAdapter;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.fragment.FragmentFriendsMusic;
import cn.ucai.ttmusic.fragment.FragmentLocalMusic;
import cn.ucai.ttmusic.fragment.FragmentNetMusic;
import cn.ucai.ttmusic.service.IMusicService;
import cn.ucai.ttmusic.service.MusicService;
import cn.ucai.ttmusic.utils.FastBlur;
import cn.ucai.ttmusic.utils.ToastUtil;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    Context mContext;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.tab_net_music)
    ImageView tabNetMusic; //网络音乐图标
    @BindView(R.id.tab_local_music)
    ImageView tabLocalMusic; //本地音乐图标
    @BindView(R.id.tab_friends_music)
    ImageView tabFriendsMusic; //好友音乐图标
    @BindView(R.id.play)
    Button play; //播放键
    @BindView(R.id.musicName)
    TextView musicName; //歌曲名字
    @BindView(R.id.musicSinger)
    TextView musicSinger; //歌手名字
    @BindView(R.id.fragments)
    ViewPager fragmentsPager;

    List<Fragment> fragments;
    FragmentNetMusic netMusic;
    FragmentLocalMusic localMusic;
    FragmentFriendsMusic friendsMusic;

    IMusicService musicService;
    List<Music> musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initDrawerLayout();
        initTabs();
        initMusicListAndService();
    }

    //////////////////////////////////////view部分/////////////////////////////////////////
    private void initDrawerLayout() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        //navigationView.setItemIconTintList(null);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg002);
        navigationView.setBackground(new BitmapDrawable(FastBlur.blur(bitmap, 30, false)));
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initTabs() {
        fragments = new ArrayList<>();
        netMusic = new FragmentNetMusic();
        localMusic = new FragmentLocalMusic();
        friendsMusic = new FragmentFriendsMusic();
        fragments.add(netMusic);
        fragments.add(localMusic);
        fragments.add(friendsMusic);
        fragmentsPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));
        fragmentsPager.addOnPageChangeListener(this);
        switchFragment(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switchFragment(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mode_normal:
                item.setChecked(true);
                ToastUtil.show(mContext, "普通模式");
                break;
            case R.id.menu_mode_shuffle:
                item.setChecked(true);
                ToastUtil.show(mContext, "随机播放");
                break;
            case R.id.menu_mode_single:
                item.setChecked(true);
                ToastUtil.show(mContext, "单曲循环");
                break;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    @OnClick({R.id.ivExpand, R.id.ivSearch})
    public void initToolbarAction(View v) {
        switch (v.getId()) {
            case R.id.ivExpand:
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START, true);
                }
                break;
            case R.id.ivSearch:
                break;
        }
    }

    @OnClick({R.id.tab_net_music, R.id.tab_local_music, R.id.tab_friends_music})
    public void switchTabs(View v) {
        int index;
        switch (v.getId()) {
            case R.id.tab_net_music:
                index = 0;
                break;
            case R.id.tab_local_music:
                index = 1;
                break;
            case R.id.tab_friends_music:
                index = 2;
                break;
            default:
                index = 0;
                break;
        }
        switchFragment(index);
    }

    int lastIndex = 0;

    private void switchFragment(int index) {
        if (index == lastIndex) {
            return;
        }
        List<ImageView> tabs = new ArrayList<>();
        tabs.add(tabNetMusic);
        tabs.add(tabLocalMusic);
        tabs.add(tabFriendsMusic);
        for (int i = 0; i < tabs.size(); i++) {
            if (i == index) {
                tabs.get(i).setBackground(getResources().getDrawable(R.drawable.tab_selected));
            } else {
                tabs.get(i).setBackground(null);
            }
        }
        fragmentsPager.setCurrentItem(index);
        lastIndex = index;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //////////////////////////////////////服务部分/////////////////////////////////////////
    private void initMusicListAndService() {
        musicList = TTApplication.getInstance().getMusicList();
        // 绑定服务
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, new MyServiceConn(), BIND_AUTO_CREATE);
    }

    //绑定服务的连接类
    class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = (IMusicService) iBinder;
            musicService.setMusicList(musicList);
            musicService.setCurrentItemId(0);
            musicService.moveToProgress(0);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    //////////////////////////////////////handler部分/////////////////////////////////////////

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
}
