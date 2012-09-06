package by.fksis.schedule.ui;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import by.fksis.schedule.R;

public class SimpleLoaderView extends ImageView {

    private LevelListDrawable image;

    public SimpleLoaderView(Context context) {
        super(context);
    }

    public SimpleLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleLoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    protected void init() {
        image = (LevelListDrawable) getResources().getDrawable(R.drawable.loader);
        setImageDrawable(image);
        update();
        Animations.fadeIn(this, null);
    }

    private void update() {
        image.setLevel((image.getLevel() + 1) % 12);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, 100);
    }
}
