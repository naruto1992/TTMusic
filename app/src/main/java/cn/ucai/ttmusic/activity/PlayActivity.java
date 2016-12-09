package cn.ucai.ttmusic.activity;

import android.content.Context;
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
import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.service.IMusicService;
import cn.ucai.ttmusic.utils.TimeUtil;
import cn.ucai.ttmusic.utils.ToastUtil;

public class PlayActivity extends BaseActivity {

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
    @BindView(R.id.play_pager)
    ViewPager playPager; //歌曲切换

    Music currentMusic;
    int[] modeIcons = new int[]{R.drawable.mode_normal, R.drawable.mode_single, R.drawable.mode_shuffle};

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
        handler.sendEmptyMessage(I.Handler.PLAY_MUSIC);
        playSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playStartTime.setText(TimeUtil.toTime(seekBar.getProgress()));
                musicService.moveToProgress(seekBar.getProgress());
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == I.Handler.PLAY_MUSIC) {
                currentMusic = musicService.getCurrentMusic();
                playMusicName.setText(currentMusic.getTitle());
                playSingerName.setText(currentMusic.getSinger());

                playSeekbar.setMax(musicService.getDuration());
                playSeekbar.setProgress(musicService.getCurrentTime());
                playStartTime.setText(TimeUtil.toTime(musicService.getCurrentTime()));
                playEndTime.setText(TimeUtil.toTime((int) currentMusic.getTime()));

                playPlayModeBtn.setImageResource(modeIcons[musicService.getPlayMode()]);
                if (musicService.isPlay()) {
                    playBtn.setImageResource(R.drawable.pause_button);
                } else {
                    playBtn.setImageResource(R.drawable.play_button);
                }

                handler.sendEmptyMessageDelayed(I.Handler.PLAY_MUSIC, 500); //每半秒刷新一次
            }
        }
    };

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
                ToastUtil.show(mContext, "收藏(开发中)");
                break;
        }
    }

    //播放、上一曲、下一曲
    @OnClick({R.id.play_pre_btn, R.id.play_btn, R.id.play_next_btn})
    public void playControl(View view) {
        switch (view.getId()) {
            case R.id.play_pre_btn:
                musicService.frontMusic();
                break;
            case R.id.play_btn:
                if (musicService.isPlay()) {
                    musicService.setCurrentTime(musicService.getCurrentTime());
                    musicService.pause();
                } else {
                    musicService.start();
                }
                break;
            case R.id.play_next_btn:
                musicService.nextMusic();
                break;
        }
        handler.sendEmptyMessage(I.Handler.PLAY_MUSIC); //立即生效
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
        handler.sendEmptyMessage(I.Handler.PLAY_MUSIC);//立即生效
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
