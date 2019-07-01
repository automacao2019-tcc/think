package Activity.telas_cadastro

import Activity.PrincipalActivity
import DAO.ConfiguracaoFirebase
import Modelos.Usuario
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.ggslmrs.think.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.lang.Exception

class CadastroActivity : AppCompatActivity() {

    private lateinit var autenticacao : FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btnCadastrar.setOnClickListener {
            novocadastro()
        }
    }


    private fun novocadastro(){
        val usuario = Usuario()
        usuario.email = edtCadastroEmail.text.toString()
        usuario.senha = edtCadastroSenha.text.toString()
        usuario.nome = edtCadastroNome.text.toString()


        autenticacao = ConfiguracaoFirebase.getFirebaseAuth()
        autenticacao.createUserWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener (this) {
            if(it.isSuccessful){
                insereUsuario(usuario)
                finish()
                //autenticacao.signOut()
            }

            else {
                var erroExcecao = ""
                try {
                    throw it.exception!!
                }catch (e : FirebaseAuthWeakPasswordException){
                    erroExcecao = "Digite uma senha mais forte"
                }catch (e : FirebaseAuthInvalidCredentialsException){
                    erroExcecao="Digite um email válido0"
                }catch (e : FirebaseAuthUserCollisionException){
                    erroExcecao="Esse email já está cadastrado"
                }catch (e : Exception){
                    erroExcecao = "Erro: $e"
                    Log.v("Erro: ", e.toString())
                }
                Toast.makeText(this, erroExcecao, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun insereUsuario(usuario: Usuario){
        try {
            reference = ConfiguracaoFirebase.getFirebase().child("usuario")
            reference.push().setValue(usuario)

            Toast.makeText(this, "Usuário cadastrado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, PrincipalActivity ::class.java))

        }catch (e: Exception){
            Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show()
            Log.v("Erro: ", e.toString())
        }
    }
}
