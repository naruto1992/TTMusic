package cn.ucai.ttmusic.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.adapter.MyPagerAdapter;

/**
 * Created by Administrator on 2016/12/1.
 */

public class FragmentLocalMusic extends BaseFragment {

    @BindView(R.id.local_music_tabs)
    TabLayout tabs;
    @BindView(R.id.local_music_pager)
    ViewPager localMusicPager;
    @BindView(R.id.local_music_fab)
    FloatingActionButton localMusicFab;

    List<Fragment> fragments;
    FragmentMusicList musicList;
    FragmentSingerList singerList;
    FragmentAlbumList albumList;
    FragmentFolderList folderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_local_music, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void initView() {
        initTabs();
    }

    private void initTabs() {
        List<String> titles = new ArrayList<>();
        titles.add("单曲");
        titles.add("歌手");
        titles.add("专辑");
        titles.add("文件夹");
        tabs.addTab(tabs.newTab().setText(titles.get(0)));
        tabs.addTab(tabs.newTab().setText(titles.get(1)));
        tabs.addTab(tabs.newTab().setText(titles.get(2)));
        tabs.addTab(tabs.newTab().setText(titles.get(3)));

        fragments = new ArrayList<>();
        musicList = new FragmentMusicList();
        singerList = new FragmentSingerList();
        albumList = new FragmentAlbumList();
        folderList = new FragmentFolderList();
        fragments.add(musicList);
        fragments.add(singerList);
        fragments.add(albumList);
        fragments.add(folderList);
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, titles);
        localMusicPager.setAdapter(adapter);
        tabs.setupWithViewPager(localMusicPager);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

}
