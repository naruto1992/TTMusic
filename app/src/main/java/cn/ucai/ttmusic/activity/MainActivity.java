package cn.ucai.ttmusic.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.adapter.MyPagerAdapter;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.fragment.FragmentFriendsMusic;
import cn.ucai.ttmusic.fragment.FragmentLocalMusic;
import cn.ucai.ttmusic.fragment.FragmentNetMusic;
import cn.ucai.ttmusic.service.IMusicService;
import cn.ucai.ttmusic.service.MusicService;
import cn.ucai.ttmusic.utils.ExitUtil;
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
    @BindView(R.id.fragments)
    ViewPager fragmentsPager;

    @BindView(R.id.panel_playOrPause)
    ImageView panelPlayOrPause; //暂停或播放
    @BindView(R.id.panel_musicName)
    TextView panelMusicName; //歌曲名
    @BindView(R.id.panel_singerName)
    TextView panelSingerName; //歌手名

    List<Fragment> fragments;
    FragmentNetMusic netMusic;
    FragmentLocalMusic localMusic;
    FragmentFriendsMusic friendsMusic;

    List<Music> musicList; //当前播放列表
    Music currentMusic; //当前播放歌曲
    ServiceConnection connection;
    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initDrawerLayout();
        initTabs();
        bindService();
        initBroadcast();
    }

    //////////////////////////////////////view部分/////////////////////////////////////////
    private void initDrawerLayout() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        //navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
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

    //////////////////////////////////////点击事件/////////////////////////////////////////

    /**
     * 菜单点击
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mode_normal:
                item.setChecked(true);
                musicService.setPlayMode(I.PlayMode.MODE_NORMAL);
                break;
            case R.id.menu_mode_shuffle:
                item.setChecked(true);
                musicService.setPlayMode(I.PlayMode.MODE_SHUFFLE);
                break;
            case R.id.menu_mode_single:
                item.setChecked(true);
                musicService.setPlayMode(I.PlayMode.MODE_SINGLE);
                break;
            case R.id.menu_search_music:
                ToastUtil.show(mContext, "扫描音乐(开发中)");
                break;
            case R.id.menu_wallpaper:
                ToastUtil.show(mContext, "壁纸(开发中)");
                break;
            case R.id.menu_sleep:
                ToastUtil.show(mContext, "睡眠(开发中)");
                break;
            case R.id.menu_about:
                ToastUtil.show(mContext, "关于(开发中)");
                break;
            case R.id.menu_setting:
                ToastUtil.show(mContext, "设置(开发中)");
                break;
            case R.id.menu_exit:
                new ExitUtil().exit(mContext);
                break;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    /**
     * 菜单栏、控制栏
     */
    @OnClick({R.id.ivExpand, R.id.ivSearch, R.id.musicControlPanel})
    public void initToolbarAction(View v) {
        switch (v.getId()) {
            case R.id.ivExpand:
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START, true);
                }
                break;
            case R.id.ivSearch:
                ToastUtil.show(mContext, "搜索(开发中)");
                break;
            case R.id.musicControlPanel:
                if (musicService == null || currentMusic == null) {
                    return;
                }
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra(I.Intent.MUSIC_SERVICE, musicService);
                startActivity(intent);
                break;
        }
    }

    /**
     * 标签栏
     */
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

    long exitTime = 0;// 退出时间

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.show(mContext, "再按一下返回到桌面");
            exitTime = System.currentTimeMillis();
        } else {
            // 返回桌面，不退出程序
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    //播放或暂停
    @OnClick(R.id.panel_playOrPause)
    public void playOrPause(View v) {
        ImageView view = (ImageView) v;
        if (musicList == null || musicList.size() == 0 || musicService == null) {
            ToastUtil.show(mContext, "无法启动服务");
            return;
        }
        switch (musicService.getState()) {
            case I.PlayState.IS_PAUSE:
                musicService.start();
                view.setImageResource(R.drawable.playbar_btn_pause);
                break;
            case I.PlayState.IS_PLAY:
                musicService.pause();
                view.setImageResource(R.drawable.playbar_btn_play);
                break;
            case I.PlayState.IS_INIT:
                musicService.setMusicList(musicList);
                musicService.playMusic(0);
                break;
        }
    }

    //下一首
    @OnClick(R.id.panel_next)
    public void playNext(View v) {
        musicService.nextMusic();
    }

    //////////////////////////////////////服务部分/////////////////////////////////////////
    public void bindService() {
        musicList = TTApplication.getInstance().getMusicList();
        if (musicList == null || musicList.size() == 0) {
            musicService = null;
            ToastUtil.show(mContext, "当前没有可播放音乐，无法启动服务");
            return;
        }
        // 绑定服务
        Intent intent = new Intent(mContext, MusicService.class);
        connection = new MyServiceConn();
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    //绑定服务的连接类
    class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = (IMusicService) iBinder;
//            musicService.setMusicList(musicList);
//            musicService.setCurrentItemId(0);
//            musicService.moveToProgress(0);
            //启动handler
            handler.sendEmptyMessage(I.Handler.PLAY_MUSIC);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    //////////////////////////////////////handler、广播/////////////////////////////////////////

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == I.Handler.PLAY_MUSIC) {
                if (musicService.isPlay()) {
                    currentMusic = musicService.getCurrentMusic();
                    //设置显示
                    panelMusicName.setText(currentMusic.getTitle());
                    panelSingerName.setText(currentMusic.getSinger());
                    panelPlayOrPause.setImageResource(R.drawable.playbar_btn_pause);
                } else {
                    panelPlayOrPause.setImageResource(R.drawable.playbar_btn_play);
                }
                //设置模式
                navigationView.getMenu().getItem(musicService.getPlayMode()).setChecked(true);

                handler.sendEmptyMessageDelayed(I.Handler.PLAY_MUSIC, 500);//每半秒刷新一次
            }
        }
    };

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.BroadCast.MUSIC_ACTION);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.BroadCast.MUSIC_ACTION:
                        if (intent == null || intent.getExtras() == null) {
                            return;
                        }
                        Bundle data = intent.getExtras();
                        List<Music> list = (List<Music>) data.getSerializable(I.BroadCast.MUSIC_LIST);
                        int position = data.getInt(I.BroadCast.MUSIC_POSITION, 0);
                        musicService.setMusicList(list);
                        musicService.pause();
                        musicService.playMusic(position);
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicService != null) {
            handler.sendEmptyMessage(I.Handler.PLAY_MUSIC);
        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        if (mReceiver != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
