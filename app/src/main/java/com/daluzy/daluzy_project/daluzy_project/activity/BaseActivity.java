package com.daluzy.daluzy_project.daluzy_project.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import butterknife.OnClick;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    ImageView backImg;
    TextView title;
    ImageView rightImg1;
    ImageView rightImg2;
    TextView rightText1;
    TextView rightText2;
    LinearLayout llBase;
    private LinearLayout llbase;
    private TextView titleTv, messageTv;
    private ImageView backImage, qrImage;
    private View contentView;
    private String titleStr;

    public Context context;

    public void setTitle(String titleStr) {
        this.titleStr = titleStr;
        if (titleTv != null) {
            titleTv.setText(titleStr);
        }
    }
    public void setRightText1(String text1,View.OnClickListener clickListener){
        rightText1.setVisibility(View.VISIBLE);
        rightText1.setText(text1);
        rightText1.setOnClickListener(clickListener);
    }

    public void showBackImage(){
        backImage.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_daluzy);
        context = this;
        titleTv =findViewById(R.id.title);
        messageTv = findViewById(R.id.right_text1);
        backImage = findViewById(R.id.back_img);
        qrImage = findViewById(R.id.right_img1);
        llbase = this.findViewById(R.id.ll_base);
        rightText1 = this.findViewById(R.id.right_text1);
        initView();
        backImage.setOnClickListener(this);

    }

    protected void contentView(int layoutId) {
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (contentView != null) {
            llbase.addView(contentView);
        }
    }

    public abstract void initView();

    @OnClick()
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img://返回键
                finish();
                break;
            case R.id.right_img1://扫一扫
                break;
            case R.id.right_text1://消息
                break;
            default://其他
                break;
        }
    }
}
