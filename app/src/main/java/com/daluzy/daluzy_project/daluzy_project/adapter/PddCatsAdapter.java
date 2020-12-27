package com.daluzy.daluzy_project.daluzy_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.domain.PddGoodsCat;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PddCatsAdapter extends RecyclerView.Adapter<PddCatsAdapter.ViewHolder> {

    private Context context;
    private PddCatsOnClickListener pddCatsOnClickListener;
    private List<PddGoodsCat> dataList = new ArrayList<>();

    private int lastIndex = 0;

    public void setPddCatsOnClickListener(PddCatsOnClickListener pddCatsOnClickListener) {
        this.pddCatsOnClickListener = pddCatsOnClickListener;
    }

    public PddCatsAdapter(Context context, List<PddGoodsCat> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cats_item_daluzy, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PddGoodsCat cat = dataList.get(position);
        if (cat.isSelected()) {
            viewHolder.llLine.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llLine.setVisibility(View.INVISIBLE);
        }
        viewHolder.tvCatName.setText(cat.getCat_name());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pddCatsOnClickListener.catOnClick(position, cat);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_cat_name)
        TextView tvCatName;
        @BindView(R.id.ll_line)
        LinearLayout llLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateItem(int position) {
        PddGoodsCat pddGoodsCat = dataList.get(lastIndex);
        pddGoodsCat.setSelected(false);
        notifyItemChanged(lastIndex,pddGoodsCat);

        PddGoodsCat pddGoodsCatNow = dataList.get(position);
        pddGoodsCatNow.setSelected(true);
        notifyItemChanged(position,pddGoodsCatNow);
        lastIndex = position;
    }

    public interface PddCatsOnClickListener {
        void catOnClick(int position, PddGoodsCat pddGoodsCat);
    }
}
