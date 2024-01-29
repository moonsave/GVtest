package custom.android.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

public class AnimationUtils extends Animation {
    private static AnimationUtils singleton = new AnimationUtils();

    public static AnimationUtils getInstance()
    {
        if ( singleton == null )
            singleton = new AnimationUtils();

        return singleton;
    }

    public static void refresh(ValueAnimator.AnimatorUpdateListener listener, int oldValue, int value)
    {
        if ( oldValue == value ) return;

        ValueAnimator animator = ValueAnimator.ofInt(oldValue, value);

        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(listener);

        if (animator.isRunning()) {
            animator.setIntValues(value);
        } else {
            animator.setDuration(1000);
            animator.start();
        }
    }

    public static void progressRefresh(ObjectAnimator animator, ProgressBar progress ,int oldValue, int value) {
        animator = ObjectAnimator.ofInt(progress, "progress", oldValue,value);
        animator.setInterpolator(new LinearInterpolator());
        if (animator.isRunning()) {
            animator.setIntValues(value);
        } else {
            animator.setDuration(400);
            animator.start();
        }
    }
}
