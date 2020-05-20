package cn.bloomad.view;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;

import javax.annotation.Nullable;

import cn.bloomad.module.ModuleManager;
import cn.bloomad.module.VideoModule;

public class VideoManager extends BaseViewManager {
    private static final String REACT_CLASS = "VideoStreaming";
    private  VideoModule videoModule;

    public VideoManager(ReactApplicationContext reactContext) {
        super(reactContext, REACT_CLASS);
        mCallerContext = reactContext;
        moduleManager = ModuleManager.getInstance();
    }

    @ReactProp(name = "size")
    public void setSize(ContainerView containerView, @Nullable ReadableMap sizeReadable) {
        Activity mActivity = getActivity(mCallerContext);
        Log.d("VideoManager", "setSize" + sizeReadable.toString());
        if (sizeReadable != null && mActivity != null) {
            HashMap<String, Object> map = sizeReadable.toHashMap();
            map.put("viewGroup", containerView);
            if(videoModule == null){
                String id = String.valueOf(containerView.getId());
                String unique = sizeReadable.getString("unique");
                videoModule = new VideoModule(mCallerContext, mActivity, id);
                moduleManager.add(unique, videoModule);
                videoModule.action(map);
            } else {
                videoModule.action(map);
            }
        }
    }

    @ReactProp(name = "play", defaultBoolean = true)
    public void setPlay(ContainerView containerView, boolean isPlay) {
        Log.d("VideoManager", "setPlay:" + String.valueOf(isPlay));
        if (videoModule != null) {
            videoModule.playVideo(isPlay);
        }
    }
}
