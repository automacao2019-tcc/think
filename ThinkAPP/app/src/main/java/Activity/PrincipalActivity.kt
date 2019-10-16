package Activity

import DAO.BD
import DAO.ConfiguracaoFirebase
import Fragments.*
import Helper.BluetoothHelper
import Helper.BluetoothSerialService
import Modelos.Casa
import Modelos.Usuario
import android.bluetooth.BluetoothAdapter
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.ggslmrs.think.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {

    private val fm = supportFragmentManager
    private lateinit var firebase: DatabaseReference
    private lateinit var autenticacao: FirebaseAuth
    private lateinit var helper : BD
    private lateinit var key : String
    private val BT_ACTIVATE = 101
    private val BT_VISIBILITY = 102
    var servie: BluetoothSerialService? = null
    var state = BluetoothSerialService.STATE_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        if(servie == null){
            listarBluetoothsPareados()

            if(BluetoothHelper.getDefaultAdapter() == null)
                ErroBD.showDialogErro(supportFragmentManager, "Smartphone sem adaptador bluetooth")

            else if(!BluetoothHelper.verificaBluetoothHabilitado()){
                val it = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(it, BT_ACTIVATE)
            }
            else
                tornarVisivel()
        }

        home.setOnClickListener {
            FragmentHome.build(servie, supportFragmentManager)
        }

        rotinas.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentRotina())
            ft.commit()
        }

        novoUsuario.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentNovoPerfil())
            ft.commit()
        }

        configuracoes.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentConfigs())
            ft.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth()
        firebase = ConfiguracaoFirebase.getFirebase()
        helper = BD(this)

        //buscarUsuario()
    }

    private fun buscarUsuario(){
        loading.showDialog(fm)

        val email = autenticacao.currentUser?.email
        var user : Usuario
        var casa : String

        if(!email.isNullOrEmpty()){
            firebase.child("usuario").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    for(data in p0.children){
                        user = data.getValue(Usuario ::class.java) as Usuario

                        casa = user.casa

                        if (casa.isNotEmpty())
                            buscarCasa(casa)
                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

    private fun buscarCasa(casaKey:String){
        var casa : Casa
        this.key = casaKey

        firebase.child("casa").orderByKey().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children){
                    if(data.key!!.contains(casaKey)){
                        casa = data.getValue(Casa ::class.java) as Casa
                        gravaCasaBD(casa, casaKey)
                    }
                }

                loading.dismissDialog()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun gravaCasaBD(casa: Casa, key: String){
        if(getCasa(key))
            updateCasa(casa, key)

        else
            novaCasa(casa, key)
    }

    private fun getCasa(key: String) : Boolean{
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select *from casa where uuid = ?", arrayOf(key))
        cursor.moveToFirst()

        return (cursor != null)
    }

    private fun updateCasa(casa: Casa, key: String) {
        val db = helper.writableDatabase
        val values = ContentValues()

        values.put("comodos", casa.comodos)

        val resultado = db.update("casa", values, "uuid = ?", arrayOf(key))

        if(resultado == -1)
            ErroBD.showDialogErro(fm, "Erro ao atualizar casa")
    }

    private fun novaCasa(casa: Casa, key: String){
        val db = helper.writableDatabase
        val values = ContentValues()

        values.put("comodos", casa.comodos)
        values.put("uuid", key)

        val resultado = db.insert("casa", null, values)

        if(resultado.toInt() == -1)
            ErroBD.showDialogErro(fm, "Erro ao salvar casa")

        listarBluetooth()
    }

    private fun listarBluetooth() {
        val b = BluetoothHelper(supportFragmentManager)
        b.onReceive(baseContext, intent)
    }

    private fun listarBluetoothsPareados(){
        val c = ConexaoBluetoothProvisoria()
        val b  = BluetoothHelper()
        val l = b.obterDevicesPareados(this)

        c.showDialog(supportFragmentManager, l)
    }

    private fun tornarVisivel(){
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
        startActivityForResult(discoverableIntent, BT_VISIBILITY)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0)
            FragmentHome.build(servie, supportFragmentManager)
        else
            supportFragmentManager.popBackStack()
    }
}
