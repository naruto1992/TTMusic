package cn.ucai.ttmusic.controller.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.ucai.ttmusic.controller.fragment.PlayViewFragment;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.Music;


public class PlayViewPagerAdapter extends FragmentPagerAdapter {

    List<Music> musicList;

    public PlayViewPagerAdapter(FragmentManager fm, List<Music> musicList) {
        super(fm);
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList == null ? 0 : musicList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Music music = musicList.get(position);
        PlayViewFragment fragment = new PlayViewFragment();
        Bundle data = new Bundle();
        data.putSerializable(I.Intent.MUSIC, music);
        fragment.setArguments(data);
        return fragment;
    }
}
