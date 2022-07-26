package mtgtech.com.weather_forecast.main.dialog;

//import static mtgtech.com.weather_forecast.main.MainActivity.isStartApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.main.MainActivity;

public class DialogPer2 extends AppCompatActivity {

    public static BackgroundLocationDialog.OnSetButtonClickListener listener;

    public static void start(Context context) {
        Intent starter = new Intent(context, DialogPer2.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission_adr10_1);
        findViewById(R.id.goToSettings).setOnClickListener(v -> {
            listener.onSetButtonClicked();
            MainActivity.isGotoSettings = true;
            finish();
        });
    }

}
