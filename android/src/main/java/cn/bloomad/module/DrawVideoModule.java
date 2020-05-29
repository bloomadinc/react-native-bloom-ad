package cn.bloomad.module;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.linkin.adsdk.AdSdk;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DrawVideoModule extends EventModule {
    private AdSdk.DrawVideoAd mDrawVideoAd;

    public DrawVideoModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
    }

    @Override
    public void threadAction(Activity mActivity, Map params) {
        final ViewGroup viewGroup = (ViewGroup) params.get("viewGroup");
        String unitId = params.get("unitId").toString();
        int count = (int) (double)(params.get("count"));
        AdSdk.getInstance().loadDrawVideoAd(mActivity, unitId, count, new AdSdk.DrawVideoAdListener() {
            @Override
            public void onAdLoad(List<AdSdk.DrawVideoAd> ads) {
                Log.d(TAG, "DrawVideoAd onAdLoad");

                mDrawVideoAd = ads.get(0);
                mDrawVideoAd.render(viewGroup);
                sendStatus("onAdLoad", mDrawVideoAd.getId());
            }

            @Override
            public void onAdShow(String id) {
                Log.d(TAG, "DrawVideoAd onAdShow");
                sendStatus("onAdShow", id);
            }

            @Override
            public void onAdClick(String id) {
                Log.d(TAG, "DrawVideoAd onAdClick");
                sendStatus("onAdClick", id);
            }

            @Override
            public void onVideoStart(String id) {
                Log.d(TAG, "DrawVideoAd onVideoStart");
                sendStatus("onVideoStart", id);
            }

            @Override
            public void onVideoPause(String id) {
                Log.d(TAG, "DrawVideoAd onVideoPause");
                sendStatus("onVideoPause", id);
            }

            @Override
            public void onVideoResume(String id) {
                Log.d(TAG, "DrawVideoAd onVideoResume");
                sendStatus("onVideoResume", id);
            }

            @Override
            public void onVideoComplete(String id) {
                Log.d(TAG, "DrawVideoAd onVideoComplete");
                sendStatus("onVideoComplete", id);
            }

            @Override
            public void onError(String id, int code, String message) {
                Log.d(TAG, "DrawVideoAd onError: code=" + code + ", message=" + message);
                sendError( id, code, message);
            }
        });
    }

    @Override
    public void sendEvent(WritableMap event) {
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }

    @Override
    public void destroy() {
        if (null != mDrawVideoAd) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "destroy");
                    mDrawVideoAd.destroy();
                    mDrawVideoAd = null;
                }
            });
        }
    }
}
