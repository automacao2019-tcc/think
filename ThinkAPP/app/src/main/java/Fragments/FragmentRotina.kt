package Fragments

import Activity.RotinaActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.ggslmrs.think.R
import kotlinx.android.synthetic.main.fragment_rotina.*

class FragmentRotina : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rotina, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rotina1.setOnClickListener {
            abrirRotina(1)
        }

        rotina2.setOnClickListener {
            abrirRotina(2)
        }
    }

    fun abrirRotina(index: Int){
        startActivity(Intent(context, RotinaActivity ::class.java))
    }

}