package mtgtech.com.weather_forecast.view.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import mtgtech.com.weather_forecast.view.weather_widget.InkPageIndicator;

/**
 * Ink page indicator behavior.
 */

public class InkPageIndicatorBehavior<V extends InkPageIndicator> extends CoordinatorLayout.Behavior<V> {

    public InkPageIndicatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull V child,
                                 int layoutDirection) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int cx = parent.getMeasuredWidth() / 2;
        child.layout(
                cx - child.getMeasuredWidth() / 2,
                parent.getMeasuredHeight() - child.getMeasuredHeight() - params.topMargin - params.bottomMargin,
                cx + child.getMeasuredWidth() / 2,
                parent.getMeasuredHeight() - params.bottomMargin
        );
        return true;
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child,
                                   @NonNull View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child,
                                          @NonNull View dependency) {
        child.setTranslationY(dependency.getY() - parent.getMeasuredHeight());
        return false;
    }
}
