package com.mtgtech.weather_forecast.main.dialog;

//import static com.com.weather_forecast.main.MainActivity.isStartApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mtgtech.weather_forecast.R;
import com.mtgtech.weather_forecast.main.MainActivity;

public class DialogPer1 extends AppCompatActivity {

    public static BackgroundLocationDialog.OnSetButtonClickListener listener;

    public static void start(Context context) {
        Intent starter = new Intent(context, DialogPer1.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission_adr10);

        findViewById(R.id.goToPer2).setOnClickListener(v -> {
            listener.onSetButtonClicked();
            MainActivity.isGotoSettings = true;
            finish();
        });
    }
}
