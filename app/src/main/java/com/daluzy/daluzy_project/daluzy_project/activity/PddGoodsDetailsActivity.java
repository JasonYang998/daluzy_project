package com.daluzy.daluzy_project.daluzy_project.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.adapter.BannerImageAdapter;
import com.daluzy.daluzy_project.daluzy_project.contants.Constants;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsPidQueryRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsPromotionUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsPidQueryResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PddGoodsDetailsActivity extends BaseActivity {
    @BindView(R.id.goods_details_banner)
    Banner goodsDetailsBanner;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_des)
    TextView tvGoodsDes;
    @BindView(R.id.viewPager)
    ViewPager2 viewPager2;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private Long goodsId;
    private String searchId;
    private String longUrl;
    private String shortUrl;
    private boolean isScrollViewPager;

    PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem goodsDetailsItem;
    private List<PddDdkGoodsPidQueryResponse.PIdQueryResponsePIdListItem> pidList;
    private Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper())) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            viewPager2.setCurrentItem(msg.what + 1);
        }
    };

    @Override
    public void initView() {
        contentView(R.layout.pdd_goods_details_page_daluzy);
        ButterKnife.bind(this);
        setTitle("商品详情");
        goodsId = getIntent().getLongExtra("goodId", 0);
        searchId = getIntent().getStringExtra("searchId");
        showBackImage();
        initData();
//        createPromotionUrl();
        searchPromotionUrl();
    }

    @OnClick({R.id.tv_commit})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_commit:
                if (shortUrl!=null && !shortUrl.isEmpty()){
                    //唤起拼多多App
                    Uri uri = Uri.parse(shortUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setPackage("com.xunmeng.pinduoduo");
                    startActivity(intent);

                }
                break;
        }
    }

    /**
     * 查询产品数据
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientId = Constants.PDD_CLIENT_ID;
                String clientSecret = Constants.PDD_CLIENT_SECRET;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
                List<Long> goodsIdList = new ArrayList<Long>();
                goodsIdList.add(goodsId);
                request.setGoodsIdList(goodsIdList);
                request.setSearchId(searchId);
                PddDdkGoodsDetailResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                assert response != null;
                String data = JsonUtil.transferToJson(response);
                goodsDetailsItem = response.getGoodsDetailResponse().getGoodsDetails().get(0);
                runOnUiThread(new Runnable() {
                    @SuppressLint({"DefaultLocale", "ClickableViewAccessibility"})
                    @Override
                    public void run() {
                        //.addBannerLifecycleObserver((LifecycleOwner) PddGoodsDetailsActivity.this)//添加生命周期观察者
                        createPromotionUrl();
                        goodsDetailsBanner.setAdapter(new BannerImageAdapter(context, goodsDetailsItem.getGoodsGalleryUrls()))
                                .setIndicator(new CircleIndicator(context))
                                .setOrientation(Banner.HORIZONTAL)
                                .start();

                        tvGoodsName.setText(goodsDetailsItem.getGoodsName());
                        double a = goodsDetailsItem.getMinNormalPrice();
                        double d = a / 100;
                        tvGoodsPrice.setText(String.format("¥%.2f", d));
                        tvGoodsDes.setText(goodsDetailsItem.getGoodsDesc());
                       /* ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(context,goodsDetailsItem.getGoodsGalleryUrls());
                        viewPager2.setAdapter(viewPager2Adapter);
                        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);*/
/*
                        ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                            }

                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                handler.sendEmptyMessageDelayed(position,2000);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                super.onPageScrollStateChanged(state);
                            }
                        };
                        viewPager2.registerOnPageChangeCallback(callback);*/
                    }
                });


            }
        }).start();
    }

    /**
     * 生成推广链接
     */
    private void createPromotionUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientId = Constants.PDD_CLIENT_ID;
                String clientSecret = Constants.PDD_CLIENT_SECRET;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
                request.setCustomParameters("str");
                request.setGenerateMallCollectCoupon(false);
                request.setGenerateQqApp(false);
                request.setGenerateSchemaUrl(true);
                request.setGenerateShortUrl(true);
                request.setGenerateWeappWebview(false);
                request.setGenerateWeiboappWebview(false);
                request.setGenerateWeApp(false);
                List<Long> goodsIdList = new ArrayList<Long>();
                goodsIdList.add(goodsDetailsItem.getGoodsId());
                request.setGoodsIdList(goodsIdList);
                request.setMultiGroup(false);
                request.setPId(pidList.get(0).getPId());
                request.setSearchId(searchId);
                PddDdkGoodsPromotionUrlGenerateResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String data = JsonUtil.transferToJson(response);
                longUrl = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0).getMobileUrl();
//                shortUrl = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0).getUrl();
                shortUrl = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0).getSchemaUrl();
                System.out.println(JsonUtil.transferToJson(response));
            }
        }).start();

    }

    /**
     * 获取推广位
     */
    public void searchPromotionUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientId = Constants.PDD_CLIENT_ID;
                String clientSecret = Constants.PDD_CLIENT_SECRET;
                PopClient client = new PopHttpClient(clientId, clientSecret);

                PddDdkGoodsPidQueryRequest request = new PddDdkGoodsPidQueryRequest();
                request.setPage(1);
                request.setPageSize(10);
                PddDdkGoodsPidQueryResponse response = null;
                try {
                    response = client.syncInvoke(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pidList = response.getPIdQueryResponse().getPIdList();
            }
        }).start();

    }

}
