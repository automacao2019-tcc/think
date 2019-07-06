package Helper

import DAO.ConfiguracaoFirebase
import Modelos.Codigo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HelperAnalisaCodigo {
    companion object{

        fun analisaCodigos() {
            val reference = ConfiguracaoFirebase.getFirebase()

            val milissegundos = System.currentTimeMillis()

            try {
                reference.child("codigos").addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(data: DataSnapshot) {
                        for(d in data.children){
                            val cod = d.getValue(Codigo ::class.java)

                            if(cod != null){
                                if((milissegundos - cod.milissegundos) > (8.64 * (Math.pow(10.0, 7.0)))){
                                    reference.child("codigos").child(d.key!!).removeValue()
                                }
                            }
                        }
                    }

                    override fun onCancelled(data: DatabaseError) {

                    }
                })
            }catch (e: Exception){

            }
        }

    }
}