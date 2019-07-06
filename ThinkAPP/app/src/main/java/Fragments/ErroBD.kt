package Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ggslmrs.think.R
import kotlinx.android.synthetic.main.fragment_erro_bd.*

class ErroBD : DialogFragment() {

    private var texto = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_erro_bd, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtErro.setText(this.texto)

        btnOkayDismissDialog.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private val instance = ErroBD()

        private fun getInstance() = instance

        fun showDialogErro(fragmentManager: FragmentManager, texto: String?){
            with(getInstance()){
                isCancelable = false

                if(texto != null && texto.isNotEmpty())
                    this.texto = texto

                show(fragmentManager, "")
            }
        }
    }
}