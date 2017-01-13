package cn.ucai.ttmusic.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.model.bean.Singer;
import cn.ucai.ttmusic.controller.activity.MainActivity;
import cn.ucai.ttmusic.controller.adapter.ItemClickListener;
import cn.ucai.ttmusic.controller.adapter.MusicAdapter;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.music.LocalMusicModel;

public class Fragment_MusicsOfSinger extends BaseFragment implements ItemClickListener {

    Context mContext;
    MainActivity mainActivity;

    @BindView(R.id.singer_music_toolbar)
    Toolbar toolbar;
    @BindView(R.id.singer_music_rv)
    RecyclerView singer_music_rv;

    Singer singer;
    List<Music> musicsOfSinger;
    MusicAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_musics_of_singer, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void initUi() {
        mainActivity = (MainActivity) getActivity();
        mContext = getActivity();
        singer = (Singer) getArguments().getSerializable("singer");
        //初始化工具栏
        initToolbar();
        //初始化列表
        initList();
    }

    private void initToolbar() {
        toolbar.setTitle(singer.getSingerName());
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);
    }

    private void initList() {
        if (singer == null) {
            return;
        }
        musicsOfSinger = LocalMusicModel.getMusicListBySinger(TTApplication.getInstance().getMusicList(), singer.getSingerName());
        adapter = new MusicAdapter(mContext, musicsOfSinger);
        adapter.setListener(this);
        singer_music_rv.setAdapter(adapter);
        singer_music_rv.setLayoutManager(new LinearLayoutManager(mContext));
        singer_music_rv.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(I.BroadCast.MUSIC_INIT);
        Bundle data = new Bundle();
        data.putSerializable(I.BroadCast.MUSIC_LIST, (Serializable) musicsOfSinger);
        data.putInt(I.BroadCast.MUSIC_POSITION, position);
        intent.putExtras(data);
        mContext.sendBroadcast(intent);
    }

}
