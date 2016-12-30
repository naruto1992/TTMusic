package cn.ucai.ttmusic.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.bean.Singer;
import cn.ucai.ttmusic.view.CharAvatarView;

public class SingerListAdapter extends RecyclerView.Adapter<SingerListAdapter.SingerHolder> {

    Context context;
    List<Singer> singers;
    LayoutInflater inflater;
    ItemClickListener listener;

    public SingerListAdapter(Context context, List<Singer> singers) {
        this.context = context;
        this.singers = singers;
        this.inflater = LayoutInflater.from(context);
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return singers == null ? 0 : singers.size();
    }

    @Override
    public SingerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_singer_list_item, null);
        return new SingerHolder(view);
    }

    @Override
    public void onBindViewHolder(SingerHolder holder, final int position) {
        Singer singer = singers.get(position);
        holder.avatar.setText(singer.getSingerName());
        holder.name.setText(singer.getSingerName());
        holder.count.setText(singer.getMusicCount() + "首");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    class SingerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.singerAvatar)
        CharAvatarView avatar;
        @BindView(R.id.singerName)
        TextView name;
        @BindView(R.id.singerMusicCount)
        TextView count;
        @BindView(R.id.singerMenu)
        ImageView menu;

        public SingerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
