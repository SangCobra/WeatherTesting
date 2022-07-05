package anaxxes.com.weatherFlow.main.dialog

import anaxxes.com.weatherFlow.OnActionCallback
import anaxxes.com.weatherFlow.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DeleteDialog(contentLayoutId: Int = R.layout.dialog_delete_location
) : AppCompatActivity(contentLayoutId) {

    companion object {
        var callback: OnActionCallback? = null

        fun start(context: Context, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, DeleteDialog::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<TextView>(R.id.bt_delete).setOnClickListener {
            callback?.callback("delete")
            finish()
        }

        findViewById<TextView>(R.id.bt_cancel).setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

}