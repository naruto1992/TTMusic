package cn.ucai.ttmusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.activity.MainActivity;
import cn.ucai.ttmusic.utils.ToastUtil;

public class FragmentMyMusic extends BaseFragment {

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_music, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

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

}
