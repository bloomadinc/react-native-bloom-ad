package cn.bloomad.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.linkin.newssdk.NewsPortalFragment;
import com.linkin.newssdk.NewsSdk;

import java.util.Map;

import cn.bloomad.util.DensityUtils;
import cn.bloomad.widget.CountdownView;


public class NewsModule extends EventModule {
    private final int NEWS_FRAGMENT_ID = 88889;
    private NewsPortalFragment mNewsPortalFragment;
    // private FrameLayout newsContainer;
    private InitModule initModule;
    private NewsSdk.ReadingCountdownHandler readingCountdownHandler;
    private NewsSdk.ReadingRewardHandler readingRewardHandler;

    public NewsModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
        initModule = InitModule.getInstance();
    }

    public void sendStatus(String type, String newsUrl, int newsType) {
        WritableMap params = Arguments.createMap();
        params.putString("type", type);
        params.putString("newsUrl", newsUrl);
        params.putString("newsType", String.valueOf(newsType));
        Log.d(TAG, eventName + ":" + type);
        sendEvent(params);
    }

    public void setReadingCountdownHandler(NewsSdk.ReadingCountdownHandler handler) {
        readingCountdownHandler = handler;
    }

    public void setReadingRewardHandler(NewsSdk.ReadingRewardHandler handler) {
        readingRewardHandler = handler;
    }

    @SuppressLint("ResourceType")
    @Override
    public void threadAction(Activity mActivity, Map params) {
        final ViewGroup viewGroup = (ViewGroup) params.get("viewGroup");
        final String appId = (String) params.get("appId");
        final AppCompatActivity activity = (AppCompatActivity) mActivity;
        final FragmentManager fm = activity.getSupportFragmentManager();
        if (appId != null && appId.length() > 0) {
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

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.END);
                layoutParams.setMargins(0, 0,
                        DensityUtils.dip2px(activity, 8),
                        DensityUtils.dip2px(activity, 8));

                // 请按照设计修改 CountdownView 类，这里是 demo 给出的示例
                NewsSdk.getInstance().configReadingCountdown(CountdownView.class, layoutParams,
                        new NewsSdk.ReadingCountdownListener() {
                            @Override
                            public void onReadingStart(NewsSdk.ReadingCountdownHandler handler, String newsUrl, int newsType) {
//                                Log.d(TAG, "NewsModule onReadingStart");
                                setReadingCountdownHandler(handler);
                                sendStatus("onReadingStart", newsUrl, newsType);
                            }

                            @Override
                            public void onReadingPause(String newsUrl, int newsType) {
//                                Log.d(TAG, "NewsModule onReadingPause");
                                sendStatus("onReadingPause", newsUrl, newsType);
                            }

                            @Override
                            public void onReadingResume(NewsSdk.ReadingCountdownHandler handler, String newsUrl, int newsType) {
//                                Log.d(TAG, "NewsModule onReadingResume");
                                sendStatus("onReadingResume", newsUrl, newsType);
                            }

                            @Override
                            public void onReward(NewsSdk.ReadingRewardHandler handler, String newsUrl, int newsType, Object rewardData) {
//                                Log.d(TAG, "NewsModule onReward");
                                // 在此发放奖励，并设置奖励结果
//                                boolean success = true;
//                                handler.setRewardResult(success, rewardData);
                                setReadingRewardHandler(handler);
                                sendStatus("onReward", newsUrl, newsType);
                            }

                            @Override
                            public void onReadingEnd(String newsUrl, int newsType) {
//                                Log.d(TAG, "NewsModule onReadingEnd");
                                sendStatus("onReadingEnd", newsUrl, newsType);
                            }
                        });

            }
        });
    }

    @Override
    public void sendEvent(WritableMap event) {
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }

    public void setShow(boolean isShow, final int countdownSeconds, final int scrollEffectSeconds, final Object rewardData) {
        Log.d(TAG, "updateReward");
        if (mActivity != null) {
            if (isShow) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (readingCountdownHandler != null) {
                            readingCountdownHandler.startCountdown(countdownSeconds, scrollEffectSeconds, rewardData);
                        }
                    }
                });
            } else {
                setReadingCountdownHandler(null);
                setReadingRewardHandler(null);
            }
        } else {
            Log.d(TAG, "noActivity");
            sendStatus("noActivity", null);
        }
    }

    public void setReward(final boolean isReward, final Object rewardData) {
        Log.d(TAG, "updateReward");
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (readingRewardHandler != null) {
                        readingRewardHandler.setRewardResult(isReward, rewardData);
                    }
                }
            });
        } else {
            Log.d(TAG, "noActivity");
            sendStatus("noActivity", null);
        }
    }
}
