package DAO

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfiguracaoFirebase {

    companion object{
        lateinit var referenciaFirebase: DatabaseReference
        lateinit var autenticacao : FirebaseAuth

        fun getFirebase() : DatabaseReference {
            referenciaFirebase = FirebaseDatabase.getInstance().reference
            return referenciaFirebase
        }

        fun getFirebaseAuth() : FirebaseAuth {
            autenticacao = FirebaseAuth.getInstance()

            return autenticacao
        }
    }
}