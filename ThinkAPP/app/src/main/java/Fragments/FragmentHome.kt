package Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Activity.ComodoActivity
import DAO.BD
import DAO.ConfiguracaoFirebase
import Helper.comodoHelper

import br.com.ggslmrs.think.R
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_fragment_home.*


class FragmentHome: Fragment() {

    private lateinit var firebase: DatabaseReference
    private lateinit var helper: BD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebase = ConfiguracaoFirebase.getFirebase()
        helper = BD(context!!)

        obtemComodos()

        comodo_area.setOnClickListener {
            abriComodo(comodoHelper.AREA.name)
        }

        comodo_cozinha.setOnClickListener {
            abriComodo(comodoHelper.COZINHA.name)
        }

        comodo_quarto.setOnClickListener {
            abriComodo(comodoHelper.QUARTO.name)
        }

        comodo_sala.setOnClickListener {
            abriComodo(comodoHelper.SALA.name)
        }
    }

    private fun abriComodo(comodo: String){
        val intent = Intent(activity, ComodoActivity::class.java)
        intent.putExtra("comodo", comodo)
        startActivity(intent)
    }

    private fun obtemComodos(){
        helper.writableDatabase

    }
}
