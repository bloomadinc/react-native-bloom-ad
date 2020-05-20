package cn.bloomad.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.linkin.videosdk.AbstractVideoControllerView;

import cn.bloomad.R;

public class VideoControllerView extends AbstractVideoControllerView {
    private final String TAG = "VideoControllerView";

    private ImageView mIvLike;

    public VideoControllerView(@NonNull Context context) {
        super(context);
        View view = View.inflate(context, R.layout.view_video_controller, this);
        mIvLike = view.findViewById(R.id.iv_like);
    }

    @Override
    public void onBind(String id, int videoType) {
    }

    @Override
    public void onProgressUpdate(String id, int videoType, int position, int duration) {
    }

    // 点赞、分享请勿监听点击事件，请使用下方回调；其他 UI 事件可以自行处理
    @Override
    public void onLikeClick(String id, int videoType, boolean like) {
        Log.d(TAG, "VideoControllerView onLikeClick" + String.valueOf(like));
//        Toast.makeText(getContext(), like ? "点赞" : "取消点赞", Toast.LENGTH_SHORT).show();
        mIvLike.setSelected(like);
    }

    @Override
    public void onShareClick(String id, int videoType, String videoUrl, String author, String title) {
        Log.d(TAG, "VideoControllerView onShareClick");
//        Toast.makeText(getContext(), "分享请勿监听点击事件", Toast.LENGTH_SHORT).show();
    }
}
