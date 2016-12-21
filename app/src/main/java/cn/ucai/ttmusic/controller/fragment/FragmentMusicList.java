package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.adapter.LocalMusicListAdapter;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.controller.interfaze.ItemClickListener;

public class FragmentMusicList extends BaseFragment implements ItemClickListener {

    Context mContext;

    @BindView(R.id.local_music_rv)
    RecyclerView local_music_rv;

    List<Music> musicList;
    LocalMusicListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_music_list, container, false);
        ButterKnife.bind(this, layout);
        mContext = getContext();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initUi() {
        musicList = TTApplication.getInstance().getMusicList();
        adapter = new LocalMusicListAdapter(mContext, musicList);
        adapter.setListener(this);
        local_music_rv.setAdapter(adapter);
        local_music_rv.setLayoutManager(new LinearLayoutManager(mContext));
        local_music_rv.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
    }

    @Override
    public List<Music> getCurrentMusicList() {
        return musicList;
    }

    @Override
    public void onHeaderClick() {
        Intent intent = new Intent(I.BroadCast.MUSIC_INIT);
        Bundle data = new Bundle();
        data.putSerializable(I.BroadCast.MUSIC_LIST, (Serializable) musicList);
        data.putInt(I.BroadCast.MUSIC_POSITION, 0);
        intent.putExtras(data);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onItemClick(int position, Music music) {
        Intent intent = new Intent(I.BroadCast.MUSIC_INIT);
        Bundle data = new Bundle();
        data.putSerializable(I.BroadCast.MUSIC_LIST, (Serializable) musicList);
        data.putInt(I.BroadCast.MUSIC_POSITION, position);
        intent.putExtras(data);
        mContext.sendBroadcast(intent);
    }
}
