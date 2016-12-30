package cn.ucai.ttmusic.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ucai.ttmusic.R;

public class FragmentFriendsMusic extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_friends_music, container, false);
        return layout;
    }

    @Override
    public void initUi() {

    }

}
