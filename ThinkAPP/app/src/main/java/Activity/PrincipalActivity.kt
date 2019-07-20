package Activity

import DAO.BD
import DAO.ConfiguracaoFirebase
import Fragments.*
import Modelos.Casa
import Modelos.Usuario
import android.content.ContentValues
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        home.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentHome(this.key))
            ft.commit()
        }

        rotinas.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentRotina())
            ft.commit()
        }

        /*perfil.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentPerfil())
            ft.commit()
        }*/

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

        buscarUsuario()
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
    }


}
