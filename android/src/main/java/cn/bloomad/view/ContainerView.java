package cn.bloomad.view;

import android.content.Context;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mmpkl05 on 12/14/17.
 */

public class ContainerView extends ViewGroup {

    private Context context;

    public ContainerView(Context context) {
        super(context);
        this.context = context;

        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
                    child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                }
                getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    @Override
    public void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        Log.i("ContainerView", String.valueOf(var1));
    }
}