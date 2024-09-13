import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.quics.login.R

class PopupManager(private val context: Context) {

    private val popupLayout: RelativeLayout
    private val popupTextView: TextView

    init {
        // Inflar el layout del popup
        popupLayout = LayoutInflater.from(context).inflate(R.layout.popup_layout, null) as RelativeLayout
        popupTextView = popupLayout.findViewById(R.id.msg_loader)

        // Configurar el popupLayout para que ocupe toda la pantalla
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        // Agregar el popup al layout raíz de la actividad
        (context as? AppCompatActivity)?.addContentView(popupLayout, params)
    }

    fun showPopup(message: String) {
        popupTextView.text = message
        popupLayout.visibility = View.VISIBLE
        setEnableDisableViewGroup((context as AppCompatActivity).findViewById(android.R.id.content), false)
    }

    fun hidePopup() {
        popupLayout.visibility = View.GONE
        setEnableDisableViewGroup((context as AppCompatActivity).findViewById(android.R.id.content), true)
    }

    private fun setEnableDisableViewGroup(viewGroup: ViewGroup, enabled: Boolean) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            child.isEnabled = enabled
            if (child is ViewGroup) {
                setEnableDisableViewGroup(child, enabled)
            }
        }
    }
}
