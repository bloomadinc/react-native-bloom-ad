package cn.bloomad;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.bloomad.view.BannerViewManager;
import cn.bloomad.view.DrawVideoManager;
import cn.bloomad.view.NativeExpressManager;
import cn.bloomad.view.VideoManager;

public class BloomAdPackage implements ReactPackage {

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(new BloomAdModule(reactContext));
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(new BannerViewManager(reactContext), new NativeExpressManager(reactContext),
                new DrawVideoManager(reactContext), new VideoManager(reactContext));
    }

}
