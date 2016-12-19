package cn.ucai.ttmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.ucai.ttmusic.I;
import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.bean.MusicListBean;


public class MyMusicListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    LayoutInflater inflater;
    String[] listTitles = new String[]{I.MyMusicList.CREATED_LIST, I.MyMusicList.COLLECTD_LIST};
    Map<String, List<MusicListBean>> list;

    public MyMusicListAdapter(Context context, Map<String, List<MusicListBean>> list) {
        this.mContext = context;
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return listTitles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(listTitles[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTitles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(listTitles[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_group_list, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded) {
            holder.ivExpand.setImageResource(R.drawable.icon_list_opened);
        } else {
            holder.ivExpand.setImageResource(R.drawable.icon_list_closed);
        }
        String title = (String) getGroup(groupPosition);
        holder.tvTitle.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_child_list, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        MusicListBean bean = (MusicListBean) getChild(groupPosition, childPosition);
        holder.listTitle.setText(bean.getList_title());
        holder.listCount.setText(bean.getMusicList().size() + "首");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHolder {
        ImageView ivExpand;
        TextView tvTitle;
        ImageView ivMenu;

        public GroupHolder(View groupView) {
            ivExpand = (ImageView) groupView.findViewById(R.id.group_icon);
            tvTitle = (TextView) groupView.findViewById(R.id.group_title);
            ivMenu = (ImageView) groupView.findViewById(R.id.group_menu);
        }
    }

    class ChildHolder {
        ImageView listCover;
        TextView listTitle;
        TextView listCount;
        ImageView listMenu;

        public ChildHolder(View childView) {
            listCover = (ImageView) childView.findViewById(R.id.list_cover);
            listTitle = (TextView) childView.findViewById(R.id.list_title);
            listCount = (TextView) childView.findViewById(R.id.list_count);
            listMenu = (ImageView) childView.findViewById(R.id.list_menu);
        }
    }
}
