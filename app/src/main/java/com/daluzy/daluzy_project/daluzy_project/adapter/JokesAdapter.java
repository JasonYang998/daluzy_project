package com.daluzy.daluzy_project.daluzy_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.activity.PddGoodsDetailsActivity;
import com.daluzy.daluzy_project.daluzy_project.domain.Joke;
import com.daluzy.daluzy_project.daluzy_project.tools.ImageLoadTool;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.ViewHolder> {

    private Context context;
    private List<Joke> dataList = new ArrayList<>();

    public JokesAdapter(Context context, List<Joke> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    public void addAllData(List<Joke> dataList1){
        dataList.addAll(dataList1);
        notifyItemRangeChanged(dataList.size() -dataList1.size(),dataList1.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.joke_item_daluzy, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvJakeContent.setText(dataList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_jake_content)
        TextView tvJakeContent;
        @BindView(R.id.ll_line)
        LinearLayout llLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
