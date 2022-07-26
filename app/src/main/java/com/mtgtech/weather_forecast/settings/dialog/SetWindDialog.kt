package com.mtgtech.weather_forecast.settings.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.mtgtech.weather_forecast.OnActionCallback
import com.mtgtech.weather_forecast.R
import com.mtgtech.weather_forecast.settings.SettingsOptionManager

class SetWindDialog(contentLayoutId: Int = R.layout.dialog_setting_windpeed) :
    AppCompatActivity(contentLayoutId) {
    companion object {
        var callback: OnActionCallback? = null
        fun start(context: Context, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, SetWindDialog::class.java))
        }

        const val Type1 = 0
        const val Type2 = 1
        const val Type3 = 2
        const val Type4 = 3
        const val Type5 = 4
        var cur = 0
    }

    private var unitType1: RadioButton? = null
    private var unitType2: RadioButton? = null
    private var unitType3: RadioButton? = null
    private var unitType4: RadioButton? = null
    private var unitType5: RadioButton? = null
    private var borderWindDialog: ConstraintLayout? = null
    private var bgUnitType1: LinearLayout? = null
    private var bgUnitType2: LinearLayout? = null
    private var bgUnitType3: LinearLayout? = null
    private var bgUnitType4: LinearLayout? = null
    private var bgUnitType5: LinearLayout? = null
    private var settingsOptionManager: SettingsOptionManager =
        SettingsOptionManager.getInstance(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val unitsTitle = resources.getStringArray(R.array.speed_units)
        val unitsValues = resources.getStringArray(R.array.speed_unit_values)
        cur = unitsTitle.indexOf(settingsOptionManager.speedUnit.getAbbreviation(this))
        unitType1 = findViewById(R.id.unitType1)
        unitType2 = findViewById(R.id.unitType2)
        unitType3 = findViewById(R.id.unitType3)
        unitType4 = findViewById(R.id.unitType4)
        unitType5 = findViewById(R.id.unitType5)
        borderWindDialog = findViewById(R.id.border_wind)
        bgUnitType1 = findViewById(R.id.bg_unitType1)
        bgUnitType2 = findViewById(R.id.bg_unitType2)
        bgUnitType3 = findViewById(R.id.bg_unitType3)
        bgUnitType4 = findViewById(R.id.bg_unitType4)
        bgUnitType5 = findViewById(R.id.bg_unitType5)

        when (cur) {
            0 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_yes)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
            }
            1 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_yes)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
            }
            2 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_yes)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)

            }
            3 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_yes)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)

            }
            4 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_yes)

            }

        }
        borderWindDialog!!.setOnClickListener {
            finish()
        }
        unitType1!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                callback?.callback(unitsValues[Type1])
                cur = Type1

                finish()
            }

        }
        unitType2!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                callback?.callback(unitsValues[Type2])
                cur = Type2

                finish()
            }

        }
        unitType3!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                callback?.callback(unitsValues[Type3])
                cur = Type3

                finish()
            }

        }
        unitType4!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                callback?.callback(unitsValues[Type4])
                cur = Type4

                finish()
            }

        }
        unitType5!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                callback?.callback(unitsValues[Type5])
                cur = Type5

                finish()
            }

        }
        bgUnitType1!!.setOnClickListener {
            callback?.callback(unitsValues[Type1])
            cur = Type1

            finish()
        }
        bgUnitType2!!.setOnClickListener {
            callback?.callback(unitsValues[Type2])
            cur = Type2

            finish()
        }
        bgUnitType3!!.setOnClickListener {
            callback?.callback(unitsValues[Type3])
            cur = Type3

            finish()
        }
        bgUnitType4!!.setOnClickListener {
            callback?.callback(unitsValues[Type4])
            cur = Type4

            finish()
        }
        bgUnitType5!!.setOnClickListener {
            callback?.callback(unitsValues[Type5])
            cur = Type5

            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

}

