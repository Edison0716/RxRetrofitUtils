package com.junlong0716.rxretrofit;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hss01248.notifyutil.NotifyUtil;
import com.junlong0716.retrofitutils.RetrofitUtils;
import com.junlong0716.retrofitutils.upload.UploadSubscriber;
import com.junlong0716.retrofitutils.utils.FileUtils;
import com.junlong0716.rxretrofit.event.DownloadEvent;
import com.junlong0716.rxretrofit.rxbus.RxBus;
import com.junlong0716.rxretrofit.rxbus.Subscribe;
import com.junlong0716.rxretrofit.rxbus.ThreadMode;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int IMAGE_PICKER_SINGLE = 0;
    private int IMAGE_PICKER_MUTI = 1;
    private ProgressBar mBar;
    private Intent mIntent;
    private ImagePicker mImagePicker;
    private TextView mTvProgress;

    //EventBus 3.0 回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(DownloadEvent s) {
        switch (s.getWhat()) {
            case "download_running":
                mBar.setProgress(s.getPercent());
                break;
            case "download_success":
                NotifyUtil.cancel(102);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                File file = new File(FileUtils.getDefaultDownLoadPath(), "qq.apk");
                String path = file.getPath();
                Log.d("path", path);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
                    Uri apkUri = FileProvider.getUriForFile(this, "com.junlong0716.rxretrofit.fileprovider", file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getDefault().register(this);
        initImagePicker();
        findViewById(R.id.bt_get_request).setOnClickListener(this);
        findViewById(R.id.bt_upload_image).setOnClickListener(this);
        findViewById(R.id.bt_upload_muti_image).setOnClickListener(this);
        findViewById(R.id.bt_post_request).setOnClickListener(this);
        findViewById(R.id.bt_download).setOnClickListener(this);
        mBar = (ProgressBar) findViewById(R.id.pb);
        mTvProgress = findViewById(R.id.tv_progress);
    }

    private void initImagePicker() {
        mImagePicker = ImagePicker.getInstance();
        mImagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        mImagePicker.setShowCamera(true);  //显示拍照按钮
        mImagePicker.setCrop(true);        //允许裁剪（单选才有效）
        mImagePicker.setSaveRectangle(true); //是否按矩形区域保存
        mImagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        mImagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        mImagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        mImagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        mImagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_get_request:
                RetrofitUtils.createService(Api.class).getFoodType("0").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull ResponseBody body) {
                                try {
                                    Toast.makeText(getApplicationContext(), body.string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.bt_upload_image:
                mImagePicker.setSelectLimit(1);    //选中数量限制
                mIntent = new Intent(MainActivity.this, ImageGridActivity.class);
                startActivityForResult(mIntent, IMAGE_PICKER_SINGLE);
                break;
            case R.id.bt_upload_muti_image:
                mImagePicker.setSelectLimit(9);    //选中数量限制
                mIntent = new Intent(MainActivity.this, ImageGridActivity.class);
                startActivityForResult(mIntent, IMAGE_PICKER_MUTI);
                break;
            case R.id.bt_post_request:
                RetrofitUtils.createService(Api.class).getPostData("0")
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull ResponseBody body) {
                                try {
                                    Toast.makeText(getApplicationContext(), body.string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.bt_download:
                Intent startIntent = new Intent(this, DownloadService.class);
                startService(startIntent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER_SINGLE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String path = images.get(0).path;
                startUpload(path);
            } else if (data != null && requestCode == IMAGE_PICKER_MUTI) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                startUploadMore(images);
            }
        }
    }

    private void startUploadMore(ArrayList<ImageItem> images) {
        ArrayList<File> filesList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            File file = new File(images.get(i).path);
            filesList.add(file);
        }
        RetrofitUtils.uploadFiles(filesList, Api.class, "uploads").safeSubscribe(new UploadSubscriber<String>(this) {
            @Override
            protected void _onNext(String result) {
                Log.i("retrofit_onNext", "onNext=======>url:" + result);
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            protected void _onProgress(Integer percent) {
                Log.i("retrofit_progress", "onProgress======>" + percent);
                mBar.setProgress(percent);
            }

            @Override
            protected void _onError(int errorCode, String msg) {
                Log.i("retrofit_onNext", "onNext=======>url:" + msg);
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startUpload(String path) {
        File file = new File(path);
        RetrofitUtils.uploadFile(file, Api.class, "upload").safeSubscribe(new UploadSubscriber<String>(this) {
            @Override
            protected void _onNext(String result) {
                Log.i("retrofit_onNext", "onNext=======>url:" + result);
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void _onProgress(Integer percent) {
                mBar.setProgress(percent);
            }

            @Override
            protected void _onError(int errorCode, String msg) {
                Log.i("retrofit_onNext", "onNext=======>url:" + msg);
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
