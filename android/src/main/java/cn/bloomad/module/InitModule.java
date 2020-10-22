package cn.bloomad.module;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mob.adsdk.AdConfig;
import com.mob.adsdk.AdSdk;
import com.mob.newssdk.NewsConfig;
import com.mob.newssdk.NewsSdk;
import com.mob.videosdk.VideoConfig;
import com.mob.videosdk.VideoSdk;


import cn.bloomad.BuildConfig;

public class InitModule {
    private static final String TAG = InitModule.class.getSimpleName();
    private static boolean isAdInit = false;
    private static boolean isVideoInit = false;
    private static boolean isNewsInit = false;
    //创建 SingleObject 的一个对象
    private static InitModule instance = new InitModule();

    //让构造函数为 private，这样该类就不会被实例化
    private InitModule() {
    }

    //获取唯一可用的对象
    public static InitModule getInstance() {
        return instance;
    }

    public void init(final Context context,final String appId) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initAd(context, appId);
                initVideo(context, appId);
                initNews(context, appId);
            }
        });
    }

    public void initAd(Context context, String appId) {
        if (!isAdInit) {
            AdSdk.getInstance().init(context, new AdConfig.Builder().appId(appId)
                    // .userId("uid") // 未登录可不设置 userId，登录时再设置
                    .multiProcess(false).debug(BuildConfig.DEBUG).build(), null);
            isAdInit = true;
        }
    }

    public void initVideo(Context context, String appId) {
        if (!isVideoInit) {
            VideoSdk.getInstance().init(context, new VideoConfig.Builder().appId(appId)
                    // .userId("uid") // 未登录可不设置 userId，登录时再设置
                    .debug(BuildConfig.DEBUG).build(), null);
            isVideoInit = true;
        }
    }


    public void initNews(Context context, String appId) {
        if (!isNewsInit) {
            NewsSdk.getInstance().init(context,
                    new NewsConfig.Builder()
                            .appId(appId)
//                            .userId("uid") // 未登录可不设置 userId，登录时再设置
                            .debug(BuildConfig.DEBUG)
                            .build(),
                    null);
            isNewsInit = true;
        }
    }

}
