package Fragments

import DAO.ConfiguracaoFirebase
import Modelos.Usuario
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.ggslmrs.think.R
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_perfil.*

class FragmentPerfil : Fragment() {

    private lateinit var firebase: DatabaseReference
    private lateinit var autenticacao: FirebaseAuth
    private lateinit var usuario_: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onResume() {
        super.onResume()

        firebase = ConfiguracaoFirebase.getFirebase()
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth()

        buscarUsuario()

        btnEditar.setOnClickListener {
            editar()
        }
    }

    private fun buscarUsuario(){
        loading.showDialog(fragmentManager!!)

        try{
            val user = FirebaseAuth.getInstance().currentUser!!.email


            firebase.child("usuario").orderByChild("email").equalTo(user)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {

                        for(data in p0.children){
                            val usuario = data.getValue(Usuario ::class.java)
                            if(usuario != null){
                                preencherUsuario(usuario)
                                usuario_ = data.key!!
                            }

                            else{
                                loading.dismissDialog()
                                ErroBD.showDialogErro(fragmentManager!!, "Erro ao buscar usuário")
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        loading.dismissDialog()
                        ErroBD.showDialogErro(fragmentManager!!, "Erro ao buscar usuário")
                    }

                })

        }catch (e:java.lang.Exception){
            loading.dismissDialog()
           ErroBD.showDialogErro(fragmentManager!!, "Erro ao buscar perfil")
        }

    }

    private fun preencherUsuario(usuario: Usuario){
        edtUserEmail.setText(usuario.email)
        edtUserName.setText(usuario.nome)
        edtUserSenha.setText(usuario.senha)

        loading.dismissDialog()
    }

    private fun editar(){
        loading.showDialog(fragmentManager!!)
        val usuario = Usuario()

        usuario.nome = edtUserName.text.toString()
        usuario.senha = edtUserSenha.text.toString()
        usuario.email = edtUserEmail.text.toString()

        try{
            firebase.child("usuario").child(usuario_).setValue(usuario).addOnCompleteListener {
                if(!it.isSuccessful){
                    loading.dismissDialog()
                    ErroBD.showDialogErro(fragmentManager!!, "Erro ao editar usuário")
                }
                else
                    loading.dismissDialog()


            }
        }
        catch (e:Exception){
            loading.dismissDialog()
            ErroBD.showDialogErro(fragmentManager!!, e.toString())
        }
    }
}