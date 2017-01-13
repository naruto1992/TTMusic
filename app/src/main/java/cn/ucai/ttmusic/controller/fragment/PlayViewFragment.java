package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.interfaze.IMusicService;
import cn.ucai.ttmusic.controller.interfaze.IPlayView;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.view.DiscoView;

public class PlayViewFragment extends Fragment implements IPlayView {

    Context context;
    Music currentMusic;
    Animation needlePlay, needlePause;

    @BindView(R.id.play_discoview)
    DiscoView playDiscoview;
    @BindView(R.id.play_needle)
    ImageView playNeedle;

    IMusicService musicService; //当前播放服务


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_play_item, container, false);
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
        currentMusic = (Music) getArguments().getSerializable(I.Intent.MUSIC);
        if (currentMusic == null || musicService == null) {
            return;
        }
        initAnimation();
        playDiscoview.init(currentMusic);
    }

    private void initAnimation() {
        needlePlay = AnimationUtils.loadAnimation(context, R.anim.needle_play);
        needlePlay.setFillAfter(true); //停留在结束位置
        needlePause = AnimationUtils.loadAnimation(context, R.anim.needle_pause);
        needlePause.setFillAfter(true);
    }

    @Override
    public void startRotate() {
        playDiscoview.start();
    }

    @Override
    public void pauseRotate() {
        playDiscoview.pause();
    }

    @Override
    public void reStartRotate() {
        playDiscoview.reStart();
    }

    @Override
    public void stopRotate() {
        playDiscoview.release();
    }
}
