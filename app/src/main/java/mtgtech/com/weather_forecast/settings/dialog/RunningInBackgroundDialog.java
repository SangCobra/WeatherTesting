package mtgtech.com.weather_forecast.settings.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.weather_model.GeoActivity;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

/**
 * Running in background dialog.
 * */
@RequiresApi(api = Build.VERSION_CODES.M)
public class RunningInBackgroundDialog extends DialogFragment {

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_running_in_background, null, false);
        view.findViewById(R.id.dialog_running_in_background_setBtn).setOnClickListener(v ->
                IntentHelper.startBatteryOptimizationActivity((GeoActivity) requireActivity()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }


}
