package com.daluzy.daluzy_project.daluzy_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.activity.PddGoodsListActivity;
import com.daluzy.daluzy_project.daluzy_project.adapter.GoodsAdapter;
import com.daluzy.daluzy_project.daluzy_project.adapter.PddCatsIconAdapter;
import com.daluzy.daluzy_project.daluzy_project.contants.Constants;
import com.daluzy.daluzy_project.daluzy_project.domain.PddGoodsCat;
import com.daluzy.daluzy_project.daluzy_project.tools.CatData;
import com.daluzy.daluzy_project.daluzy_project.tools.JsonTool;
import com.daluzy.daluzy_project.daluzy_project.utils.MyGridLayoutManager;
import com.daluzy.daluzy_project.daluzy_project.utils.MyLinearLayoutManager;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PddGoodsFirstFragment extends Fragment {
    @BindView(R.id.shop_goods_list)
    RecyclerView shopGoodsList;
    @BindView(R.id.shop_srl)
    SmartRefreshLayout shopSrl;
    @BindView(R.id.shop_cat_list)
    RecyclerView shopCatList;
    private Context context;
    private GoodsAdapter goodsAdapter;
    private PddCatsIconAdapter catsIconAdapter;
    private View view;
    private int page = 0;
    private int pageSize = 30;

    private String clientId;
    private String clientSecret;
    private PddGoodsCat cat;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
                    if (dataList != null && dataList.size() > 0) {
                        if (page == 1) {
                            goodsAdapter = new GoodsAdapter(context, dataList);
                            shopGoodsList.setAdapter(goodsAdapter);
                        } else {
                            if (goodsAdapter != null) {
                                goodsAdapter.addAllData(dataList);
                            }
                        }
                        if (dataList.size() < pageSize) {
                            shopSrl.setEnableLoadMore(false);
                        }
                    } else {
                        shopSrl.setEnableLoadMore(false);
                    }
                    shopSrl.finishLoadMore();
                    shopSrl.finishRefresh();
                    break;

            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.pdd_goods_first_page_daluzy, container, false);
        ButterKnife.bind(this, view);

        context = getContext();
        clientId = Constants.PDD_CLIENT_ID;
        clientSecret = Constants.PDD_CLIENT_SECRET;
        /**
         * 下拉刷新
         */
        shopSrl.setOnRefreshListener(refreshLayout -> {
            page = 0;
            initData();
        });
        /**
         * 上拉加载
         */
        shopSrl.setOnLoadMoreListener(refreshLayout -> {
            initData();
        });

        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(context);
        linearLayoutManager.setvScroll(false);

        shopGoodsList.setLayoutManager(linearLayoutManager);



        List<PddGoodsCat> pddGoodsCats = JsonTool.jsonToList(CatData.getCatData(),PddGoodsCat.class);
        for (PddGoodsCat pddGoodsCat: pddGoodsCats){
            switch (pddGoodsCat.getCat_name()){
                case "男装":
                    pddGoodsCat.setIcon(R.mipmap.ison_mens);
                    break;
                case "手机":
                    pddGoodsCat.setIcon(R.mipmap.icon_cell_phone);
                    break;
                case "女装":
                    pddGoodsCat.setIcon(R.mipmap.icon_women_s);
                    break;
                case "女鞋":
                    pddGoodsCat.setIcon(R.mipmap.icon_women_shoes);
                    break;
                case "腕表眼镜":
                    pddGoodsCat.setIcon(R.mipmap.icon_watch_glasses);
                    break;
                case "童装":
                    pddGoodsCat.setIcon(R.mipmap.icon_children_wear);
                    break;
                case "箱包":
                    pddGoodsCat.setIcon(R.mipmap.icon_luggage);
                    break;
                case "居家日用":
                    pddGoodsCat.setIcon(R.mipmap.icon_daily_user);
                    break;
            }
        }
        MyGridLayoutManager gridLayoutManager =new MyGridLayoutManager(context,4);
        gridLayoutManager.setvScroll(false);
        gridLayoutManager.sethScroll(false);
        shopCatList.setLayoutManager(gridLayoutManager);

        catsIconAdapter = new PddCatsIconAdapter(context,pddGoodsCats);

        shopCatList.setAdapter(catsIconAdapter);
        initData();

        catsIconAdapter.setPddCatsOnClickListener(new PddCatsIconAdapter.PddCatsOnClickListener() {
            @Override
            public void catOnClick(int position, PddGoodsCat pddGoodsCat) {
                //跳转产品列表
                Intent intent = new Intent(context,PddGoodsListActivity.class);
                intent.putExtra("cat",pddGoodsCat);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
                request.setPage(page);
                request.setPageSize(pageSize);
                PddDdkGoodsSearchResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String data = JsonUtil.transferToJson(response);
                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = response.getGoodsSearchResponse().getGoodsList();
                Message message = new Message();
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);

            }
        }).start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
