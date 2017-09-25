# RxRetrofitUtils
RxRetrofitUtils

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
	        compile 'com.github.Jack1995:RxRetrofitUtils:v1.0'
	}
  
  # 初始化
  BaseRetrofitClient.getInstance().setBaseUrl("http://xxx.xxx.com/").init(this);
  # get post 请求
  ```
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
  ```
  # 单文件上传
  ```
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
  ```
  
  # 多文件上传
  ```
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
  ```
  # 文件下载
  ```
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
```
##通过学习 https://github.com/whichname/EasyRxRetrofit 这个朋友的库 自己仿照写的 感谢
这位开发者！
