package cn.bloomad.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mob.videosdk.DrawVideoFragment;
import com.mob.videosdk.VideoSdk;

import java.util.Map;

import cn.bloomad.widget.VideoControllerView;

public class VideoModule extends EventModule {
    private DrawVideoFragment mDrawVideoFragment;
    // private FrameLayout newsContainer;
    private InitModule initModule;

    public VideoModule(final ReactApplicationContext reactContext, final Activity activity, String name) {
        super(reactContext, activity, name);
        initModule = InitModule.getInstance();
    }

    @SuppressLint("ResourceType")
    @Override
    public void threadAction(Activity mActivity, Map params) {
        final int reactNativeId = (int) params.get("reactNativeId");
        final String appId = (String) params.get("appId");
        AppCompatActivity activity = (AppCompatActivity) mActivity;
        Log.d(TAG, "VideoModule threadAction:");

        if (appId != null && appId.length() > 0) {
            initModule.init(mActivity, appId);
        }

        mDrawVideoFragment = DrawVideoFragment.newInstance();
        activity.getSupportFragmentManager().beginTransaction().replace(
                reactNativeId, mDrawVideoFragment, String.valueOf(reactNativeId)
        ).commitAllowingStateLoss();

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
    }

    public void sendStatus(String type, String id, int videoType) {
        WritableMap params = Arguments.createMap();
        params.putString("type", type);
        params.putString("id", id);
        params.putString("videoType", String.valueOf(videoType));
        Log.d(TAG, eventName + ":" + type);
        sendEvent(params);
    }

    @Override
    public void sendEvent(WritableMap event) {
        mReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(Integer.parseInt(eventName), "onNativeChange",
                event);
    }

    public void playVideo(final boolean isPlay) {
        Log.d(TAG, "playVideo");
        if (mActivity != null && mDrawVideoFragment != null) {
            Log.d(TAG, "onHiddenChanged:" + String.valueOf(isPlay));
            mDrawVideoFragment.onHiddenChanged(!isPlay);
        }
    }

    @Override
    public void destroy() {
        AppCompatActivity activity = (AppCompatActivity) mActivity;
        Log.d(TAG, "destroy");
        activity.getSupportFragmentManager().beginTransaction().remove(mDrawVideoFragment).commitAllowingStateLoss();
    }
}
