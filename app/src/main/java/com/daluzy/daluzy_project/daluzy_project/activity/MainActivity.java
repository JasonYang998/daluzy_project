package com.daluzy.daluzy_project.daluzy_project.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.adapter.GoodsAdapter;
import com.daluzy.daluzy_project.daluzy_project.adapter.NewsAdapter;
import com.daluzy.daluzy_project.daluzy_project.contants.Constants;
import com.daluzy.daluzy_project.daluzy_project.domain.NewsResultData;
import com.daluzy.daluzy_project.daluzy_project.tools.HttpTools;
import com.daluzy.daluzy_project.daluzy_project.tools.JsonTool;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {
    private RecyclerView rvList;
    private GoodsAdapter goodsAdapter;
    private SmartRefreshLayout refreshLayout;
    private int page = 0;
    private int pageSize = 30;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> dataList = (List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem>) msg.obj;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_daluzy);
        rvList = this.findViewById(R.id.rv_list);
        refreshLayout = this.findViewById(R.id.srl);
        /**
         * 下拉刷新
         */
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            getData();
        });
        /**
         * 上拉加载
         */
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            getData();
        });

        rvList.setLayoutManager(new LinearLayoutManager(this));
//        getData();
        getNewsData();
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page++;
                String clientId = Constants.PDD_CLIENT_ID;
                String clientSecret = Constants.PDD_CLIENT_SECRET;
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
               /* Message message = new Message();
                message.what = 1;
                message.obj = dataList;
                handler.sendMessage(message);
*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dataList != null && dataList.size() > 0) {
                            if (page == 1) {
                                goodsAdapter = new GoodsAdapter(MainActivity.this, dataList);
                                rvList.setAdapter(goodsAdapter);
                            } else {
                                if (goodsAdapter != null) {
                                    goodsAdapter.addAllData(dataList);
                                }
                            }
                            if (dataList.size() < pageSize) {
                                refreshLayout.setEnableLoadMore(false);
                            }
                        } else {
                            refreshLayout.setEnableLoadMore(false);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                });
            }
        }).start();
    }

    public void getNewsData(){
        HttpTools.postData("http://v.juhe.cn/toutiao/index", new HttpTools.HttpBackListener() {
            @Override
            public void onSuccess(String data, int code) {
                try {
                    //1 fastJson 做json解析
                    //2， webView 简单使用

                    NewsResultData newsResultData = JsonTool.jsonToObject(data, NewsResultData.class);

                    if (newsResultData != null && newsResultData.getError_code() == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                assert newsResultData != null;
                                NewsAdapter goodsAdapter = new NewsAdapter(MainActivity.this, newsResultData.getResult().getData());
                                rvList.setAdapter(goodsAdapter);
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (newsResultData != null){
                                    Toast.makeText(MainActivity.this,newsResultData.getReason(),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MainActivity.this, R.string.data_error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                 /*   JSONObject jsonObject = new JSONObject(data);
                    String result = jsonObject.getString("result");

                    JSONObject jsonObject2 = new JSONObject(result);
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");
                    List<News> news = new ArrayList<>();
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        News news1 = new News();
                        news1.setUniquekey(jsonObject3.getString("uniquekey"));
                        news1.setTitle(jsonObject3.getString("title"));
                        news1.setDate(jsonObject3.getString("date"));
                        news1.setCategory(jsonObject3.getString("category"));
                        news1.setAuthor_name(jsonObject3.getString("author_name"));
                        news1.setUrl(jsonObject3.getString("url"));
                        news1.setThumbnail_pic_s(jsonObject3.getString("thumbnail_pic_s"));
//                        news1.setThumbnail_pic_s02(jsonObject3.getString("thumbnail_pic_s02"));
//                        news1.setThumbnail_pic_s03(jsonObject3.getString("thumbnail_pic_s03"));
                        news.add(news1);
                    }*/



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error != null && !error.isEmpty()){
                            Toast.makeText(MainActivity.this,error,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, R.string.data_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
