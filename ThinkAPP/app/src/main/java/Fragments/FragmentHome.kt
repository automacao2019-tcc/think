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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable

import br.com.ggslmrs.think.R
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.card_view.*
import kotlinx.android.synthetic.main.fragment_fragment_home.*
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FragmentHome(key: String): Fragment() {

    private lateinit var firebase: DatabaseReference
    private lateinit var helper: BD
    private var key: String

    init {
        this.key = key
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebase = ConfiguracaoFirebase.getFirebase()
        helper = BD(context!!)

        val map = obtemComodos()


        /*for (i in 0..map.size){
            val comodo = map[i]

            if(comodo.containsKey(comodoHelper.AREA.name)){
                val card = view_card
                card.setOnClickListener {
                    abriComodo(comodoHelper.AREA.name)
                }

                val icone = card.getChildAt(R.id.icone_comodo) as ImageView
                val descricao = card.getChildAt(R.id.descricao_comodo) as TextView

                icone.setImageDrawable(R.drawable.ic_area.toDrawable())
                descricao.text = comodoHelper.AREA.name

                layoutComodos.addView(card)
            }

            else if(comodo.containsKey(comodoHelper.COZINHA.name)){
                val card = view_card
                card.setOnClickListener {
                    abriComodo(comodoHelper.COZINHA.name)
                }

                val icone = card.getChildAt(R.id.icone_comodo) as ImageView
                val descricao = card.getChildAt(R.id.descricao_comodo) as TextView

                icone.setImageDrawable(R.drawable.ic_cozinha.toDrawable())
                descricao.text = comodoHelper.COZINHA.name

                layoutComodos.addView(card)
            }

            else if(comodo.containsKey(comodoHelper.QUARTO.name)){
                val card = view_card
                card.setOnClickListener {
                    abriComodo(comodoHelper.QUARTO.name)
                }

                val icone = card.getChildAt(R.id.icone_comodo) as ImageView
                val descricao = card.getChildAt(R.id.descricao_comodo) as TextView

                icone.setImageDrawable(R.drawable.ic_quarto.toDrawable())
                descricao.text = comodoHelper.QUARTO.name

                layoutComodos.addView(card)
            }

            else if(comodo.containsKey(comodoHelper.SALA.name)){
                val card = view_card
                card.setOnClickListener {
                    abriComodo(comodoHelper.SALA.name)
                }

                val icone = card.getChildAt(R.id.icone_comodo) as ImageView
                val descricao = card.getChildAt(R.id.descricao_comodo) as TextView

                icone.setImageDrawable(R.drawable.ic_sala.toDrawable())
                descricao.text = comodoHelper.SALA.name

                layoutComodos.addView(card)
            }

            else if(comodo.containsKey(comodoHelper.COPA.name)){
                val card = view_card
                card.setOnClickListener {
                    abriComodo(comodoHelper.COPA.name)
                }

                val icone = card.getChildAt(R.id.icone_comodo) as ImageView
                val descricao = card.getChildAt(R.id.descricao_comodo) as TextView

                icone.setImageDrawable(R.drawable.ic_copa.toDrawable())
                descricao.text = comodoHelper.COPA.name

                layoutComodos.addView(card)
            }

            else if(comodo.containsKey(comodoHelper.BANHEIRO.name)){
                val card = view_card
                card.setOnClickListener {
                    abriComodo(comodoHelper.BANHEIRO.name)
                }

                val icone = card.getChildAt(R.id.icone_comodo) as ImageView
                val descricao = card.getChildAt(R.id.descricao_comodo) as TextView

                icone.setImageDrawable(R.drawable.ic_banheiro.toDrawable())
                descricao.text = comodoHelper.BANHEIRO.name

                layoutComodos.addView(card)
            }
        }*/

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

    private fun obtemComodos() : ArrayList<HashMap<String, String>> {
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select *from casa where uuid = ?", arrayOf(this.key))
        cursor.moveToFirst()
        var comodos: Array<JSONObject>? = null

        for ( i in 0..cursor.count){
            try{
                val index = cursor.getColumnIndex("comodos")
                comodos = arrayOf(JSONObject(cursor.getString(index)))
            }
            catch (e: Exception){
                ErroBD.showDialogErro(fragmentManager!!, "Impossível obter o objeto JSON")
            }
        }

        val arr = arrayListOf<HashMap<String, String>>()

        if(comodos != null && comodos.isNotEmpty()){
            for(c in comodos)
                arr.add(convertToMap(c))
        }

        return arr
    }

    private fun convertToMap(comodo: JSONObject) : HashMap<String, String>{
        val map = HashMap<String, String>()
        val keys = comodo.keys()

        while (keys.hasNext()){
            val key = keys.next()
            map.put(key, "${comodo.get(key)}")
        }

        return map
    }
}
