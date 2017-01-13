package cn.ucai.ttmusic.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.adapter.PlayViewPagerAdapter;
import cn.ucai.ttmusic.controller.fragment.PlayViewFragment;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.DBManager;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.utils.TimeUtil;
import cn.ucai.ttmusic.model.utils.ToastUtil;
import cn.ucai.ttmusic.view.DiscoView;
import cn.ucai.ttmusic.view.MyPlayViewPager;
import me.wcy.lrcview.LrcView;

public class PlayActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener {

    Context mContext;

    @BindView(R.id.blurView)
    ImageView blurView; //模糊背景层
    @BindView(R.id.play_musicName)
    TextView playMusicName; //歌曲名
    @BindView(R.id.play_singerName)
    TextView playSingerName; //歌手名
    @BindView(R.id.play_start_time)
    TextView playStartTime; //起始时间、已播放时间
    @BindView(R.id.play_seekbar)
    SeekBar playSeekbar; //进度控制条
    @BindView(R.id.play_end_time)
    TextView playEndTime; //歌曲总时间
    @BindView(R.id.play_playMode_btn)
    ImageView playPlayModeBtn; //播放模式按钮
    @BindView(R.id.play_btn)
    ImageView playBtn; //播放按钮
    @BindView(R.id.btn_favorite)
    ImageView btnFavorite; //收藏按钮
    @BindView(R.id.play_lrc)
    LrcView lrcView; //歌词
    @BindView(R.id.playViewPager)
    MyPlayViewPager playViewPager;

    List<PlayViewFragment> fragments = new ArrayList<>();
    PlayViewPagerAdapter adapter;
    int currentPosition;

    Music currentMusic;
    int[] modeIcons = new int[]{R.drawable.mode_normal, R.drawable.mode_single, R.drawable.mode_shuffle};
    boolean isCollected; //当前歌曲是否被收藏

    MediaPlayer mediaPlayer; // 音乐播放对象
    LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        // 保持屏幕唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContext = this;
        musicService = TTApplication.getInstance().getMusicService();
        if (musicService == null) {
            return;
        }
        broadcastManager = LocalBroadcastManager.getInstance(TTApplication.getContext());
        initView();
    }

    private void initView() {
        playSeekbar.setOnSeekBarChangeListener(this);
        mediaPlayer = musicService.getMediaPlayer();
        // 设置播放完毕监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                // 如果播放完毕就直接下一曲
                musicService.nextMusic();
                handler.sendEmptyMessage(I.Handler.NEXT_MUSIC);
            }
        });
        initPlayViewPager();
        handler.sendEmptyMessage(I.Handler.INIT_VIEW);
        handler.sendEmptyMessage(I.Handler.PLAY_MUSIC);
    }

    private void initPlayViewPager() {
        for (Music music : musicService.getMusicList()) {
            PlayViewFragment fragment = new PlayViewFragment();
            Bundle data = new Bundle();
            data.putSerializable(I.Intent.CURRENT_MUSIC, music);
            fragment.setArguments(data);
            fragments.add(fragment);
        }
        adapter = new PlayViewPagerAdapter(getSupportFragmentManager(), fragments);
        playViewPager.setAdapter(adapter);
        playViewPager.setOffscreenPageLimit(2);
        playViewPager.addOnPageChangeListener(this);
        currentPosition = playViewPager.getCurrentItem();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == musicService.getCurrentItemId()) {
            return;
        }
        if (fragments.get(currentPosition).isRotating()) {
            fragments.get(currentPosition).pauseRotate();
        }
        musicService.playMusic(position);
        fragments.get(position).startRotate();
        handler.sendEmptyMessage(I.Handler.PAGE_SELECT);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case I.Handler.INIT_VIEW:
                    adapter.currentFragment.stopRotate();
                    setMusicInfo();
                    setPlayButton();
                    setPlayMode();
                    initDiscoView();
                    break;
                case I.Handler.PLAY_MUSIC:
                    setPlayButton();
                    playStartTime.setText(TimeUtil.toTime(musicService.getCurrentTime()));
                    playSeekbar.setProgress(musicService.getCurrentTime());
                    handler.sendEmptyMessageDelayed(I.Handler.PLAY_MUSIC, 500);
                    break;
                case I.Handler.PAUSE_MUSIC:
                    setPlayButton();
                    break;
                case I.Handler.FRONT_MUSIC:
                    adapter.currentFragment.stopRotate();
                    setMusicInfo();
                    setPlayButton();
                    initDiscoView();
                    mHandler.removeCallbacks(playLrcs);
                    showLrc(musicService.getCurrentMusic());
                    break;
                case I.Handler.NEXT_MUSIC:
                    adapter.currentFragment.stopRotate();
                    setMusicInfo();
                    setPlayButton();
                    initDiscoView();
                    mHandler.removeCallbacks(playLrcs);
                    showLrc(musicService.getCurrentMusic());
                    break;
                case I.Handler.SET_MODE:
                    setPlayMode();
                    break;
                case I.Handler.START_LRC:
                    showLrc(musicService.getCurrentMusic());
                    break;
                case I.Handler.PAUSE_LRC:
                    mHandler.removeCallbacks(playLrcs);
                    break;
                case I.Handler.PAGE_SELECT:
                    setMusicInfo();
                    setPlayButton();
                    mHandler.removeCallbacks(playLrcs);
                    handler.sendEmptyMessage(I.Handler.START_LRC);
                    break;
            }
        }
    };

    //////////////////////////////////与界面相关的几个方法//////////////////////////////////
    private void setMusicInfo() {
        currentMusic = musicService.getCurrentMusic();
        playMusicName.setText(currentMusic.getTitle());
        playSingerName.setText(currentMusic.getSinger());

        playSeekbar.setMax(musicService.getDuration());
        playEndTime.setText(TimeUtil.toTime((int) currentMusic.getTime()));

        isCollected = DBManager.isCollected(currentMusic.getSongId());
        btnFavorite.setImageResource(isCollected ? R.drawable.favorite_selected_on : R.drawable.favorite_selected_off);
    }

    private void setPlayButton() {
        if (musicService.isPlay()) {
            playBtn.setImageResource(R.drawable.pause_button);
        } else {
            playBtn.setImageResource(R.drawable.play_button);
        }
    }

    private void setPlayMode() {
        playPlayModeBtn.setImageResource(modeIcons[musicService.getPlayMode()]);
    }

    private void initDiscoView() {
        if (fragments.get(currentPosition).isRotating()) {
            fragments.get(currentPosition).pauseRotate();
        }
        int position = musicService.getCurrentItemId();
        playViewPager.setCurrentItem(position);
        fragments.get(position).startRotate();
        currentPosition = position;
        if (!musicService.isPlay()) {
            fragments.get(position).pauseRotate();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    boolean isLrcShow = false;

    @OnClick(R.id.play_cover)
    public void showLrcOrDisco(View view) {
        if (isLrcShow) {
            playViewPager.setVisibility(View.VISIBLE);
            lrcView.setVisibility(View.GONE);
            handler.sendEmptyMessage(I.Handler.PAUSE_LRC);
            isLrcShow = false;
        } else {
            playViewPager.setVisibility(View.GONE);
            lrcView.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(I.Handler.START_LRC);
            isLrcShow = true;
        }
    }

    //返回、分享、收藏
    @OnClick({R.id.icon_back, R.id.icon_share, R.id.btn_favorite})
    public void action(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                onBackPressed();
                break;
            case R.id.icon_share:
                ToastUtil.show(mContext, "分享(开发中)");
                break;
            case R.id.btn_favorite:
                if (isCollected) {
                    DBManager.cancelCollect(currentMusic.getSongId());
                    ToastUtil.show(mContext, "取消收藏");
                } else {
                    DBManager.collectMusic(currentMusic);
                    ToastUtil.show(mContext, "收藏成功");
                }
                Intent update = new Intent(I.BroadCast.UPDATE_LIST);
                broadcastManager.sendBroadcast(update);
                break;
        }
    }

    //播放、上一曲、下一曲
    @OnClick({R.id.play_pre_btn, R.id.play_btn, R.id.play_next_btn})
    public void playControl(View view) {
        switch (view.getId()) {
            case R.id.play_pre_btn:
                musicService.frontMusic();
                handler.sendEmptyMessage(I.Handler.FRONT_MUSIC);
                break;
            case R.id.play_btn:
                if (musicService.isPlay()) {
                    musicService.setCurrentTime(musicService.getCurrentTime());
                    musicService.pause();
                    handler.sendEmptyMessage(I.Handler.PAUSE_MUSIC);
                    handler.sendEmptyMessage(I.Handler.PAUSE_LRC);
                    adapter.currentFragment.pauseRotate();
                } else {
                    musicService.start();
                    handler.sendEmptyMessage(I.Handler.START_LRC);
                    adapter.currentFragment.reStartRotate();
                }
                break;
            case R.id.play_next_btn:
                musicService.nextMusic();
                handler.sendEmptyMessage(I.Handler.NEXT_MUSIC);
                break;
        }
    }

    //播放模式切换
    @OnClick(R.id.play_playMode_btn)
    public void setPlayMode(View v) {
        switch (musicService.getPlayMode()) {
            case I.PlayMode.MODE_NORMAL:
                musicService.setPlayMode(I.PlayMode.MODE_SINGLE);
                break;
            case I.PlayMode.MODE_SINGLE:
                musicService.setPlayMode(I.PlayMode.MODE_SHUFFLE);
                break;
            case I.PlayMode.MODE_SHUFFLE:
                musicService.setPlayMode(I.PlayMode.MODE_NORMAL);
                break;
        }
        handler.sendEmptyMessage(I.Handler.SET_MODE);
    }

    Handler mHandler = new Handler();
    Runnable playLrcs = new Runnable() {
        @Override
        public void run() {
            if (musicService.isPlay()) {
                long time = musicService.getCurrentTime();
                lrcView.updateTime(time);
            }
            mHandler.postDelayed(this, 100);
        }
    };

    public void showLrc(Music music) {
        // 读取同文件夹下的歌词文件
        File f = new File(music.getUrl().replace(".mp3", ".lrc"));
        try {
            lrcView.loadLrc(f);
            mHandler.post(playLrcs);
        } catch (Exception e) {
            lrcView.setLabel("找不到歌词(&gt;_&lt;)");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        playStartTime.setText(TimeUtil.toTime(seekBar.getProgress()));
        musicService.moveToProgress(seekBar.getProgress());
        lrcView.onDrag(seekBar.getProgress());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(playLrcs);
        super.onDestroy();
    }
}
