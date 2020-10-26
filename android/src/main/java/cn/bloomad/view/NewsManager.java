package cn.bloomad.view;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;

import cn.bloomad.module.ModuleManager;
import cn.bloomad.module.NewsModule;

public class NewsManager extends BaseViewManager {
    private static final String REACT_CLASS = "NewsPortal";
    private NewsModule newsModule;

    public NewsManager(ReactApplicationContext reactContext) {
        super(reactContext, REACT_CLASS);
        mCallerContext = reactContext;
        moduleManager = ModuleManager.getInstance();
    }

    @ReactProp(name = "size")
    public void setSize(ContainerView containerView, ReadableMap sizeReadable) {
        Activity mActivity = getActivity(mCallerContext);
        Log.d("NewsManager", "setSize" + sizeReadable.toString());
        if (sizeReadable != null && mActivity != null) {
            HashMap<String, Object> map = sizeReadable.toHashMap();
            map.put("viewGroup", containerView);
            String unique = sizeReadable.getString("unique");
            if (moduleManager.has(unique)) {
                newsModule = (NewsModule) moduleManager.getInstance(unique);
                newsModule.action(map);
            } else {
                String id = String.valueOf(containerView.getId());
                newsModule = new NewsModule(mCallerContext, mActivity, id);
                moduleManager.add(unique, newsModule);
                newsModule.action(map);
            }
        }
    }
}
