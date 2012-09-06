package by.fksis.schedule.ui;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

public class Animations {

    public static void fadeOut(View view, AnimationListener l) {
        Animation a = new AlphaAnimation(1, 0);
        a.setDuration(500);
        a.setAnimationListener(l);
        view.startAnimation(a);
    }

    public static void fadeIn(View view, AnimationListener l) {
        Animation a = new AlphaAnimation(0, 1);
        a.setDuration(500);
        a.setAnimationListener(l);
        view.startAnimation(a);
    }

    public static void fadeOutAndHide(final View view) {
        fadeOut(view, new HidingAnimationListener(view));
    }

    public static void scaleIn(View view, AnimationListener l, float px, float py) {
        Animation a = new ScaleAnimation(0, 1, 0, 1, px * view.getWidth(), py * view.getHeight());
        a.setDuration(250);
        a.setAnimationListener(l);
        view.startAnimation(a);
    }

    public static void scaleOut(View view, AnimationListener l, float px, float py) {
        Animation a = new ScaleAnimation(1, 0, 1, 0, px * view.getWidth(), py * view.getHeight());
        a.setDuration(250);
        a.setAnimationListener(l);
        view.startAnimation(a);
    }

    public static void scaleOutAndHide(final View view, float px, float py) {
        scaleOut(view, new HidingAnimationListener(view), px, py);
    }

    public static void verticalCollapse(View view, float py) {
        Animation a = new ScaleAnimation(1, 1, 1, 0, 0, py * view.getHeight());
        a.setDuration(250);
        a.setAnimationListener(new HidingAnimationListener(view));
        view.startAnimation(a);
    }

    public static void verticalExpand(View view, float py) {
        Animation a = new ScaleAnimation(1, 1, 0, 1, 0, py * view.getHeight());
        view.setVisibility(View.VISIBLE);
        a.setDuration(250);
        view.startAnimation(a);
    }

    static class HidingAnimationListener implements AnimationListener {
        public View view;

        public HidingAnimationListener(View v) {
            view = v;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.GONE);
        }
    }
}
