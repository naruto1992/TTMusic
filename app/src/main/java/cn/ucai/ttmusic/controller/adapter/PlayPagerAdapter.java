package cn.ucai.ttmusic.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import cn.ucai.ttmusic.controller.fragment.DiscoFragment;
import cn.ucai.ttmusic.controller.fragment.LrcFrament;


public class PlayPagerAdapter extends FragmentPagerAdapter {

    DiscoFragment discoFragment;
    LrcFrament lrcFrament;

    public PlayPagerAdapter(FragmentManager fm, DiscoFragment discoFragment, LrcFrament lrcFrament) {
        super(fm);
        this.discoFragment = discoFragment;
        this.lrcFrament = lrcFrament;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return discoFragment;
        } else {
            return lrcFrament;
        }
    }

}
