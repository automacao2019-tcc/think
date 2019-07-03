package Activity

import DAO.ConfiguracaoFirebase
import Modelos.Codigo
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.lang.Exception

class CadastroActivity : AppCompatActivity() {

    private lateinit var autenticacao : FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btnCadastrar.setOnClickListener {
            verificaCodigo()
        }
    }

    private fun verificaCodigo(){
        val cod = edtCodigoDeSeguranca.text.toString()
        var valido = false

        reference = ConfiguracaoFirebase.getFirebase()

        reference.child("codigos").orderByChild("codigo").equalTo(cod).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    val c = data.getValue(Codigo ::class.java)
                    if(c != null){
                        valido = c.codigo.isNotEmpty()
                    }
                }

                if(valido)
                    novocadastro()

                else
                    Toast.makeText(this@CadastroActivity, "Esse código não é válido", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
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
            }

            else {
                var erroExcecao = ""
                try {
                    throw it.exception!!
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
