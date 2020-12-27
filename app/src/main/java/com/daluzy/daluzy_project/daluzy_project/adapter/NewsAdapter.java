package com.daluzy.daluzy_project.daluzy_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.activity.WebViewActivity;
import com.daluzy.daluzy_project.daluzy_project.domain.News;
import com.daluzy.daluzy_project.daluzy_project.tools.ImageLoadTool;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<News> dataList = new ArrayList<>();
    public NewsAdapter(Context context, List<News> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void addAllData(List<News> dataListUpdate){
        if (dataListUpdate != null && dataListUpdate.size()>0){
            dataList.addAll(dataListUpdate);
            notifyItemRangeChanged(dataList.size() -dataListUpdate.size(),dataListUpdate.size());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_item_daluzy,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        News item = dataList.get(position);
        ImageLoadTool.imageLoad(context,item.getThumbnail_pic_s(),viewHolder.ivGoodsImage);
        viewHolder.tvGoodsName.setText(item.getTitle());
        viewHolder.tvGoodsDes.setText(item.getAuthor_name());

        viewHolder.tvGoodsCoupon.setText(item.getCategory());
        viewHolder.tvGoodsSales.setText("");

        viewHolder.tvGoodsEndTime.setText(item.getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",item.getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivGoodsImage;
        private TextView tvGoodsName;
        private TextView tvGoodsDes;
        private TextView tvGoodsCoupon;
        private TextView tvGoodsSales;
        private TextView tvGoodsEndTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGoodsImage = itemView.findViewById(R.id.iv_goods_image);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsDes = itemView.findViewById(R.id.tv_goods_des);
            tvGoodsCoupon = itemView.findViewById(R.id.tv_goods_coupon);
            tvGoodsSales = itemView.findViewById(R.id.tv_goods_sales);
            tvGoodsEndTime = itemView.findViewById(R.id.tv_goods_end_time);
        }
    }
}
