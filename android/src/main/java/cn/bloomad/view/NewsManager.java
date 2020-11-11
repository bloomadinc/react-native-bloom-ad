package cn.bloomad.view;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

import java.util.HashMap;

import cn.bloomad.module.NewsModule;

public class NewsManager extends BaseFragmentManager {
    private static final String TAG = NewsManager.class.getSimpleName();
    private static final String REACT_CLASS = "NewsPortal";
    private NewsModule newsModule;

    public NewsManager(ReactApplicationContext reactContext) {
        super(reactContext, REACT_CLASS);
    }

    @Override
    public void attachFragment(int reactNativeId, String appId) {
        Activity mActivity = getActivity(mCallerContext);
        if (mActivity != null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String moduleId = String.valueOf(reactNativeId);
            Log.d("NewsManager", "reactNativeId:" + moduleId);
            map.put("reactNativeId", reactNativeId);
            map.put("appId", appId);
            Log.i(TAG, "reactNativeId:" + moduleId);
            if (moduleManager.has(moduleId)) {
                newsModule = (NewsModule) moduleManager.getInstance(moduleId);
            } else {
                newsModule = new NewsModule(mCallerContext, mActivity, moduleId);
                moduleManager.add(moduleId, newsModule);
            }
            newsModule.action(map);
        }
    }
}
