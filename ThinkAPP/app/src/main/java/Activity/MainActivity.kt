package Activity

import DAO.BD
import DAO.ConfiguracaoFirebase
import Fragments.ErroBD
import Fragments.FragmentCodigo
import Fragments.loading
import Helper.MetodoLoginHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ggslmrs.think.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var callbackManager = CallbackManager.Factory.create()
    private lateinit var autenticacao : FirebaseAuth
    private lateinit var helper: BD

    override fun onResume() {
        super.onResume()
        this.helper = BD(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accessToken = AccessToken.getCurrentAccessToken()
        val estaLogado = (accessToken != null && !accessToken.isExpired) || (FirebaseAuth.getInstance().currentUser != null)

        if(estaLogado)
            abrirTelaPrincipal()

        btnLoginGoogle.setOnClickListener {
            val fragment = FragmentCodigo()
            fragment.show(supportFragmentManager, "", MetodoLoginHelper.GOOGLE.name)
        }

        novoCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        btnLoginFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }

        btnEntrar.setOnClickListener {
            fazerLogin()
        }

        button_login_facebook.setReadPermissions("email")

        //retorna se o login foi efetuado ou não
        button_login_facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val fragment = FragmentCodigo()
                fragment.show(supportFragmentManager, "", MetodoLoginHelper.FACEBOOK.name)
            }

            override fun onCancel() {
                loading.dismissDialog()
            }

            override fun onError(exception: FacebookException) {
                loading.dismissDialog()
                ErroBD.showDialogErro(supportFragmentManager, exception.toString())
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun fazerLogin(){
        loading.showDialog(supportFragmentManager)
        if(!edtEmail.text.toString().equals("") && !edtSenha.text.toString().equals("")){
            autenticacao = ConfiguracaoFirebase.getFirebaseAuth()
            autenticacao.signInWithEmailAndPassword(edtEmail.text.toString(), edtSenha.text.toString()).addOnCompleteListener {
                task ->
                if(task.isSuccessful)
                    abrirTelaPrincipal()

                else{
                    loading.dismissDialog()
                    ErroBD.showDialogErro(supportFragmentManager, "Não é possível fazer login")
                }
            }
        }
    }

    private fun abrirTelaPrincipal(){
        loading.dismissDialog()
        startActivity(Intent(this, PrincipalActivity ::class.java))
    }
}
