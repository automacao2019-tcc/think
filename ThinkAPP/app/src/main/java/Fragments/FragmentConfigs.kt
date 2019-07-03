package Fragments

import Activity.MainActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.ggslmrs.think.R
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_config.*

class FragmentConfigs : Fragment() {

    private lateinit var autenticacao : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_config, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        autenticacao = FirebaseAuth.getInstance()

        deslogar.setOnClickListener {
            deslogar()
        }
    }

    private fun deslogar(){

        try{
            autenticacao.signOut()
            LoginManager.getInstance().logOut()
           // googleSignInClient.signOut()

            startActivity(Intent(context, MainActivity ::class.java))
        }catch (e:Exception){
            Toast.makeText(context, "Erro ao deslogar", Toast.LENGTH_SHORT).show()
        }
    }
}