package com.daluzy.daluzy_project.daluzy_project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.domain.PddGoodsCat;

import java.util.ArrayList;
import java.util.List;

public class AllCatsAdapter extends BaseAdapter {
    private Context context;
    private List<PddGoodsCat> dataList = new ArrayList<>();
    public AllCatsAdapter(Context context,List<PddGoodsCat> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(context,R.layout.cats_item_daluzy,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(dataList.get(position).getCat_name());

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        public ViewHolder(View view) {
            textView = view .findViewById(R.id.tv_cat_name);
        }
    }
}
