package cn.ucai.ttmusic.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import cn.ucai.ttmusic.controller.fragment.PlayViewFragment;


public class PlayViewPagerAdapter extends FragmentPagerAdapter {

    List<PlayViewFragment> fragments;
    public PlayViewFragment currentFragment;

    public PlayViewPagerAdapter(FragmentManager fm, List<PlayViewFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.currentFragment = (PlayViewFragment) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

}
