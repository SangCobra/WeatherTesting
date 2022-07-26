package mtgtech.com.weather_forecast.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.utils.helpter.IntentHelper;

public class AwakeUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), R.string.refresh, Toast.LENGTH_SHORT).show();
        IntentHelper.startAwakeForegroundUpdateService(getApplicationContext());
        finish();
    }
}
