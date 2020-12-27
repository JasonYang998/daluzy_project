package com.daluzy.daluzy_project.daluzy_project.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;



@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    @Override
    public void onCreate() {
        super.onCreate();
        startJob();
    }

  /*  @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("qin_service:", "aaa " + startId);

        return START_STICKY;
    }*/

    private void startJob() {
        JobInfo.Builder builder = new JobInfo.Builder(1001, new ComponentName(getPackageName(), MyJobService.class.getName()));
        builder.setPersisted(false);//是否关机重启
        //是否需要处于充电状态
//        builder.setRequiresCharging(false);
        //是否需要处于非低电量状态
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setRequiresBatteryNotLow(false);
            builder.setRequiresStorageNotLow(false);
        }*/
        //是否需要设备处于空闲状态
        builder.setRequiresDeviceIdle(false);
        //是否需要设备剩余空间不为low


        //可选参数
        //NETWORK_TYPE_ANY ：需要网络连接
        //NETWORK_TYPE_UNMETERED : 需要不计费的网络连接
        //NETWORK_TYPE_NOT_ROAMING : 需要不漫游的网络连接
        //NETWORK_TYPE_METERED : 需要计费的网络连接，例如蜂窝数据网络
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        //设置任务周期执行，其周期为intervalMillis参数
        //你无法控制任务的执行时间，系统只保证在此时间间隔内，任务最多执行一次。
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            builder.setMinimumLatency(10*1000);
        }else{
            builder.setPeriodic(10*1000);
        }

            //任务将被延迟至少minLatencyMillis时间执行，和setPeriodic周期任务冲突。
//        builder.setMinimumLatency(500);
        //不管其他约束条件是否满足，任务最多于maxExecutionDelayMillis时间后被执行
        //和setPeriodic周期任务冲突。
//        builder.setOverrideDeadline(1000);

        //增加待监听的uri
//        builder.addTriggerContentUri("");
//设置uri变化时，延迟durationMs后执行任务。在此期间Uri再次变化，则重新计时
//设置该时间可以让我们过滤掉太频繁的变化，减少任务的执行次数。
//        builder.setTriggerContentUpdateDelay(long durationMs)
//第一次uri变化后，我们任务可以等待的最大时间，和updateDelay配合使用。
//        builder.setTriggerContentMaxDelay(long durationMs)
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        int i = jobParameters.getJobId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 1000; i++) {
                        Thread.sleep(2000);
                        Log.i("qin", "这是第" + i + "个");
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        jobFinished(jobParameters,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
