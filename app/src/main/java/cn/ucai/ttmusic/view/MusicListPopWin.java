package cn.ucai.ttmusic.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.TTApplication;
import cn.ucai.ttmusic.controller.interfaze.IMusicService;
import cn.ucai.ttmusic.model.db.Music;

public class MusicListPopWin extends PopupWindow {

    Context context;
    View view;
    IMusicService musicService;
    PopWinListener listener;
    PopWinListAdapter adapter;

    @BindView(R.id.popWinMusicList)
    ListView musicList;
    @BindView(R.id.popWinTitle)
    TextView popWinTitle;

    public MusicListPopWin(Context context) {
        this.context = context;
        this.musicService = TTApplication.getInstance().getMusicService();
        this.view = LayoutInflater.from(context).inflate(R.layout.layout_list_pop_win, null);
        ButterKnife.bind(this, view);
        //初始化列表
        final List<Music> list = musicService.getMusicList();
        adapter = new PopWinListAdapter(context, list);
        musicList.setAdapter(adapter);

        int position = musicService.getCurrentItemId();
        musicList.setSelection(position);
        adapter.setCurrent(position);
        //设置点击事件
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                musicService.playMusic(i);
                updateList();
                if (listener != null) {
                    listener.onItemClick();
                }
            }
        });
        //初始化标题
        popWinTitle.setText("播放列表" + " (" + list.size() + ")");

        // 设置外部可点击
        this.setOutsideTouchable(true);
        //添加触摸监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        // 设置视图
        this.setContentView(view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popup_animation);
    }

    public void setListener(PopWinListener listener) {
        this.listener = listener;
    }

    public void updateList() {
        int position = musicService.getCurrentItemId();
        musicList.setSelection(position);
        adapter.setCurrent(position);
    }

    //清空
    @OnClick(R.id.popWinClear)
    public void popWinClear(View v) {
        if (listener != null) {
            listener.onClearAll();
        }
    }

    //收藏全部
    @OnClick(R.id.popWinCollectAll)
    public void popWinCollectAll(View v) {
        if (listener != null) {
            listener.onCollectAll();
        }
    }

    public interface PopWinListener {
        void onItemClick();

        void onCurrentDelete(int position);

        void onCollectAll();

        void onClearAll();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    class PopWinListAdapter extends BaseAdapter {

        Context context;
        List<Music> list;
        int mPosition;

        public PopWinListAdapter(Context context, List<Music> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        public void setCurrent(int position) {
            this.mPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ItemViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_pop_win_list, null);
                holder = new ItemViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ItemViewHolder) view.getTag();
            }
            Music music = list.get(i);
            holder.title.setText(music.getTitle() + " - " + music.getSinger());
            //设置当前项
            if (mPosition == i) {
                holder.title.setTextColor(Color.parseColor("#ff1E90FF"));
                holder.playing.setVisibility(View.VISIBLE);
            } else {
                holder.title.setTextColor(Color.parseColor("#000000"));
                holder.playing.setVisibility(View.GONE);
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //更新列表
                    list.remove(i);
                    notifyDataSetChanged();
                    popWinTitle.setText("播放列表" + " (" + list.size() + ")");
                    musicService.setMusicList(list);
                    TTApplication.getInstance().setMusicService(musicService);
                    //如果删除的是当前播放的，则先下一曲
                    if (i == mPosition) {
                        if (listener != null) {
                            listener.onCurrentDelete(i);
                        }
                    }
                }
            });
            return view;
        }
    }

    class ItemViewHolder {
        @BindView(R.id.pop_list_item_playing)
        ImageView playing;
        @BindView(R.id.pop_list_item_title)
        TextView title;
        @BindView(R.id.pop_list_item_delete)
        ImageView delete;

        public ItemViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
