package cn.bloomad;

import com.facebook.react.bridge.ReactContext;

import cn.bloomad.module.InitModule;

public class AdConfigure {
    public static void init(ReactContext context, String appId) {
        InitModule.getInstance().init(context.getCurrentActivity(), appId);
    }
}
