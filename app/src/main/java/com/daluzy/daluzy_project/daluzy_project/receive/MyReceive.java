package com.daluzy.daluzy_project.daluzy_project.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daluzy.daluzy_project.daluzy_project.utils.ToastUtils;

public class MyReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String flag = intent.getStringExtra("flag");
        assert flag != null;
        switch (flag){
            case "refresh_user_info":
                ToastUtils.showTextToast(context,"刷新用户信息");
                break;

        }
    }
}
