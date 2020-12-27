package com.daluzy.daluzy_project.daluzy_project.activity;

import android.content.Intent;
import android.widget.GridView;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.adapter.AllCatsAdapter;
import com.daluzy.daluzy_project.daluzy_project.domain.PddGoodsCat;
import com.donkingliang.labels.LabelsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PddCatsActivity extends BaseActivity {
    @BindView(R.id.cats_gv_list)
    GridView catsGvList;
    @BindView(R.id.cats_labels_list)
    LabelsView catsLabelsList;
    private List<PddGoodsCat> catList = new ArrayList<>();
    private AllCatsAdapter allCatsAdapter;

    @Override
    public void initView() {
        contentView(R.layout.pdd_cats_page_daluzy);
        ButterKnife.bind(this);
        //设置标题
        setTitle("所有分类");
        //显示返回键
        showBackImage();
        catList = (List<PddGoodsCat>) getIntent().getSerializableExtra("cats");
       /* allCatsAdapter = new AllCatsAdapter(context, catList);
        catsGvList.setAdapter(allCatsAdapter);*/

        catsLabelsList.setLabels(catList, new LabelsView.LabelTextProvider<PddGoodsCat>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, PddGoodsCat data) {
                return data.getCat_name();
            }
        });

        //标签的点击监听
        catsLabelsList.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                Intent intent = new Intent(context, PddGoodsListActivity.class);
                PddGoodsCat goodsCat = (PddGoodsCat) data;
                intent.putExtra("cat",goodsCat);
                startActivity(intent);
            }
        });
    }

}
