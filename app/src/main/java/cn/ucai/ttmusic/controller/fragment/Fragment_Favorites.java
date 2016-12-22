package cn.ucai.ttmusic.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.activity.MainActivity;
import cn.ucai.ttmusic.controller.adapter.MusicAdapter;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.DBManager;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.utils.ToastUtil;

public class Fragment_Favorites extends BaseFragment implements MusicAdapter.ItemClickListener {

    Context mContext;
    MainActivity mainActivity;
    LocalBroadcastManager broadcastManager;
    BroadcastReceiver mReceiver;

    @BindView(R.id.favorites_music_toolbar)
    Toolbar toolbar;
    @BindView(R.id.favorites_music_rv)
    RecyclerView favorites_music_rv;

    List<Music> favorites;
    MusicAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void initUi() {
        mainActivity = (MainActivity) getActivity();
        mContext = getActivity();
        broadcastManager = LocalBroadcastManager.getInstance(TTApplication.getContext());
        //初始化工具栏
        initToolbar();
        //初始化列表
        initList();
        //初始化广播
        initBroadCast();
    }

    private void initToolbar() {
        toolbar.setTitle("我喜欢的音乐");
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

    private void initList() {
        favorites = DBManager.getCollects();
        adapter = new MusicAdapter(mContext, favorites);
        adapter.setListener(this);
        favorites_music_rv.setAdapter(adapter);
        favorites_music_rv.setLayoutManager(new LinearLayoutManager(mContext));
        favorites_music_rv.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
    }

    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.BroadCast.UPDATE_LIST);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.BroadCast.UPDATE_LIST:
                        initList();
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(I.BroadCast.MUSIC_INIT);
        Bundle data = new Bundle();
        data.putSerializable(I.BroadCast.MUSIC_LIST, (Serializable) favorites);
        data.putInt(I.BroadCast.MUSIC_POSITION, position);
        intent.putExtras(data);
        mContext.sendBroadcast(intent);
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
