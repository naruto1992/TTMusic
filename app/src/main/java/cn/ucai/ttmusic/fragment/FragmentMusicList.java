package cn.ucai.ttmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.adapter.MusicListAdapter;
import cn.ucai.ttmusic.bean.Music;
import cn.ucai.ttmusic.interfaze.ItemClickListener;
import cn.ucai.ttmusic.utils.ToastUtil;

public class FragmentMusicList extends BaseFragment implements ItemClickListener {

    Context mContext;

    @BindView(R.id.local_music_rv)
    RecyclerView local_music_rv;

    List<Music> musicList;
    MusicListAdapter adapter;

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
    public void initData() {
        musicList = TTApplication.getInstance().getMusicList();
    }

    @Override
    public void initView() {
        adapter = new MusicListAdapter(mContext, musicList);
        adapter.setListener(this);
        local_music_rv.setAdapter(adapter);
        local_music_rv.setLayoutManager(new LinearLayoutManager(mContext));
        local_music_rv.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
    }

    @Override
    public void initListener() {

    }

    @Override
    public List<Music> getCurrentMusicList() {
        return musicList;
    }

    @Override
    public void onHeaderClick() {

    }

    @Override
    public void onItemClick(int position, Music music) {
        ToastUtil.show(mContext, music.toString());
    }
}
