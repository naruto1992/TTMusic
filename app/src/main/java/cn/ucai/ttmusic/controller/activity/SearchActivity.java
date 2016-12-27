package cn.ucai.ttmusic.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.controller.adapter.MusicAdapter;
import cn.ucai.ttmusic.model.I;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.utils.InputUtil;
import cn.ucai.ttmusic.model.utils.ToastUtil;

public class SearchActivity extends BaseActivity implements MusicAdapter.ItemClickListener {

    Context mContext;
    @BindView(R.id.search_music_toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_searchMusic)
    EditText etSearchMusic;
    @BindView(R.id.rv_searchMusic)
    RecyclerView rv_searchMusic;

    int searchType;
    List<Music> musicData;

    MusicAdapter adapter;
    List<Music> musicResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        initToolbar();
        adapter = new MusicAdapter(mContext, musicResult);
        adapter.setListener(this);
        rv_searchMusic.setAdapter(adapter);
        rv_searchMusic.setLayoutManager(new LinearLayoutManager(mContext));
        rv_searchMusic.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL));
        initEditText();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initEditText() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            searchType = data.getInt(I.Intent.SEARCH_TYPE, I.SearchType.MUSIC_LOCAL);
            if (searchType == I.SearchType.MUSIC_LOCAL) {
                etSearchMusic.setHint("搜索本地歌曲");
            } else {
                etSearchMusic.setHint("搜索歌单内歌曲");
            }
            musicData = (List<Music>) data.getSerializable(I.Intent.SEARCH_DATA);
            etSearchMusic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        getAndSetResult(textView.getText().toString().trim());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void getAndSetResult(String str) {
        if (musicData == null || musicData.size() == 0) {
            ToastUtil.show(mContext, "当前搜索来源为空");
            InputUtil.hideSoftInput(etSearchMusic);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            ToastUtil.show(mContext, "请输入搜索关键词");
            InputUtil.hideSoftInput(etSearchMusic);
            return;
        }
        if (musicResult != null) {
            musicResult.clear();
        }
        for (Music music : musicData) {
            if (music.getTitle().contains(str) || music.getSinger().contains(str)
                    || music.getAlbum().contains(str)) {
                musicResult.add(music);
            }
        }
        if (musicResult.size() == 0) {
            ToastUtil.show(mContext, "未搜索到相关歌曲");
        } else {
            adapter.notifyDataSetChanged();
        }
        InputUtil.hideSoftInput(etSearchMusic);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(I.BroadCast.MUSIC_INIT);
        Bundle data = new Bundle();
        data.putSerializable(I.BroadCast.MUSIC_LIST, (Serializable) musicResult);
        data.putInt(I.BroadCast.MUSIC_POSITION, position);
        intent.putExtras(data);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
