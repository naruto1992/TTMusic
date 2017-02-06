package cn.ucai.ttmusic.controller.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.adapter.PlayPagerAdapter;
import cn.ucai.ttmusic.controller.fragment.DiscoFragment;
import cn.ucai.ttmusic.controller.fragment.LrcFrament;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.utils.TimeUtil;
import cn.ucai.ttmusic.model.utils.ToastUtil;

public class PlayActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

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
    @BindView(R.id.play_middle)
    ViewPager playViewPager;

    Music currentMusic;
    int[] modeIcons = new int[]{R.drawable.mode_normal, R.drawable.mode_single, R.drawable.mode_shuffle};
    MediaPlayer mediaPlayer; // 音乐播放对象
    DiscoFragment discoFragment;
    LrcFrament lrcFrament;

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
        initView();
    }

    private void initView() {
        initPlayViewPager();
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
        handler.sendEmptyMessage(I.Handler.INIT_VIEW);
    }

    private void initPlayViewPager() {
        discoFragment = new DiscoFragment();
        lrcFrament = new LrcFrament();
        PlayPagerAdapter adapter = new PlayPagerAdapter(getSupportFragmentManager(), discoFragment, lrcFrament);
        playViewPager.setAdapter(adapter);
        playViewPager.setOffscreenPageLimit(2);
        playViewPager.setCurrentItem(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case I.Handler.INIT_VIEW:
                    setMusicInfo();
                    setPlayButton();
                    setPlayMode();
                    handler.post(startDisco);
                    handler.post(setTimeAndProgress);
                    break;
                case I.Handler.PLAY_MUSIC:
                    setPlayButton();
                    discoFragment.reStartRotate();
                    break;
                case I.Handler.PAUSE_MUSIC:
                    setPlayButton();
                    discoFragment.pauseRotate();
                    break;
                case I.Handler.FRONT_MUSIC:
                    setMusicInfo();
                    setPlayButton();
                    discoFragment.startRotate(musicService.getCurrentMusic(), true);
                    lrcFrament.showLrc(currentMusic);
                    break;
                case I.Handler.NEXT_MUSIC:
                    setMusicInfo();
                    setPlayButton();
                    discoFragment.startRotate(musicService.getCurrentMusic(), true);
                    lrcFrament.showLrc(currentMusic);
                    break;
                case I.Handler.SET_MODE:
                    setPlayMode();
                    break;
            }
        }
    };
    Runnable startDisco = new Runnable() {
        @Override
        public void run() {
            discoFragment.startRotate(musicService.getCurrentMusic(), musicService.isPlay());
            lrcFrament.showLrc(currentMusic);
        }
    };

    Runnable setTimeAndProgress = new Runnable() {
        @Override
        public void run() {
            playStartTime.setText(TimeUtil.toTime(musicService.getCurrentTime()));
            playSeekbar.setProgress(musicService.getCurrentTime());
            handler.postDelayed(this, 500);
        }
    };

    //////////////////////////////////与界面相关的几个方法//////////////////////////////////
    private void setMusicInfo() {
        currentMusic = musicService.getCurrentMusic();
        playMusicName.setText(currentMusic.getTitle());
        playSingerName.setText(currentMusic.getSinger());

        playSeekbar.setMax(musicService.getDuration());
        playEndTime.setText(TimeUtil.toTime((int) currentMusic.getTime()));
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

    ////////////////////////////////////////////////////////////////////////////////////////

    //返回、分享、列表
    @OnClick({R.id.icon_back, R.id.icon_share, R.id.play_music_list})
    public void action(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                onBackPressed();
                break;
            case R.id.icon_share:
                ToastUtil.show(mContext, "分享(开发中)");
                break;
            case R.id.play_music_list:
                ToastUtil.show(mContext, "播放列表(开发中)");
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
                } else {
                    musicService.start();
                    handler.sendEmptyMessage(I.Handler.PLAY_MUSIC);
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(startDisco);
        handler.removeCallbacks(setTimeAndProgress);
        super.onDestroy();
    }
}
