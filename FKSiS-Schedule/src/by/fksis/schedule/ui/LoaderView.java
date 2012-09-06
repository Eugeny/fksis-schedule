package by.fksis.schedule.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;
import by.fksis.schedule.R;

public class LoaderView extends SimpleLoaderView {
    boolean hasBackground;

    public LoaderView(Context context, boolean bg) {
        super(context);
        hasBackground = bg;
    }

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static LoaderView createOn(RelativeLayout parent, boolean hasBackground) {
        LoaderView view = new LoaderView(parent.getContext(), hasBackground);
        parent.addView(view);
        return view;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    @Override
    protected void init() {
        super.init();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setLayoutParams(lp);
        setScaleType(ScaleType.CENTER);
        if (hasBackground)
            setBackgroundResource(R.drawable.loader_dim);
    }

    public void finish() {
        Animations.fadeOut(this, new AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                ((RelativeLayout) getParent()).removeView(LoaderView.this);
            }
        });
    }
}
