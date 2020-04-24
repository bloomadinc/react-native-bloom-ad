package cn.bloomad.view;

import android.app.Activity;
import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;

import cn.bloomad.module.ModuleManager;

public class BaseViewManager extends SimpleViewManager<ContainerView> {
    private String REACT_CLASS = "BaseView";
    public ModuleManager moduleManager;

    ReactApplicationContext mCallerContext;

    public BaseViewManager(ReactApplicationContext reactContext, String reactName) {
        mCallerContext = reactContext;
        REACT_CLASS = reactName;
        moduleManager = ModuleManager.getInstance();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ContainerView createViewInstance(ThemedReactContext context) {
        ContainerView containerView = new ContainerView(context);
//        TextView textView = new TextView(context);
//        textView.setLayoutParams(
//        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//        ViewGroup.LayoutParams.WRAP_CONTENT));
//        textView.setText("测试2");
//        textView.setTextColor(Color.RED);
//        containerView.addView(textView);
        return containerView;

    }

    public Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }

        if (context instanceof ReactContext) {
            ReactContext reactContext = ((ReactContext) context);
            return reactContext.getCurrentActivity();
        }
        return null;
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of("onNativeChange", MapBuilder.of("registrationName", "onChange"));
    }
}
