package cn.bloomad.view;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;

import cn.bloomad.module.VideoModule;

public class VideoManager extends BaseFragmentManager {
    private static final String TAG = VideoManager.class.getSimpleName();
    private static final String REACT_CLASS = "VideoStreaming";
    private VideoModule videoModule;

    public VideoManager(ReactApplicationContext reactContext) {
        super(reactContext, REACT_CLASS);
    }

    @Override
    public void attachFragment(int reactNativeId, String appId) {
        Activity mActivity = getActivity(mCallerContext);
        if (mActivity != null) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String moduleId = String.valueOf(reactNativeId);
            Log.d("VideoManager", "reactNativeId:" + moduleId );
            map.put("reactNativeId", reactNativeId);
            map.put("appId", appId);
            Log.i(TAG, "reactNativeId:" + moduleId);
            if (moduleManager.has(moduleId)) {
                videoModule = (VideoModule) moduleManager.getInstance(moduleId);
            } else {
                videoModule = new VideoModule(mCallerContext, mActivity, moduleId);
                moduleManager.add(moduleId, videoModule);
            }
            videoModule.action(map);
        }
    }


    @ReactProp(name = "play")
    public void setPlay(FrameLayout layout, ReadableMap map) {
        Boolean isPlay = map.getBoolean("play");
        int reactNativeId = map.getInt("reactNativeId");
        String moduleId = String.valueOf(reactNativeId);
        Log.d("VideoManager", "reactNativeId:" + moduleId + ",setPlay:" + String.valueOf(isPlay));
        if (moduleManager.has(moduleId)) {
            videoModule = (VideoModule) moduleManager.getInstance(moduleId);
            videoModule.playVideo(isPlay);
        }
    }
}
