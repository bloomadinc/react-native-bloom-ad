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
import com.linkin.videosdk.DrawVideoFragment;
import com.linkin.videosdk.VideoSdk;

import java.util.Map;

import javax.annotation.Nullable;

import cn.bloomad.widget.VideoControllerView;

public class VideoModule extends EventModule {
    private final int NEWS_FRAGMENT_ID = 88888;
    private DrawVideoFragment mDrawVideoFragment;
    // private FrameLayout newsContainer;
    private InitModule initModule;

    public VideoModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
        initModule = InitModule.getInstance();
    }

    public void sendStatus(String type, @Nullable String id, @Nullable int videoType) {
        WritableMap params = Arguments.createMap();
        params.putString("type", type);
        params.putString("id", id);
        params.putString("videoType", String.valueOf(videoType));
        Log.d(TAG, eventName + ":" + type);
        sendEvent(params);
    }

    @SuppressLint("ResourceType")
    @Override
    public void threadAction(Activity mActivity, Map params) {
        final ViewGroup viewGroup = (ViewGroup) params.get("viewGroup");
        final String appId = (String) params.get("appId");
        final Boolean play = (Boolean) params.get("play");
        AppCompatActivity activity = (AppCompatActivity) mActivity;
        final FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(NEWS_FRAGMENT_ID);
        Log.d(TAG, "VideoModule threadAction:" + (null == fragment) + ":" + String.valueOf(viewGroup.getId()) + ",play:" + String.valueOf(play));
        if (null == fragment) {

            initModule.init(mActivity, appId);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            FrameLayout newsContainer = new FrameLayout(activity);
            newsContainer.setId(NEWS_FRAGMENT_ID);

            viewGroup.addView(newsContainer, layoutParams);

            viewGroup.post(new Runnable() {
                @Override
                public void run() {
                    mDrawVideoFragment = DrawVideoFragment.newInstance();

                    fm.beginTransaction().add(NEWS_FRAGMENT_ID, mDrawVideoFragment).commitAllowingStateLoss();

                    mDrawVideoFragment.setControllerViewClass(VideoControllerView.class);

                    mDrawVideoFragment.setVideoListener(new VideoSdk.VideoListener() {
                        @Override
                        public void onVideoShow(String id, int videoType) { // 视频切换展示
                            Log.d(TAG, "VideoModule onVideoShow");
                            sendStatus("onVideoShow", id, videoType);
                        }

                        @Override
                        public void onVideoStart(String id, int videoType) { // 播放开始
                            Log.d(TAG, "VideoModule onVideoStart");
                            sendStatus("onVideoStart", id, videoType);
                        }

                        @Override
                        public void onVideoPause(String id, int videoType) { // 播放暂停
                            Log.d(TAG, "VideoModule onVideoPause");
                            sendStatus("onVideoPause", id, videoType);
                        }

                        @Override
                        public void onVideoResume(String id, int videoType) { // 播放恢复
                            Log.d(TAG, "VideoModule onVideoResume");
                            sendStatus("onVideoResume", id, videoType);
                        }

                        @Override
                        public void onVideoComplete(String id, int videoType) { // 播放完成
                            Log.d(TAG, "VideoModule onVideoComplete");
                            sendStatus("onVideoComplete", id, videoType);
                        }

                        @Override
                        public void onVideoError(String id, int videoType) { // 播放出错
                            Log.d(TAG, "VideoModule onVideoError");
                            sendStatus("onVideoError", id, videoType);
                        }
                    });

                    // 视频点赞监听，返回是否阻止事件继续传播
                    mDrawVideoFragment.setOnLikeClickListener(new VideoSdk.OnLikeClickListener() {
                        @Override
                        public boolean onLikeClick(String id, int videoType, boolean like) {
                            Log.d(TAG, "VideoModule onLikeClick");
                            WritableMap params = Arguments.createMap();
                            params.putString("type", "onLikeClick");
                            params.putString("id", id);
                            params.putString("videoType", String.valueOf(videoType));
                            params.putString("like", String.valueOf(like));
                            sendEvent(params);
                            return false;
                        }
                    });

                    // 视频分享监听，返回是否阻止事件继续传播
                    mDrawVideoFragment.setOnShareClickListener(new VideoSdk.OnShareClickListener() {
                        @Override
                        public boolean onShareClick(String id, int videoType, String videoUrl, String author, String title) {
                            Log.d(TAG, "VideoModule onShareClick");
                            WritableMap params = Arguments.createMap();
                            params.putString("type", "onShareClick");
                            params.putString("id", id);
                            params.putString("videoType", String.valueOf(videoType));
                            params.putString("videoUrl", videoUrl);
                            params.putString("author", author);
                            params.putString("title", title);
                            sendEvent(params);
                            return false;
                        }
                    });

                    // 视频播放进度监听
                    mDrawVideoFragment.setProgressListener(new VideoSdk.ProgressListener() {
                        @Override
                        public void onProgressUpdate(String id, int videoType, int position, int duration) {
                            Log.d(TAG, "VideoModule onProgressUpdate");
                            WritableMap params = Arguments.createMap();
                            params.putString("type", "onProgressUpdate");
                            params.putString("id", id);
                            params.putString("videoType", String.valueOf(videoType));
                            params.putString("position", String.valueOf(position));
                            params.putString("duration", String.valueOf(duration));
                            sendEvent(params);
                        }
                    });

                    mDrawVideoFragment.onHiddenChanged(!play);
                }
            });
        } else {
            // final Fragment finalFragment1 = fragment;
            // viewGroup.post(new Runnable() {
            //     @Override
            //     public void run() {
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
            if (mDrawVideoFragment != null) {
                mDrawVideoFragment.onHiddenChanged(!play);
            }
            //     }
            // });
        }
    }

    @Override
    public void sendEvent(WritableMap event) {
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }

    public void playVideo(final boolean isPlay) {
        Log.d(TAG, "playVideo");
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDrawVideoFragment != null) {
                        mDrawVideoFragment.onHiddenChanged(!isPlay);
                    }
                }
            });
        } else {
            Log.d(TAG, "noActivity");
            sendStatus("noActivity", null);
        }
    }
}
