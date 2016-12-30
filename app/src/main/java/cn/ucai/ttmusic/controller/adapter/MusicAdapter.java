package cn.ucai.ttmusic.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.model.db.Music;

public class MusicAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context mContext;
    List<Music> musicList;

    static final int TYPE_HEADER = 0;
    static final int TYPE_ITEM = 1;

    ItemClickListener listener;

    public MusicAdapter(Context context, List<Music> musicList) {
        this.mContext = context;
        this.musicList = musicList;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return musicList.size() == 0 ? 0 : musicList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder = null;
        View layout = null;
        switch (viewType) {
            case TYPE_HEADER:
                layout = inflater.inflate(R.layout.header_music_list, null);
                holder = new Header(layout);
                break;
            case TYPE_ITEM:
                layout = inflater.inflate(R.layout.layout_local_music_item, null);
                holder = new ItemHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            Header header = (Header) holder;
            header.header_music_count.setText("(共" + musicList.size() + "首歌曲)");
        }
        if (getItemViewType(position) == TYPE_ITEM) {
            final Music music = musicList.get(position - 1);
            ItemHolder item = (ItemHolder) holder;
            item.musicName.setText(music.getTitle());
            item.singerName.setText(music.getSinger());
            item.itemView.setTag(position - 1);
        }
    }

    class ItemHolder extends ViewHolder {

        @BindView(R.id.music_item_musicName)
        TextView musicName;
        @BindView(R.id.music_item_singerName)
        TextView singerName;

        @OnClick(R.id.layout_music_item)
        public void click(View v) {
            int position = (int) v.getTag();
            if (listener != null) {
                listener.onItemClick(position);
            }
        }

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class Header extends ViewHolder {

        @BindView(R.id.header_music_count)
        TextView header_music_count;

        @OnClick(R.id.header_music_list)
        public void click(View v) {
            if (listener != null) {
                listener.onItemClick(0);
            }
        }

        public Header(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
