package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.DBManager;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.utils.ToastUtil;
import cn.ucai.ttmusic.view.DiscoView;

public class DiscoFragment extends Fragment {

    Context context;
    Animation needlePlay, needlePause;

    @BindView(R.id.play_discoview)
    public DiscoView playDiscoview;
    @BindView(R.id.play_needle)
    public ImageView playNeedle;
    @BindView(R.id.btn_favorite)
    ImageView btnFavorite;

    Music currentMusic;
    boolean isCollected; //当前歌曲是否被收藏
    LocalBroadcastManager broadcastManager;

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
        broadcastManager = LocalBroadcastManager.getInstance(TTApplication.getContext());
        initAnimation();
    }

    private void initAnimation() {
        needlePlay = AnimationUtils.loadAnimation(context, R.anim.needle_play);
        needlePlay.setFillAfter(true); //停留在结束位置
        needlePause = AnimationUtils.loadAnimation(context, R.anim.needle_pause);
        needlePause.setFillAfter(true);
    }

    public void startRotate(Music music, boolean isPlay) {
        if (playDiscoview == null) {
            Log.e("main", "playDiscoview == null");
            return;
        }
        stopRotate();
        if (music == null) { //其实不应该为空，但谨慎起见还是做了防空判断
            playDiscoview.initByBitmap(null);
        } else {
            playDiscoview.initByMusic(music);
            //初始化收藏按钮
            currentMusic = music;
            initCollect(music);
        }
        playDiscoview.start();
        if (isPlay) {
            playNeedle.startAnimation(needlePlay);
        } else {
            playDiscoview.pause();
            playNeedle.startAnimation(needlePause);
        }
    }

    private void initCollect(Music music) {
        isCollected = DBManager.isCollected(music.getSongId());
        btnFavorite.setImageResource(isCollected ? R.drawable.favorite_selected_on : R.drawable.favorite_selected_off);
    }

    public void pauseRotate() {
        if (playDiscoview.isStarted()) {
            playDiscoview.pause();
            playNeedle.startAnimation(needlePause);
        }
    }

    public void reStartRotate() {
        if (!playDiscoview.isStarted()) {
            playDiscoview.reStart();
            playNeedle.startAnimation(needlePlay);
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

    @OnClick(R.id.btn_favorite)
    public void collect(View view) {
        if (currentMusic != null) {
            if (isCollected) {
                DBManager.cancelCollect(currentMusic.getSongId());
                ToastUtil.show(context, "取消收藏");
                isCollected = false;
            } else {
                DBManager.collectMusic(currentMusic);
                ToastUtil.show(context, "收藏成功");
                isCollected = true;
            }
            Intent update = new Intent(I.BroadCast.UPDATE_LIST);
            broadcastManager.sendBroadcast(update);
            btnFavorite.setImageResource(isCollected ? R.drawable.favorite_selected_on : R.drawable.favorite_selected_off);
        }
    }
}
