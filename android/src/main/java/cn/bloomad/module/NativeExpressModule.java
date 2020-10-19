package cn.bloomad.module;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mob.adsdk.AdSdk;

import java.util.List;
import java.util.Map;

public class NativeExpressModule extends EventModule {
    private AdSdk.NativeExpressAd mNativeExpressAd;

    public NativeExpressModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
    }

    @Override
    public void threadAction(final Activity mActivity, Map params) {
        final ViewGroup viewGroup = (ViewGroup) params.get("viewGroup");
        final float width = (float) (double) params.get("width");
        String unitId = params.get("unitId").toString();
        int count = (int) (double) (params.get("count"));
//        Log.v(TAG, "width:" + String.valueOf(width) + ",count:" + String.valueOf(count));
        AdSdk.getInstance().loadNativeExpressAd(mActivity, unitId, width, count,
                new AdSdk.NativeExpressAdListener() {
                    @Override
                    public void onAdLoad(List<AdSdk.NativeExpressAd> ads) {
                        Log.d(TAG, "NativeExpressAd onAdLoad");
                        mNativeExpressAd = ads.get(0);
                        mNativeExpressAd.render(viewGroup);
                        sendStatus("onAdLoad", mNativeExpressAd.getId());
                    }

                    @Override
                    public void onAdShow(String id) {
                        Log.d(TAG, "NativeExpressAd onAdShow");
                        sendStatus("onAdShow", id);
                    }

                    @Override
                    public void onAdClose(String id) {
                        Log.d(TAG, "NativeExpressAd onAdClose");
                        sendStatus("onAdClose", id);
                    }

                    @Override
                    public void onAdClick(String id) {
                        Log.d(TAG, "NativeExpressAd onAdClick");
                        sendStatus("onAdClick", id);
                    }

                    @Override
                    public void onError(String id, int code, String message) {
                        Log.d(TAG, "NativeExpressAd onError: code=" + code + ", message=" + message);
                        sendError( id, code, message);
                    }
                });
    }

    @Override
    public void sendEvent(WritableMap event) {
        Log.d(TAG, "BannerAd sendEvent");
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }

    @Override
    public void destroy() {
        if (null != mNativeExpressAd) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "destroy");
                    mNativeExpressAd.destroy();
                    mNativeExpressAd = null;
                }
            });
        }
    }
}
