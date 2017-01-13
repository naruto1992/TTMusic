package cn.ucai.ttmusic.controller.fragment;

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
import cn.ucai.ttmusic.model.bean.Singer;
import cn.ucai.ttmusic.controller.activity.MainActivity;
import cn.ucai.ttmusic.controller.adapter.ItemClickListener;
import cn.ucai.ttmusic.controller.adapter.SingerListAdapter;
import cn.ucai.ttmusic.model.music.LocalMusicModel;

public class Fragment_SingerList extends BaseFragment implements ItemClickListener {

    Context context;

    @BindView(R.id.singerList)
    RecyclerView singerList;

    List<Singer> singers;
    SingerListAdapter adapter;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_singer_list, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void initUi() {
        mainActivity = (MainActivity) getActivity();
        context = getActivity();
        singers = LocalMusicModel.getSingerList(TTApplication.getContext());
        adapter = new SingerListAdapter(context, singers);
        adapter.setListener(this);
        singerList.setAdapter(adapter);
        singerList.setLayoutManager(new LinearLayoutManager(context));
        singerList.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
    }

    @Override
    public void onItemClick(int position) {
        Fragment_MusicsOfSinger musicsOfSinger = new Fragment_MusicsOfSinger();
        Bundle data = new Bundle();
        data.putSerializable("singer", singers.get(position));
        musicsOfSinger.setArguments(data);
        mainActivity.goToFragment(musicsOfSinger);
    }
}
