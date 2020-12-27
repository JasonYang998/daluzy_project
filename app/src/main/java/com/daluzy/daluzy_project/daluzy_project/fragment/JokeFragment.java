package com.daluzy.daluzy_project.daluzy_project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.adapter.JokesAdapter;
import com.daluzy.daluzy_project.daluzy_project.domain.Joke;
import com.daluzy.daluzy_project.daluzy_project.domain.ResultData;
import com.daluzy.daluzy_project.daluzy_project.tools.JsonTool;
import com.daluzy.daluzy_project.daluzy_project.utils.HttpUtils;
import com.daluzy.daluzy_project.daluzy_project.utils.ToastUtils;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class JokeFragment extends Fragment {
    @BindView(R.id.joke_list)
    RecyclerView jokeList;
    @BindView(R.id.joke_srl)
    SmartRefreshLayout jokeSrl;
    private Context context;
    private View view;
    private int page  = 0;
    private int pageSize = 20;
    JokesAdapter jokesAdapter;
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
        view = inflater.inflate(R.layout.joke_fragment_daluzy, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        jokeList.setLayoutManager(new LinearLayoutManager(context));
        initData3();
        //加载
        jokeSrl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                initData3();
            }
        });
        //刷新
        jokeSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 0;
                initData3();
            }
        });

        return view;
    }

    private void initData3() {
        page ++;
        Map<String , Object> map = new HashMap<>();
        map.put("sort","asc:");
        map.put("page",page);
        map.put("pagesize",pageSize);
        map.put("time", System.currentTimeMillis()/1000);
        map.put("key","62fdcf3cc950992c95d0d4631e1e12dd");

//        HttpUtils.get("http://v.juhe.cn/joke/content/list.php", map, new HttpUtils.HttpCallBack() {
        HttpUtils.post("http://v.juhe.cn/joke/content/list.php", map, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(ResultData resultData) {
                if (resultData.getError_code() == 0){
                    JSONObject jsonObject = JSON.parseObject(resultData.getResult());
                    List<Joke> jokeLists = JsonTool.jsonToList(jsonObject.getString("data"),Joke.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jokeLists != null){
                                if (page == 1){
                                    jokesAdapter = new JokesAdapter(context,jokeLists);
                                    jokeList.setAdapter(jokesAdapter);
                                }else {
                                    if (jokesAdapter!=null){
                                        jokesAdapter.addAllData(jokeLists);
                                    }
                                }
                                if (jokeLists.size()<pageSize){
                                    jokeSrl.setEnableLoadMore(false);
                                }
                            }

                        }
                    });
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showTextToast(context,resultData.getReason());
                        }
                    });
                }
                jokeSrl.finishLoadMore();
                jokeSrl.finishRefresh();
            }

            @Override
            public void onFail(String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast(context,error);
                    }
                });
                jokeSrl.finishLoadMore();
                jokeSrl.finishRefresh();
            }
        });
    }

    private void initData2() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("sort","asc:")
                .add("page","1")
                .add("pagesize","20")
                .add("time", String.valueOf(System.currentTimeMillis()/1000))
                .add("key","62fdcf3cc950992c95d0d4631e1e12dd")
                .build();

        Request request = new Request.Builder()
                .url("http://v.juhe.cn/joke/content/list.php")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String error = e.getMessage();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                    JSONObject jsonObject = JSON.parseObject(data);
                    JSONObject jsonObject2 = JSON.parseObject(jsonObject.getString("result"));
                    List<Joke> jokeLists = JsonTool.jsonToList(jsonObject2.getString("data"),Joke.class);
                    getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JokesAdapter jokesAdapter = new JokesAdapter(context,jokeLists);
                        jokeList.setAdapter(jokesAdapter);
                    }
                });


            }
        });
    }

    private void initData() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .get()
                .url("http://v.juhe.cn/joke/content/list.php?sort=asc%3A&page=1&pagesize=20&time=1518816972&key=62fdcf3cc950992c95d0d4631e1e12dd")
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String error = e.getMessage();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject jsonObject2 = JSON.parseObject(jsonObject.getString("result"));
                List<Joke> jokeLists = JsonTool.jsonToList(jsonObject2.getString("data"),Joke.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JokesAdapter jokesAdapter = new JokesAdapter(context,jokeLists);
                        jokeList.setAdapter(jokesAdapter);
                    }
                });


            }
        });

    }


}
