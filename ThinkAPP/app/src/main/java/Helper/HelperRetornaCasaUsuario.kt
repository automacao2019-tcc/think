package Helper

import DAO.ConfiguracaoFirebase
import Modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HelperRetornaCasaUsuario {
    companion object {
        private val firebase = ConfiguracaoFirebase.getFirebase()
        private val autentica = ConfiguracaoFirebase.getFirebaseAuth()

        fun getCasaUser(): String {
            val email = autentica.currentUser?.email
            var casa = ""
            var usuario : Usuario

            if(!email.isNullOrEmpty()){
                firebase.child("usuario").orderByChild("email").equalTo(email).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        for(data in p0.children){
                            usuario = data.getValue(Usuario ::class.java) as Usuario
                            casa = usuario.casa
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
            }

            return casa
        }
    }
}