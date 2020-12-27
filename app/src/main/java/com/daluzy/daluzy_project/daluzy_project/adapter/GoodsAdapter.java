package com.daluzy.daluzy_project.daluzy_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.activity.PddGoodsDetailsActivity;
import com.daluzy.daluzy_project.daluzy_project.tools.ImageLoadTool;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private Context context;
    private List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = new ArrayList<>();
    public GoodsAdapter(Context context,List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void addAllData(List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataListUpdate){
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
        PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem item = dataList.get(position);
        ImageLoadTool.imageLoad(context,item.getGoodsThumbnailUrl(),viewHolder.ivGoodsImage);
        viewHolder.tvGoodsName.setText(item.getGoodsName());
        viewHolder.tvGoodsDes.setText(item.getGoodsDesc());
        String coupon="";
        if (item.getCouponDiscount() != null){
            coupon = String.valueOf(item.getCouponDiscount()/100);
        }
        viewHolder.tvGoodsCoupon.setText(String.format("可领 %s元 优惠券", coupon));
        viewHolder.tvGoodsSales.setText(item.getSalesTip());
        String endTime = "";
        if (item.getCouponEndTime() != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
            Date date = new Date(item.getCouponEndTime() * 1000);
            endTime = simpleDateFormat.format(date);
        }
        viewHolder.tvGoodsEndTime.setText(String.format("截至日期：%s", endTime));

      viewHolder.itemView.setOnClickListener(v -> {
          Intent intent = new Intent(context, PddGoodsDetailsActivity.class);
          intent.putExtra("goodId",item.getGoodsId());
          intent.putExtra("searchId",item.getSearchId());
          context.startActivity(intent);
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
