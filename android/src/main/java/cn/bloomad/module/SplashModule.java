package cn.bloomad.module;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cn.bloomad.activity.SplashActivity;

public class SplashModule extends EventModule {
    private static final int BLOOM_AD_REQUEST = 1;
    private static boolean isInitSplash = false;
    private static long minSplashInterval = 1000 * 60 * 3;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
//            Log.i(TAG, "requestCode:" +  String.valueOf(requestCode) + ",resultCode:" + resultCode );
            if(BLOOM_AD_REQUEST == requestCode) {
                String id =  data.getStringExtra("id");
                int code = data.getIntExtra("code", 0);
                String message = data.getStringExtra("message");
                if (code == 0) {
                    sendStatus("onAdDismiss", id);
                } else {
                    sendError(id, code, message);
                }
                Log.i(TAG, "code:" +  String.valueOf(code) + ",id:" + id );
            }
        }
    };

    public SplashModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);

        Log.i(TAG, String.valueOf(isInitSplash));
        if(!isInitSplash){
            initSplash();
            reactContext.addActivityEventListener(mActivityEventListener);
            isInitSplash = true;
        }
    }

    public void show(final int interval){
        minSplashInterval = interval;

        if (minSplashInterval <= 0) {
            return;
        }

        Intent intent = new Intent(mActivity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // 无动画
        mActivity.startActivityForResult(intent, BLOOM_AD_REQUEST);
    }

    public void initSplash(){
        mActivity = mReactContext.getCurrentActivity();

        mActivity.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            private int activityCount = 1;
            private long leaveTime = 0;

            @Override
            public void onActivityCreated(@Nonnull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@Nonnull Activity activity) {
                if (0 == activityCount++) {
                    // App 回到前台
                    if (System.currentTimeMillis() - leaveTime >= minSplashInterval) {
                        if (!(activity instanceof SplashActivity)) {
                            Intent intent = new Intent(activity, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // 无动画
                            activity.startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onActivityResumed(@Nonnull Activity activity) {
            }

            @Override
            public void onActivityPaused(@Nonnull Activity activity) {
            }

            @Override
            public void onActivityStopped(@Nonnull Activity activity) {
                if (0 == --activityCount) {
                    // App 切到后台
                    leaveTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@Nonnull Activity activity, @Nonnull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@Nonnull Activity activity) {
            }
        });
    }
}
