package cn.bloomad.module;

import android.app.Activity;
import android.util.Log;

import com.linkin.adsdk.AdConfig;
import com.linkin.adsdk.AdSdk;
import com.linkin.newssdk.NewsConfig;
import com.linkin.newssdk.NewsSdk;
import com.linkin.videosdk.VideoConfig;
import com.linkin.videosdk.VideoSdk;

import java.util.HashMap;

import cn.bloomad.BuildConfig;

public class InitModule {
    private static final String TAG = InitModule.class.getSimpleName();
    private  static  boolean isAdInit = false;
    private static  boolean isVideoInit = false;
    private static  boolean isNewsInit = false;
    //创建 SingleObject 的一个对象
    private static InitModule instance = new InitModule();

    //让构造函数为 private，这样该类就不会被实例化
    private InitModule(){ }

    //获取唯一可用的对象
    public static InitModule getInstance(){
        return instance;
    }

    public void init(Activity mActivity, String appId){
        initAd( mActivity,appId);
        initVideo(mActivity,appId);
        initNews(mActivity,appId);
    }

    public void initAd(Activity mActivity, String appId){
        if(!isAdInit) {
            AdSdk.getInstance().init(mActivity, new AdConfig.Builder().appId(appId)
                    // .userId("uid") // 未登录可不设置 userId，登录时再设置
                    .multiProcess(false).debug(BuildConfig.DEBUG).build(), null);
            isAdInit = true;
        }
    }

    public void initVideo(Activity mActivity, String appId){
        if(!isVideoInit) {
            VideoSdk.getInstance().init(mActivity, new VideoConfig.Builder().appId(appId)
                    // .userId("uid") // 未登录可不设置 userId，登录时再设置
                    .debug(BuildConfig.DEBUG).build(), null);
            isVideoInit = true;
        }
    }


    public void initNews(Activity mActivity, String appId){
        if(!isNewsInit) {
            NewsSdk.getInstance().init(mActivity,
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
