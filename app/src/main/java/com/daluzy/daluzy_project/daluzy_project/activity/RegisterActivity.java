package com.daluzy.daluzy_project.daluzy_project.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.domain.User;
import com.daluzy.daluzy_project.daluzy_project.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_email)
    EditText etUserEmail;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.et_user_password_again)
    EditText etUserPasswordAgain;
    @BindView(R.id.tv_register_btn)
    TextView tvRegisterBtn;

    @Override
    public void initView() {
        contentView(R.layout.register_page_daluzy);
        ButterKnife.bind(this);
        setTitle("注册");
        showBackImage();
        tvRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }
    /**
     * 账号密码注册
     */
    private void signUp() {
        String userName = etUserName.getText().toString().trim();
        String userPhone = etUserPhone.getText().toString().trim();
        String userEmail = etUserEmail.getText().toString().trim();
        String userPassword = etUserPassword.getText().toString().trim();
        String userPasswordAgain = etUserPasswordAgain.getText().toString().trim();
        if (userName.isEmpty()){
            ToastUtils.showTextToast(context,"请输入用户名称");
            return;
        }
        if (userPhone.isEmpty()){
            ToastUtils.showTextToast(context,"请输入用户电话号码");
            return;
        }
        if (userEmail.isEmpty()){
            ToastUtils.showTextToast(context,"请输入用户邮箱");
            return;
        }
        if (userPassword.isEmpty()){
            ToastUtils.showTextToast(context,"请输入用户密码");
            return;
        }
        if (!userPassword.equals(userPasswordAgain)){
            ToastUtils.showTextToast(context,"两次密码输入不一致");
            return;
        }
        final User user = new User();
        user.setUsername(userName);
        user.setPassword(userPassword);
        user.setEmail(userEmail);
        user.setMobilePhoneNumber(userPhone);
        user.setAge(18);
        user.setGender(0);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    ToastUtils.showTextToast(context,"注册成功");
                } else {
                    ToastUtils.showTextToast(context,e.getMessage());
                }
            }
        });
    }
}
