package anaxxes.com.weatherFlow.main.dialog

import anaxxes.com.weatherFlow.OnActionCallback
import anaxxes.com.weatherFlow.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SetPrecipitationDialog(contentLayoutId: Int = R.layout.dialog_setting_precipitation) : AppCompatActivity(contentLayoutId) {
    companion object{
        var callback: OnActionCallback? = null
        fun start(context: Context, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, SetPrecipitationDialog::class.java))
        }
        const val Type1=0
        const val Type2=1
        const val Type3=2
        var cur=0
    }
    private var unitType1 : RadioButton? = null
    private var unitType2 : RadioButton? = null
    private var unitType3 : RadioButton? = null
    private var borderPrecipitationDialog : ConstraintLayout? = null
    private var bgUnitType1 : LinearLayout ? = null
    private var bgUnitType2 : LinearLayout ? = null
    private var bgUnitType3 : LinearLayout ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val unitsTitle = resources.getStringArray(R.array.precipitation_units)
        val unitsValues = resources.getStringArray(R.array.precipitation_unit_values)
        unitType1 = findViewById(R.id.unitType1)
        unitType2 = findViewById(R.id.unitType2)
        unitType3 = findViewById(R.id.unitType3)
        borderPrecipitationDialog = findViewById(R.id.border_prep)
        bgUnitType1 = findViewById(R.id.bg_unitType1)
        bgUnitType2 = findViewById(R.id.bg_unitType2)
        bgUnitType3 = findViewById(R.id.bg_unitType3)
        when (cur) {
            0 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_yes)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
            }
            1 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_yes)
                unitType3!!.setButtonDrawable(R.drawable.ic_no)
            }
            2 -> {
                unitType1!!.setButtonDrawable(R.drawable.ic_no)
                unitType2!!.setButtonDrawable(R.drawable.ic_no)
                unitType3!!.setButtonDrawable(R.drawable.ic_yes)
            }
        }
        borderPrecipitationDialog!!.setOnClickListener {
            finish()
        }
        unitType1!!.setOnCheckedChangeListener { _, b ->
            if (b){
                callback?.callback(unitsValues[Type1])
                cur = Type1

                finish()
            }

        }
        unitType2!!.setOnCheckedChangeListener { _, b ->
            if (b){
                callback?.callback(unitsValues[Type2])
                cur = Type2
                finish()
            }

        }
        unitType3!!.setOnCheckedChangeListener { _, b ->
            if (b){
                callback?.callback(unitsValues[Type3])
                cur = Type3
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
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

}