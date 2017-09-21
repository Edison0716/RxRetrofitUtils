package com.junlong0716.rxretrofit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;


import com.hss01248.notifyutil.NotifyUtil;
import com.junlong0716.retrofitutils.RetrofitUtils;
import com.junlong0716.retrofitutils.download.DownLoadSubscriber;
import com.junlong0716.rxretrofit.event.DownloadEvent;
import com.junlong0716.rxretrofit.rxbus.RxBus;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

/**
 * Created by ${巴黎没有摩天轮Li} on 2017/9/20.
 */

public class DownloadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RetrofitUtils.downLoadFile("qq.apk").safeSubscribe(new DownLoadSubscriber(this) {
            @Override
            protected void _onNext(String result) {
                Toast.makeText(getApplicationContext(), "下载成功！", Toast.LENGTH_SHORT).show();
                RxBus.getDefault().post(new DownloadEvent("download_success", 100));
            }

            @Override
            protected void _onProgress(Integer percent) {
                NotifyUtil.buildProgress(102, R.mipmap.ic_launcher, "正在下载", percent, 100, "下载进度:%d%%")
                        .setOnGoing()
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLockScreenVisiablity(VISIBILITY_PUBLIC)
                        .show();
                RxBus.getDefault().post(new DownloadEvent("download_running", percent));
            }

            @Override
            protected void _onError(int errorCode, String msg) {
                NotifyUtil.cancel(102);
                RxBus.getDefault().post(new DownloadEvent("download_failed", 0));
                Toast.makeText(getApplicationContext(), "下载失败！", Toast.LENGTH_SHORT).show();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}
