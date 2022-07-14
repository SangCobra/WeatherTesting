package anaxxes.com.weatherFlow.main.dialog

import anaxxes.com.weatherFlow.OnActionCallback
import anaxxes.com.weatherFlow.R
import anaxxes.com.weatherFlow.settings.SettingsOptionManager
import anaxxes.com.weatherFlow.utils.DisplayUtils
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingNavDialog(contentLayoutId: Int  = R.layout.dialog_setting_nav) : AppCompatActivity(contentLayoutId) {
    companion object{

        var callback: OnActionCallback? = null
        fun start(context: Context, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, SettingNavDialog::class.java))
        }

    }

    private var settingsOptionManager: SettingsOptionManager? = null
    private var borderSetNav : LinearLayout? = null
    private var doneButton : Button? = null
    private var borderDetails : LinearLayout? = null
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchChangeTime : Switch? = null
    private var tvPrep : EditText? = null
    private var tvPressure : EditText? = null
    private var tvtypeWind : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsOptionManager = SettingsOptionManager.getInstance(this)
        borderSetNav = findViewById(R.id.border_set_nav)
        doneButton = findViewById(R.id.btn_done_set_nav)
        borderDetails = findViewById(R.id.border_detail)
//        switchChangeTime = findViewById(R.id.switch_change_time)
        tvPrep = findViewById(R.id.etNavPrecipUnit)
        tvPressure = findViewById(R.id.etNavPressureUnit)
        tvtypeWind = findViewById(R.id.etNavSpeedUnit)
//        DisplayUtils.disableEditText(binding.navLayout.etNavTempUnit)
        DisplayUtils.disableEditText(tvPressure)
//        DisplayUtils.disableEditText(binding.navLayout.etNavDistanceUnit)
        DisplayUtils.disableEditText(tvtypeWind)
        DisplayUtils.disableEditText(tvPrep)
//        DisplayUtils.disableEditText(binding.navLayout.etNavRefreshRate)


//        binding.navLayout.etNavTempUnit.setText(
//            settingsOptionManager!!.temperatureUnit.getAbbreviation(
//                mainActivity
//            )
//        )
        (tvPressure)!!.setText(
            settingsOptionManager!!.pressureUnit.getAbbreviation(
                this
            )
        )
//        binding.navLayout.etNavDistanceUnit.setText(
//            settingsOptionManager!!.distanceUnit.getAbbreviation(
//                mainActivity
//            )
//        )
        (tvtypeWind)!!.setText(
            settingsOptionManager!!.speedUnit.getAbbreviation(
                this
            )
        )
        (tvPrep)!!.setText(
            settingsOptionManager!!.precipitationUnit.getAbbreviation(
                this
            )
        )
//        binding.navLayout.etNavRefreshRate.setText(
//            settingsOptionManager!!.updateInterval.getUpdateIntervalName(
//                mainActivity
//            )
//        )
        borderSetNav!!.setOnClickListener {
            finish()
        }
        doneButton!!.setOnClickListener {
            callback?.callback("done")
            finish()
        }
        borderDetails!!.setOnClickListener {

        }
//        switchChangeTime!!.isChecked
//        switchChangeTime!!.setOnCheckedChangeListener { _, isChecked ->
//        }
        findViewById<LinearLayout>(R.id.llNavPrecip).setOnClickListener {
            showPrecipUnitDialog()
        }
        findViewById<LinearLayout>(R.id.llNavPressure).setOnClickListener {
            showPressureUnitDialog()
        }
        findViewById<LinearLayout>(R.id.llNavSpeedUnit).setOnClickListener {
            showSpeedUnitDialog()
        }
        
    }
    // Convert temperature

    private fun showSpeedUnitDialog() {
        val unitsTitle = resources.getStringArray(R.array.speed_units)
        val unitsValues = resources.getStringArray(R.array.speed_unit_values)
        SetWindDialog.start(this,object: OnActionCallback {
            @SuppressLint("SetTextI18n")
            override fun callback(key: String?, vararg data: Any?) {
                for (i in unitsValues.indices){
                    if (key.equals(unitsValues[i])){
                        settingsOptionManager!!.setSpeedUnit(key)
                        tvtypeWind!!.setText(settingsOptionManager!!.speedUnit.getAbbreviation(this@SettingNavDialog))
                    }

                }
            }

        })
    }
//
//    fun showDistanceUnitDialog() {
//        val unitsTitle = resources.getStringArray(R.array.distance_units)
//        val unitsValues = resources.getStringArray(R.array.distance_unit_values)
//        val builder = AlertDialog.Builder(this@MainActivity)
//        builder.setTitle(getString(R.string.settings_title_distance_unit))
//        builder.setItems(unitsTitle) { dialog: DialogInterface?, which: Int ->
//            settingsOptionManager!!.setDistanceUnit(unitsValues[which])
//            binding.navLayout.etNavDistanceUnit.setText(
//                settingsOptionManager.distanceUnit.getAbbreviation(this)
//            )
//
//        }
//        val dialog = builder.create()
//        dialog.show()
//    }

    private fun showPrecipUnitDialog() {
        val unitsTitle = resources.getStringArray(R.array.precipitation_units)
        val unitsValues = resources.getStringArray(R.array.precipitation_unit_values)
        SetPrecipitationDialog.start(this, object : OnActionCallback {
            @SuppressLint("SetTextI18n")
            override fun callback(key: String?, vararg data: Any?) {
                for (i in unitsValues.indices){
                    if (key.equals(unitsValues[i])){
                        settingsOptionManager!!.setPrecipitationUnit(key)
                        tvPrep!!.setText(settingsOptionManager!!.precipitationUnit.getAbbreviation(this@SettingNavDialog))
                    }

                }
            }

        })
    }

    private fun showPressureUnitDialog() {
        val pressureUnitsTitle = resources.getStringArray(R.array.pressure_units)
        val pressureUnitsValues = resources.getStringArray(R.array.pressure_unit_values)
        SetPressureDialog.start(this,object: OnActionCallback {
            @SuppressLint("SetTextI18n")
            override fun callback(key: String?, vararg data: Any?) {
                for (i in pressureUnitsValues.indices){
                    if (key.equals(pressureUnitsValues[i])){
                        settingsOptionManager!!.setPressureUnit(key)
                        tvPressure!!.setText(settingsOptionManager!!.pressureUnit.getAbbreviation(this@SettingNavDialog))
                    }

                }
            }

        })
    }



}