package anaxxes.com.weatherFlow.main;

import android.content.Context;

import anaxxes.com.weatherFlow.ui.decotarion.ListDecoration;
import anaxxes.com.weatherFlow.utils.manager.ThemeManager;

/**
 * Main list decoration.
 * */

public class MainListDecoration extends ListDecoration {

    public MainListDecoration(Context context) {
        super(
                context,
                ThemeManager.getInstance(context).getLineColor(context)
        );
    }
}
