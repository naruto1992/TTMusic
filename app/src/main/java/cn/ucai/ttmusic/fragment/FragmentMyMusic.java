package cn.ucai.ttmusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.activity.MainActivity;
import cn.ucai.ttmusic.adapter.MyMusicListAdapter;
import cn.ucai.ttmusic.bean.MusicListBean;
import cn.ucai.ttmusic.db.DBManager;
import cn.ucai.ttmusic.db.Music;
import cn.ucai.ttmusic.utils.ToastUtil;

public class FragmentMyMusic extends BaseFragment {

    Context mContext;
    @BindView(R.id.list_collect)
    ExpandableListView myMusicList;

    Map<String, List<MusicListBean>> list = new HashMap<>();
    List<MusicListBean> myList = new ArrayList<>();
    MyMusicListAdapter adapter;

    List<Music> favorites;
    LocalBroadcastManager broadcastManager;
    BroadcastReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_music, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void initData() {
        mContext = getActivity();
        broadcastManager = LocalBroadcastManager.getInstance(mContext);

        favorites = DBManager.getCollects();
        MusicListBean favorite = new MusicListBean();
        favorite.setList_title("我喜欢的音乐");
        favorite.setMusicList(favorites);

        myList.add(favorite);
        list.put(I.MyMusicList.CREATED_LIST, myList);

        list.put(I.MyMusicList.COLLECTD_LIST, new ArrayList<MusicListBean>());
    }

    @Override
    public void initView() {
        myMusicList.setGroupIndicator(null);
        adapter = new MyMusicListAdapter(mContext, list);
        myMusicList.setAdapter(adapter);
        //默认全部展开
        myMusicList.expandGroup(0);
        myMusicList.expandGroup(1);
        //
        initBroadCast();
    }

    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(I.BroadCast.UPDATE_COLLECT);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case I.BroadCast.UPDATE_COLLECT:
                        favorites = DBManager.getCollects();
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }


    @OnClick({R.id.list_localMusic, R.id.list_recents, R.id.list_downloads, R.id.list_my_singers, R.id.list_my_mv})
    public void onClick(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.list_localMusic:
                mainActivity.goToFragment(new FragmentLocalMusic());
                break;
            case R.id.list_recents:
                ToastUtil.show(mContext, "最近播放(开发中)");
                break;
            case R.id.list_downloads:
                ToastUtil.show(mContext, "下载管理(开发中)");
                break;
            case R.id.list_my_singers:
                ToastUtil.show(mContext, "我的歌手(开发中)");
                break;
            case R.id.list_my_mv:
                ToastUtil.show(mContext, "我的MV(开发中)");
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            broadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
