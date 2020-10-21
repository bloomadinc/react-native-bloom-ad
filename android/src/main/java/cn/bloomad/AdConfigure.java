package cn.bloomad;

import android.content.Context;

import cn.bloomad.module.InitModule;

public class AdConfigure {
    public static void init(Context context, String appId) {
        InitModule.getInstance().init(context, appId);
    }
}
