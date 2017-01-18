package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.interfaze.IMusicService;
import cn.ucai.ttmusic.model.db.Music;
import me.wcy.lrcview.LrcView;

public class LrcFrament extends Fragment {

    @BindView(R.id.play_lrc)
    LrcView lrcView;

    Context context;
    Music currentMusic;
    IMusicService musicService; //当前播放服务

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_lrc, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        musicService = TTApplication.getInstance().getMusicService();
        initView();
    }

    private void initView() {
        if (musicService == null) {
            return;
        }
        currentMusic = musicService.getCurrentMusic();
        showLrc(currentMusic);
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

    private void showLrc(Music music) {
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
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(playLrcs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(playLrcs);
    }
}
