package cn.bloomad.view;

import android.app.Activity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;

import javax.annotation.Nullable;

import cn.bloomad.module.ModuleManager;
import cn.bloomad.module.NativeExpressModule;

public class NativeExpressManager extends BaseViewManager {
    private static final String REACT_CLASS = "NativeExpress";

    public NativeExpressManager(ReactApplicationContext reactContext) {
        super(reactContext, REACT_CLASS);
        mCallerContext = reactContext;
        moduleManager = ModuleManager.getInstance();
    }

    @ReactProp(name = "size")
    public void setSize(ContainerView containerView, @Nullable ReadableMap sizeReadable) {
        Activity mActivity = getActivity(mCallerContext);
        if (sizeReadable != null && mActivity != null) {
            // Log.i("Create View Instance", "setSize:" + sizeReadable.toString());
            String id = String.valueOf(containerView.getId());
            // Log.i("Create View Instance", "bannerView id:" + id);
            NativeExpressModule nativeExpressModule = new NativeExpressModule(mCallerContext, mActivity, id);
            String unique = sizeReadable.getString("unique");
            moduleManager.add(unique, nativeExpressModule);

            HashMap<String, Object> map = sizeReadable.toHashMap();
            map.put("viewGroup", containerView);
            nativeExpressModule.action(map);
        }
    }
}
