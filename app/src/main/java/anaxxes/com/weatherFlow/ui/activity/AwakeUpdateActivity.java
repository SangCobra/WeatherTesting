package anaxxes.com.weatherFlow.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.utils.helpter.IntentHelper;

public class AwakeUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), R.string.refresh, Toast.LENGTH_SHORT).show();
        IntentHelper.startAwakeForegroundUpdateService(getApplicationContext());
        finish();
    }
}
