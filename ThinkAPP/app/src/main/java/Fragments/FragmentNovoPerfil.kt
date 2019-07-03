package Fragments

import DAO.ConfiguracaoFirebase
import Helper.PermissionsHelper
import Modelos.Codigo
import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.ggslmrs.think.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_novo_perfil.*
import kotlin.random.Random

class FragmentNovoPerfil : Fragment() {

    private lateinit var reference : DatabaseReference
    private var random = Random

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_novo_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        reference = ConfiguracaoFirebase.getFirebase()

        btnConvidar.setOnClickListener {
            convidarUsuario()
        }
    }

    private fun convidarUsuario(){
        try {
            val codigo = Codigo()
            codigo.milissegundos = System.currentTimeMillis()
            codigo.codigo = gerarCod()

            reference.child("codigos").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(data: DataSnapshot) {
                    for(d in data.children){
                        val cod = d.getValue(codigo ::class.java)

                        if(cod != null){
                            if((codigo.milissegundos - cod.milissegundos) > (8.64 * (Math.pow(10.0, 7.0)))){
                                   reference.child("codigos").child(d.key!!).removeValue()
                            }
                        }
                    }
                }

                override fun onCancelled(data: DatabaseError) {
                    Toast.makeText(context, "Não foi apagado nenhum registro", Toast.LENGTH_SHORT).show()
                }
            })

            reference.child("codigos").push().setValue(codigo)

            mandarSMS(codigo.codigo)


        }catch (e:Exception){
            Toast.makeText(context, "$e", Toast.LENGTH_LONG).show()
            Log.e("Erro", e.toString())
        }
    }

    private fun gerarCod(): String {
        var cod = ""

        val l = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "X", "W", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9")

        for(i in 0..5)
            cod += l[rand(0, l.size - 1)]

        return cod
    }

    private fun rand(from: Int, to: Int) = random.nextInt(to - from) + from

    private fun mandarSMS(codigo: String){
        val number = edtConvidarUsuarioTel.text.toString()
        val smsManager = SmsManager.getDefault()

        val texto = "Seu código Think é: $codigo"

        try{
            if(PermissionsHelper.verificaPermissao(context!!, Manifest.permission.SEND_SMS))
                smsManager.sendTextMessage(number, null, texto, null, null)

            else
                PermissionsHelper.requisitaPermissao(context as Activity, Manifest.permission.SEND_SMS)

            Toast.makeText(context, "SMS enviado com o código", Toast.LENGTH_SHORT).show()
        }catch (e : Exception) {
            Log.e("Erro", e.toString())
        }

    }
}