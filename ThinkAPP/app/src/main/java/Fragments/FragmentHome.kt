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
import Helper.BluetoothSerialService
import Helper.comodoHelper
import android.app.Activity
import android.util.Log
import androidx.fragment.app.FragmentManager

import br.com.ggslmrs.think.R
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_fragment_home.*
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FragmentHome: Fragment() {

    private lateinit var firebase: DatabaseReference
    private lateinit var helper: BD
    private var service : BluetoothSerialService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebase = ConfiguracaoFirebase.getFirebase()
        helper = BD(context!!)

        //val map = obtemComodos()

        comodo_cozinha.setOnClickListener {
            abriComodo(comodoHelper.COZINHA.name, false)
        }

        comodo_quarto_1.setOnClickListener {
            abriComodo(comodoHelper.QUARTO.name, false)
        }

        comodo_quarto_2.setOnClickListener {
            abriComodo(comodoHelper.QUARTO.name, true)
        }

        comodo_sala.setOnClickListener {
            abriComodo(comodoHelper.SALA.name, false)
        }

        comodo_banheiro_1.setOnClickListener {
            abriComodo(comodoHelper.BANHEIRO.name, false)
        }

        comodo_banheiro_2.setOnClickListener {
            abriComodo(comodoHelper.BANHEIRO.name, true)
        }
    }

    private fun abriComodo(comodo: String, isSecond : Boolean){
        /*val intent = Intent(activity, ComodoActivity::class.java)
        intent.putExtra("comodo", comodo)
        intent.putExtra("segundo", isSecond)
        if(service != null)
            intent.putExtra("service", service)
        startActivityForResult(intent, BluetoothSerialService.REQUEST_CODE_FOR_INTENT)*/
        ComodoActivity.build(service, isSecond, comodo, fragmentManager)
    }

    /*private fun obtemComodos() : ArrayList<HashMap<String, String>> {
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

    private fun convertToMap(comodo: JSONObject) : HashMap<String, String> {
        val map = HashMap<String, String>()
        val keys = comodo.keys()

        while (keys.hasNext()){
            val key = keys.next()
            map.put(key, "${comodo.get(key)}")
        }

        return map
    }*/

    companion object {
        private val instance = FragmentHome()
        private fun getInstance() = instance

        fun build(service : BluetoothSerialService?, fm : FragmentManager?){
            with(getInstance()){
                this.service = service

                val ft = fm?.beginTransaction()
                ft?.replace(R.id.exibiFragment, this)
                ft?.commit()
            }
        }
    }
}
