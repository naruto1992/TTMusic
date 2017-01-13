package cn.ucai.ttmusic.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.model.bean.MusicListBean;

public class MusicListAdapter extends BaseAdapter {

    Context mContext;
    List<MusicListBean> list;
    LayoutInflater inflater;

    public MusicListAdapter(Context context, List<MusicListBean> list) {
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_music_list_item, null);
            holder = new ListViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ListViewHolder) view.getTag();
        }
        MusicListBean bean = list.get(i);
        holder.list_title.setText(bean.getList_title());
        holder.list_count.setText(bean.getMusicList().size() + "首");
        return view;
    }

    class ListViewHolder {
        @BindView(R.id.list_cover)
        ImageView list_cover;
        @BindView(R.id.list_title)
        TextView list_title;
        @BindView(R.id.list_count)
        TextView list_count;

        public ListViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
