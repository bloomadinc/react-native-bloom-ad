package cn.bloomad.module;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.linkin.adsdk.AdSdk;

import java.util.Map;

import cn.bloomad.module.EventModule;

public class RewardVideoModule extends EventModule {

    public RewardVideoModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
    }

    @Override
    public void threadAction(Activity mActivity, Map params) {
        String unitId = params.get("unitId").toString();
        AdSdk.getInstance().loadRewardVideoAd(mActivity, unitId, false,
                new AdSdk.RewardVideoAdListener() {
                    @Override
                    public void onAdLoad(String id) {
                        Log.d(TAG, "RewardVideoAd onAdLoad");

                        sendStatus("onAdLoad", id);
                    }

                    @Override
                    public void onVideoCached(String id) {
                        Log.d(TAG, "RewardVideoAd onVideoCached");

                        sendStatus("onVideoCached", id);
                    }

                    @Override
                    public void onAdShow(String id) {
                        Log.d(TAG, "RewardVideoAd onAdShow");

                        sendStatus("onAdShow", id);
                    }

                    /** 视频广告播完验证奖励有效性回调，建议在此回调给用户奖励 */
                    @Override
                    public void onReward(String id) {
                        Log.d(TAG, "RewardVideoAd onReward");

                        sendStatus("onReward", id);
                    }

                    @Override
                    public void onAdClick(String id) {
                        Log.d(TAG, "RewardVideoAd onAdClick");

                        sendStatus("onAdClick", id);
                    }

                    @Override
                    public void onVideoComplete(String id) {
                        Log.d(TAG, "RewardVideoAd onVideoComplete");

                        sendStatus("onVideoComplete", id);
                    }

                    @Override
                    public void onAdClose(String id) {
                        Log.d(TAG, "RewardVideoAd onAdClose");

                        sendStatus("onAdClose", id);
                    }

                    @Override
                    public void onError(String id, int code, String message) {
                        Log.d(TAG, "RewardVideoAd onError: code=" + code + ", message=" + message);
                        // Toast.makeText(activity, "【" + code + "】" + message,
                        // Toast.LENGTH_LONG).show();
                        sendError( id, code, message);
                    }
                });

    }
}
