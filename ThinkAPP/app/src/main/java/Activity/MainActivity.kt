package br.com.ggslmrs.think.Activity

import DAO.ConfiguracaoFirebase
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accessToken = AccessToken.getCurrentAccessToken()
        val estaLogado = (accessToken != null && !accessToken.isExpired) || (FirebaseAuth.getInstance().currentUser != null)

        if(estaLogado)
            abrirTelaPrincipal()

        btnLoginFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }

        btnEntrar.setOnClickListener {
            fazerLogin()
        }

        button_login_facebook.setReadPermissions("email")

        //retorna se o login foi efetuado ou não
        button_login_facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                abrirTelaPrincipal()
            }

            override fun onCancel() {

            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(baseContext, "Login não pode ser efetuado com sucesso", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun fazerLogin(){
        if(!edtEmail.text.toString().equals("") && !edtSenha.text.toString().equals("")){
            autenticacao = ConfiguracaoFirebase.getFirebaseAuth()
            autenticacao.signInWithEmailAndPassword(edtEmail.text.toString(), edtSenha.text.toString()).addOnCompleteListener {
                task ->
                if(task.isSuccessful)
                    abrirTelaPrincipal()

                else
                    Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirTelaPrincipal(){

    }
}
