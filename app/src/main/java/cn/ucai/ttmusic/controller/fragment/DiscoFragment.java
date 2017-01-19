package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.view.DiscoView;

public class DiscoFragment extends Fragment {

    Context context;
    Music currentMusic;
    Animation needlePlay, needlePause;

    @BindView(R.id.play_discoview)
    public DiscoView playDiscoview;
    @BindView(R.id.play_needle)
    public ImageView playNeedle;

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
        initView();
    }

    private void initView() {
        initAnimation();
    }

    private void initAnimation() {
        needlePlay = AnimationUtils.loadAnimation(context, R.anim.needle_play);
        needlePlay.setFillAfter(true); //停留在结束位置
        needlePause = AnimationUtils.loadAnimation(context, R.anim.needle_pause);
        needlePause.setFillAfter(true);
    }

    public void startRotate(Music music) {
        stopRotate();
        if (music == null) {
            playDiscoview.initByBitmap(null);
        } else {
            playDiscoview.initByMusic(music);
        }
        playDiscoview.start();
        playNeedle.startAnimation(needlePause);
        playNeedle.startAnimation(needlePlay);
    }

    public void pauseRotate() {
        if (playDiscoview.isStarted()) {
            playDiscoview.pause();
            playNeedle.startAnimation(needlePause);
            Log.e("main", "pauseRotate()");
        }
    }

    public void reStartRotate() {
        if (!playDiscoview.isStarted()) {
            playDiscoview.reStart();
            playNeedle.startAnimation(needlePlay);
            Log.e("main", "reStartRotate()");
        }
    }

    public void stopRotate() {
        playDiscoview.release();
    }

    @Override
    public void onDestroyView() {
        stopRotate();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        stopRotate();
        super.onDestroy();
    }
}
