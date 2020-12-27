package com.daluzy.daluzy_project.daluzy_project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.domain.User;
import com.daluzy.daluzy_project.daluzy_project.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.util.V;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.tv_login_btn)
    TextView tvLoginBtn;

    private SharedPreferences sharedPreferences;
    @Override
    public void initView() {
        contentView(R.layout.login_page_daluzy);
        ButterKnife.bind(this);
        setTitle("登录");
        showBackImage();
        sharedPreferences = getSharedPreferences("token",MODE_PRIVATE);
        setRightText1("注册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,RegisterActivity.class));
            }
        });

        tvLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login(){
        String userAccount = etUserName.getText().toString().trim();
        String userPassword = etUserPassword.getText().toString().trim();

        if (userAccount.isEmpty()){
            ToastUtils.showTextToast(context,"请输入用户账号");
            return;
        }

        if (userPassword.isEmpty()){
            ToastUtils.showTextToast(context,"请输入用户密码");
            return;
        }

        BmobUser.loginByAccount(userAccount, userPassword, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token",user.getSessionToken());
                    editor.apply();
                    ToastUtils.showTextToast(context,"登陆成功");
                    startActivity(new Intent(context,NewMainActivity.class));

                }   else {
                    ToastUtils.showTextToast(context,e.getMessage());
                }
            }
        });
    }

}
