package cn.bloomad.view;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;

import javax.annotation.Nullable;

import cn.bloomad.module.BannerModule;

public class BannerViewManager extends BaseViewManager {
    private static final String REACT_CLASS = "BannerView";

    public BannerViewManager(ReactApplicationContext reactContext) {
        super(reactContext, REACT_CLASS);
    }

    @ReactProp(name = "size")
    public void setSize(ContainerView bannerView, @Nullable ReadableMap sizeReadable) {
        Activity mActivity = getActivity(mCallerContext);
        if (sizeReadable != null && mActivity != null) {
            // Log.i("Create View Instance", "setSize:" + sizeReadable.toString());
            String id = String.valueOf(bannerView.getId());
            String unique = sizeReadable.getString("unique");
            String unitId = sizeReadable.getString("unitId");

            BannerModule bannerModule = new BannerModule(mCallerContext, mActivity, id);
            moduleManager.add(unique, bannerModule);
            HashMap<String, Object> map = sizeReadable.toHashMap();
            map.put("viewGroup", bannerView);
            map.put("unitId", unitId);
            bannerModule.action(map);
        }
    }

}
