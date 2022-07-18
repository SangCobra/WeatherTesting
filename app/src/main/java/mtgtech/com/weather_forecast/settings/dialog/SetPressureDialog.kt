package mtgtech.com.weather_forecast.settings.dialog

import mtgtech.com.weather_forecast.OnActionCallback
import mtgtech.com.weather_forecast.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SetPressureDialog(contentLayoutId: Int = R.layout.dialog_setting_pressure) : AppCompatActivity(contentLayoutId) {
    companion object{
        var callback: OnActionCallback? = null
        fun start(context: Context, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, SetPressureDialog::class.java))
        }
        const val Type1=0
        const val Type2=1
        const val Type3=2
        const val Type4=3
        const val Type5 = 4
        const val Type6 = 5
        const val Type7 = 6
        var cur=0
    }
    private var unitType1 : RadioButton? = null
    private var unitType2 : RadioButton? = null
    private var unitType3 : RadioButton? = null
    private var unitType4 : RadioButton? = null
    private var unitType5 : RadioButton? = null
    private var unitType6 : RadioButton? = null
    private var unitType7 : RadioButton? = null
    private var borderPressureDialog : ConstraintLayout? = null
    private var bgUnitType1 : LinearLayout? = null
    private var bgUnitType2 : LinearLayout? = null
    private var bgUnitType3 : LinearLayout? = null
    private var bgUnitType4 : LinearLayout? = null
    private var bgUnitType5 : LinearLayout? = null
    private var bgUnitType6 : LinearLayout? = null
    private var bgUnitType7 : LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pressureUnitsTitle = resources.getStringArray(R.array.pressure_units)
        val pressureUnitsValues = resources.getStringArray(R.array.pressure_unit_values)
        unitType1 = findViewById(R.id.unitType1)
        unitType2 = findViewById(R.id.unitType2)
        unitType3 = findViewById(R.id.unitType3)
        unitType4 = findViewById(R.id.unitType4)
        unitType5 = findViewById(R.id.unitType5)
        unitType6 = findViewById(R.id.unitType6)
        unitType7 = findViewById(R.id.unitType7)
        borderPressureDialog = findViewById(R.id.border_pres)
        bgUnitType1 = findViewById(R.id.bg_unitType1)
        bgUnitType2 = findViewById(R.id.bg_unitType2)
        bgUnitType3 = findViewById(R.id.bg_unitType3)
        bgUnitType4 = findViewById(R.id.bg_unitType4)
        bgUnitType5 = findViewById(R.id.bg_unitType5)
        bgUnitType6 = findViewById(R.id.bg_unitType6)
        bgUnitType7 = findViewById(R.id.bg_unitType7)

        when (cur) {
            0 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_yes)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
                unitType6!!.setButtonDrawable(R.drawable.ic_no)
                unitType7!!.setButtonDrawable(R.drawable.ic_no)
            }
            1 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_yes)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
                unitType6!!.setButtonDrawable(R.drawable.ic_no)
                unitType7!!.setButtonDrawable(R.drawable.ic_no)
            }
            2 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_yes)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
                unitType6!!.setButtonDrawable(R.drawable.ic_no)
                unitType7!!.setButtonDrawable(R.drawable.ic_no)

            }
            3 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_yes)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
                unitType6!!.setButtonDrawable(R.drawable.ic_no)
                unitType7!!.setButtonDrawable(R.drawable.ic_no)
            }
            4 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_yes)
                unitType6!!.setButtonDrawable(R.drawable.ic_no)
                unitType7!!.setButtonDrawable(R.drawable.ic_no)
            }
            5 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
                unitType6!!.setButtonDrawable(R.drawable.ic_yes)
                unitType7!!.setButtonDrawable(R.drawable.ic_no)
            }
            6 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
                unitType4!!.setButtonDrawable(R.drawable.ic_no)
                unitType5!!.setButtonDrawable(R.drawable.ic_no)
                unitType6!!.setButtonDrawable(R.drawable.ic_no)
                unitType7!!.setButtonDrawable(R.drawable.ic_yes)
            }

        }
        borderPressureDialog!!.setOnClickListener {
            finish()
        }
        unitType1!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type1])
                cur = Type1
                finish()
            }

        }
        unitType2!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type2])
                cur = Type2
                finish()
            }

        }
        unitType3!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type3])
                cur = Type3
                finish()
            }

        }
        unitType4!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type4])
                cur = Type4
                finish()
            }

        }
        unitType5!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type5])
                cur = Type5
                finish()
            }

        }
        unitType6!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type6])
                cur = Type6
                finish()
            }

        }
        unitType7!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback(pressureUnitsValues[Type7])
                cur = Type7
                finish()
            }

        }
        bgUnitType1!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type1])
            cur = Type1
            finish()
        }
        bgUnitType2!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type2])
            cur = Type2
            finish()
        }
        bgUnitType3!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type3])
            cur = Type3
            finish()
        }
        bgUnitType4!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type4])
            cur = Type4
            finish()
        }
        bgUnitType5!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type5])
            cur = Type5
            finish()
        }
        bgUnitType6!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type6])
            cur = Type6
            finish()
        }
        bgUnitType7!!.setOnClickListener {
            callback?.callback(pressureUnitsValues[Type7])
            cur = Type7
            finish()
        }
    }


}