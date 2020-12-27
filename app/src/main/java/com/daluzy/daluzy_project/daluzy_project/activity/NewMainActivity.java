package com.daluzy.daluzy_project.daluzy_project.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.daluzy.daluzy_project.daluzy_project.R;
import com.daluzy.daluzy_project.daluzy_project.adapter.ViewPagerAdapter;
import com.daluzy.daluzy_project.daluzy_project.fragment.JokeFragment;
import com.daluzy.daluzy_project.daluzy_project.fragment.MineFragment;
import com.daluzy.daluzy_project.daluzy_project.fragment.NewsFragment;
import com.daluzy.daluzy_project.daluzy_project.fragment.ShopFragment;
import com.daluzy.daluzy_project.daluzy_project.receive.MyReceive;
import com.daluzy.daluzy_project.daluzy_project.service.MyService;
import com.daluzy.daluzy_project.daluzy_project.utils.ToastUtils;
import com.daluzy.daluzy_project.daluzy_project.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMainActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.first_radio)
    RadioButton firstRadio;
    @BindView(R.id.type_radio)
    RadioButton typeRadio;
    @BindView(R.id.shopping_car_radio)
    RadioButton shoppingCarRadio;
    @BindView(R.id.mine_radio)
    RadioButton mineRadio;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    List<Fragment> fragmentList = new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;
    private ShopFragment shopFragment;
    private JokeFragment jokeFragment;
    private MineFragment mineFragment;
    private NewsFragment newsFragment;
    private MyReceive mainReceive;
    private ServiceConnection serviceConnection;
    public static String MAIN_BROADCAST = "com.daluzy.main_broadcast";
    String[] PermissionString = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private MyService myService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        contentView(R.layout.new_main_activity_daluzy);
        ButterKnife.bind(this);

        //启动Service
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("qin>>", "连接成功：" + name);
                MyService.MyBinder  myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("qin>>", "断开连接");
            }
        };
//        bindService(intent, serviceConnection, BIND_AUTO_CREATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //第 1 步: 检查是否有相应的权限
            boolean isAllGranted = checkPermissionAllGranted(PermissionString);
            if (!isAllGranted) {
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(this,
                        PermissionString, 1);
            }
        }

        initData();
        setTitle("优惠券商城");
        radioGroup.check(R.id.first_radio);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MAIN_BROADCAST);
        mainReceive = new MyReceive();
        registerReceiver(mainReceive, intentFilter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        radioGroup.check(R.id.first_radio);
                        break;
                    case 1:
                        radioGroup.check(R.id.type_radio);
                        break;
                    case 2:
                        radioGroup.check(R.id.shopping_car_radio);
                        break;
                    case 3:
                        radioGroup.check(R.id.mine_radio);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.first_radio:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.type_radio:
                        //停止MyService
//                        Intent intent = new Intent(context, MyService.class);
//                        stopService(intent);

                      /*  if (myService != null)//调用MYservice里面的方法
                            myService.longTimeDo();*/
                        //通过广播调用MYservice里面的方法
                        Intent intent1 = new Intent(MyService.MYSERVICE_BC);
                        intent1.putExtra("flag","myservice_bc");
                        sendBroadcast(intent1);

                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.shopping_car_radio:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.mine_radio:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });


//        List<ActivityManager.RunningAppProcessInfo> processes = ProcessManager.getRunningAppProcessInfo(ctx);

       /* ActivityManager myAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        assert myAM != null;
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  myAM.getRunningTasks(100);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =  myAM.getRunningAppProcesses();
        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList<ActivityManager.RunningServiceInfo>) myAM.getRunningServices(40);
        runningServices.size();

        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(serviceInfo.service.getPackageName(), serviceInfo.service.getClassName()));
                stopService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        ArrayList<ActivityManager.RunningServiceInfo> runningServices2 = (ArrayList<ActivityManager.RunningServiceInfo>) myAM.getRunningServices(40);
        runningServices2.size();*/
    }

    class MainReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String flag = intent.getStringExtra("flag");
            assert flag != null;
            switch (flag) {
                case "refresh_user_info":
                    ToastUtils.showTextToast(context, "刷新用户信息");
                    break;
            }
        }
    }


    private void initData() {
        shopFragment = new ShopFragment();
        newsFragment = new NewsFragment();
        jokeFragment = new JokeFragment();
        mineFragment = new MineFragment();

        fragmentList.add(shopFragment);
        fragmentList.add(newsFragment);
        fragmentList.add(jokeFragment);
        fragmentList.add(mineFragment);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainReceive);
        unbindService(serviceConnection);
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                //Log.e("err","权限"+permission+"没有授权");
                return false;
            }
        }
        return true;
    }

    //申请权限结果返回处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                // 所有的权限都授予了
                Log.e("err", "权限都授权了");
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                //容易判断错
                //MyDialog("提示", "某些权限未开启,请手动开启", 1) ;
            }
        }
    }


}
