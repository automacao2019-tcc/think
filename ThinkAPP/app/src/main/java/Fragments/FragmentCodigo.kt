package Fragments

import Activity.PrincipalActivity
import DAO.ConfiguracaoFirebase
import Helper.MetodoLoginHelper
import Modelos.Codigo
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ggslmrs.think.R
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_codigo.*
import java.lang.Exception
import java.util.*

class FragmentCodigo: DialogFragment() {

    private lateinit var firebase: DatabaseReference
    private lateinit var metodoLogin: String
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1000
    private lateinit var autenticacao : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_codigo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context!!, gso)

        btnValidar.setOnClickListener {
            validar(edtCodigoDeSegurancaInsr.text.toString())
        }
    }

    private fun validar(codigo: String){
        firebase = ConfiguracaoFirebase.getFirebase()
        var cod: Codigo
        var estaValidado = false

        firebase.child("codigos").orderByChild("codigo").equalTo(codigo).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children){
                    if(data != null){
                        cod = data.getValue(Codigo ::class.java) as Codigo
                        estaValidado = cod.codigo.isNotEmpty()
                    }
                }

                if(estaValidado)
                    fazerLogin()
                else
                    Toast.makeText(context, "Esse código não é válido", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun fazerLogin(){
        if(metodoLogin.equals(MetodoLoginHelper.FACEBOOK.name))
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        else{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    fun show(manager: FragmentManager?, tag: String?, metodoLogin: String) {
        this.metodoLogin  = metodoLogin
        super.show(manager, tag)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException ::class.java)
                if(account != null)
                    logarComGoogle(account)

            }catch (e: Exception){
                Log.e("ErroLogin", e.toString())
                Toast.makeText(context, "Não foi possível fazer o login com sua conta do google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logarComGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        autenticacao.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful)
                abrirTelaPrincipal()
            else
                Toast.makeText(context, "Não foi possível efetuar o login pela sua conta do google", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirTelaPrincipal(){
        startActivity(Intent(context, PrincipalActivity ::class.java))
    }
}