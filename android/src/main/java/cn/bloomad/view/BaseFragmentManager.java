package cn.bloomad.view;

import android.app.Activity;
import android.content.Context;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import java.util.Map;

import cn.bloomad.module.ModuleManager;

public class BaseFragmentManager extends ViewGroupManager<FrameLayout> {
    private static final String TAG = BaseFragmentManager.class.getSimpleName();
    private String REACT_CLASS = "FragmentManager";
    private final int COMMAND_CREATE = 1;
    public ReactApplicationContext mCallerContext;
    public ModuleManager moduleManager;

    public BaseFragmentManager(ReactApplicationContext reactContext, String reactName) {
        mCallerContext = reactContext;
        REACT_CLASS = reactName;
        moduleManager = ModuleManager.getInstance();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected FrameLayout createViewInstance(ThemedReactContext context) {
        return new FrameLayout(context);
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "create", COMMAND_CREATE
        );
    }

    @Override
    public void receiveCommand(FrameLayout root,
                               String commandId,
                               ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        int receiveNativeViewId = args.getInt(0);
        String appId = args.getString(1);

        int commandIdInt = Integer.parseInt(commandId);
        switch (commandIdInt) {
            case COMMAND_CREATE:
                this.createFragment(root, receiveNativeViewId);
                this.attachFragment(receiveNativeViewId, appId);
                break;
            default: {

            }
        }
    }

    public void attachFragment(int reactNativeId, String appId){
//        DrawVideoFragment mDrawVideoFragment = DrawVideoFragment.newInstance();
//        Log.i(TAG, "reactNativeId:" + String.valueOf(reactNativeId));
//        ((FragmentActivity) this.context.getCurrentActivity()).getSupportFragmentManager().beginTransaction().replace(reactNativeId, mDrawVideoFragment, String.valueOf(reactNativeId)).commit();
    }

    public void createFragment(FrameLayout parentLayout, int reactNativeId) {
        View parentView = (ViewGroup)parentLayout.findViewById(reactNativeId);
        setupLayoutHack((ViewGroup) parentView);
    }

    void setupLayoutHack(final ViewGroup view){
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long l) {
                manuallyLayoutChildren(view);
                view.getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    void manuallyLayoutChildren(ViewGroup view){
        for(int i = 0; i < view.getChildCount();i++){
            View child = view.getChildAt(i);

            child.measure(
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(),View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(),View.MeasureSpec.EXACTLY)
            );

            child.layout(0,0,child.getMeasuredWidth(),child.getMeasuredHeight());
        }
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
