package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.controller.activity.MainActivity;
import cn.ucai.ttmusic.controller.adapter.MyPagerAdapter;
import cn.ucai.ttmusic.model.utils.ToastUtil;

public class FragmentLocalMusic extends BaseFragment {

    Context mContext;

    @BindView(R.id.local_music_toolbar)
    Toolbar toolbar;
    @BindView(R.id.local_music_tabs)
    TabLayout tabs;
    @BindView(R.id.local_music_pager)
    ViewPager localMusicPager;

    List<Fragment> fragments;
    FragmentMusicList musicList;
    FragmentSingerList singerList;
    FragmentAlbumList albumList;
    FragmentFolderList folderList;

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_local_music, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mContext = getActivity();
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("本地音乐");
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void initUi() {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    //不加这个方法，菜单图标显示不出来
    //利用反射原理修改属性和方法
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                ToastUtil.show(mContext, "搜索(开发中)");
                break;
            case R.id.menu_action1:
                ToastUtil.show(mContext, "功能1(开发中)");
                break;
            case R.id.menu_action2:
                ToastUtil.show(mContext, "功能2(开发中)");
                break;
            case R.id.menu_action3:
                ToastUtil.show(mContext, "功能3(开发中)");
                break;
        }
        return true;
    }
}
