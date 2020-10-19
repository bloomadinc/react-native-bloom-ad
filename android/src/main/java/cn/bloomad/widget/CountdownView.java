package cn.bloomad.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.newssdk.AbstractCountdownView;

import cn.bloomad.R;

public class CountdownView extends AbstractCountdownView {

    private TextView mTvReward;
    private ImageView mIvRedPack;
    private DonutProgress mProgress;

    public CountdownView(Context context) {
        super(context);
        // 请按照设计修改布局
        View view = View.inflate(context, R.layout.view_countdown, this);
        mTvReward = view.findViewById(R.id.tv_reward);
        mIvRedPack = view.findViewById(R.id.iv_red_pack);
        mProgress = view.findViewById(R.id.progress);
        mProgress.setMax(1);
    }

    @Override
    public void onStart(Object rewardData) {
        mProgress.setProgress(0);
    }

    @Override
    public void onProgressUpdate(float progress, int totalSeconds) {
        // progress 在0到1之间
        mProgress.setProgress(progress);
    }

    @Override
    public void onEnd(boolean rewardSuccess, Object rewardData) {
        if (!rewardSuccess) {
            // 奖励失败
            removeFromParent();
            return;
        }

        mTvReward.setText("+" + rewardData);

        // 自定义奖励动画，请按照设计修改代码
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(makeRewardAnimator(), makeRedPackAnimator(), makeAlphaAnimator());

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTvReward.setVisibility(View.VISIBLE);
                mIvRedPack.setScaleX(0.9f);
                mIvRedPack.setScaleY(0.9f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeFromParent();

                CountdownView.this.setAlpha(1f);
                mTvReward.setVisibility(View.GONE);
                mIvRedPack.setRotation(0f);
                mIvRedPack.setScaleX(1f);
                mIvRedPack.setScaleY(1f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animatorSet.start();
    }

    private Animator makeRewardAnimator() {
        PropertyValuesHolder pvhTranslationY = PropertyValuesHolder.ofFloat("translationY", 0.0f, -5f);
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 0.1f, 1f);
        PropertyValuesHolder pvhTextSize = PropertyValuesHolder.ofFloat("textSize", 10f, 30f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTvReward, pvhTranslationY, pvhAlpha, pvhTextSize);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(1000);
        return animator;
    }

    private Animator makeRedPackAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvRedPack, "rotation", -20f, 20f);
        animator.setDuration(100).setRepeatCount(10);
        return animator;
    }

    private Animator makeAlphaAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setStartDelay(800);
        animator.setDuration(200);
        return animator;
    }
}
