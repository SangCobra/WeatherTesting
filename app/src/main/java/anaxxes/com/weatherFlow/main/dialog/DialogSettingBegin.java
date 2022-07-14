package anaxxes.com.weatherFlow.main.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import anaxxes.com.weatherFlow.OnActionCallback;
import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.GeoActivity;
import anaxxes.com.weatherFlow.main.MainActivity;
import anaxxes.com.weatherFlow.settings.SettingsOptionManager;

public class DialogSettingBegin extends AppCompatActivity {

    public static OnActionCallback callbackDialog;
    public static MainActivity activityMain;

    public static void start(Context context, OnActionCallback onActionCallback, MainActivity activity) {
        Intent starter = new Intent(context, DialogSettingBegin.class);
        context.startActivity(starter);
        callbackDialog = onActionCallback;
        activityMain = activity;
    }
    private FrameLayout borderLayout;
    private LinearLayout insideLayout;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch changeTerm, changeTime, lockScreen, notify, status;
    private Button done;
    private SettingsOptionManager settingsOptionManager = SettingsOptionManager.getInstance(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings_begin);
        borderLayout = findViewById(R.id.border_settings_begin);
        insideLayout = findViewById(R.id.in_settings_begin);
        changeTerm = findViewById(R.id.switch_to_change_term_type);
        changeTime = findViewById(R.id.switch_to_change_time_format);
        lockScreen = findViewById(R.id.set_lock_screen);
        notify = findViewById(R.id.set_notification);
        status = findViewById(R.id.set_status_bar);
        done = findViewById(R.id.done_settings_begin);

        changeTerm.setChecked(false);
        changeTime.setChecked(false);
        lockScreen.setChecked(false);
        notify.setChecked(false);
        status.setChecked(false);
        borderLayout.setOnClickListener(v -> {
            finish();
        });
        insideLayout.setOnClickListener(v -> {});
        changeTerm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()){
                settingsOptionManager.setTermChange(isChecked);
            }
        });
        changeTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            callbackDialog.callback("changeTime_" + isChecked);
        });
        lockScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()){
                settingsOptionManager.setNotificationHideInLockScreenEnabled(!isChecked);
            }
        });
        notify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()){
                settingsOptionManager.setNotificationEnabled(isChecked);
            }
        });
        status.setOnCheckedChangeListener((buttonView, isChecked) -> {
            callbackDialog.callback("status_" + isChecked);
        });
        done.setOnClickListener(v -> {
            activityMain.onRefresh();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callbackDialog = null;
    }
}
