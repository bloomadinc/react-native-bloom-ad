package cn.bloomad.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.linkin.newssdk.NewsPortalFragment;

import java.util.Map;


public class NewsModule extends EventModule {
    private final int NEWS_FRAGMENT_ID = 88889;
    private NewsPortalFragment mNewsPortalFragment;
    // private FrameLayout newsContainer;
    private InitModule initModule;

    public NewsModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
        initModule = InitModule.getInstance();
    }

    @SuppressLint("ResourceType")
    @Override
    public void threadAction(Activity mActivity, Map params) {
        final ViewGroup viewGroup = (ViewGroup) params.get("viewGroup");
        final String appId = (String) params.get("appId");
        AppCompatActivity activity = (AppCompatActivity) mActivity;
        final FragmentManager fm = activity.getSupportFragmentManager();
        if(appId != null && appId.length() > 0){
            initModule.init(mActivity, appId);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout newsContainer = new FrameLayout(activity);
        newsContainer.setId(NEWS_FRAGMENT_ID);

        viewGroup.addView(newsContainer, layoutParams);

        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                mNewsPortalFragment = NewsPortalFragment.newInstance();
                fm.beginTransaction().add(NEWS_FRAGMENT_ID, mNewsPortalFragment).commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void sendEvent(WritableMap event) {
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }
}
