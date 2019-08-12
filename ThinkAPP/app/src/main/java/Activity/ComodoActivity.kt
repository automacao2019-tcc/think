package Activity

import Fragments.ConexaoBluetoothProvisoria
import Fragments.ErroBD
import Helper.BluetoothHelper
import Helper.BluetoothListener
import Helper.BluetoothSerialService
import Helper.comodoHelper
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import br.com.ggslmrs.think.R
import kotlinx.android.synthetic.main.activity_area.*
import java.io.DataOutputStream
import java.lang.Exception

class ComodoActivity : AppCompatActivity(), BluetoothListener {

    private val BT_ACTIVATE = 101
    private val BT_VISIBILITY = 102
    private lateinit var bluetooth: BluetoothHelper
    private lateinit var arrayAdapter : ArrayAdapter<String>
    lateinit var servie: BluetoothSerialService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extra = intent.getStringExtra("comodo")

        when(extra) {
            comodoHelper.AREA.name -> preenche(R.layout.activity_area)
            comodoHelper.COZINHA.name -> preenche(R.layout.activity_cozinha)
            comodoHelper.QUARTO.name -> preenche(R.layout.activity_comodo)
            comodoHelper.SALA.name -> preenche(R.layout.activity_sala)
        }

        if(BluetoothHelper.getDefaultAdapter() == null)
            ErroBD.showDialogErro(supportFragmentManager, "Smartphone sem adaptador bluetooth")

        else if(!BluetoothHelper.verificaBluetoothHabilitado()){
            val it = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(it, BT_ACTIVATE)
        }
        else {
            tornarVisivel()
        }

        bluetooth = BluetoothHelper()

        listarBluetoothsPareados()
    }

    override fun onResume() {
        super.onResume()

        liga.setOnClickListener {
            mandarMensagem("liga")
        }

        desliga.setOnClickListener {
            mandarMensagem("desliga")
        }
    }

    private fun mandarMensagem(msg: String){
        Thread {
            try {
                servie.write(obtemByteArray(msg))

            }catch (e:Exception){
                ErroBD.showDialogErro(supportFragmentManager, "Erro ao mandar mensagem bluetooth: $e")
            }
        }.start()
    }

    private fun obtemByteArray(string:String) : ByteArray{
        val charset = Charsets.UTF_8
        val byteArray = string.toByteArray(charset)

        return byteArray
    }

    private fun preenche(layout: Int) {
        setContentView(layout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(BT_ACTIVATE == requestCode){
            if(Activity.RESULT_OK == resultCode){
                tornarVisivel()
            }
            else {

            }
        }
    }

    private fun tornarVisivel(){
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
        startActivityForResult(discoverableIntent, BT_VISIBILITY)
    }

    private fun procuraBluetoothsAtiviso(){
        try {
            bluetooth = BluetoothHelper.iniciaProcuraPorBluetoths(this, this)
        }catch (e:Exception){
            ErroBD.showDialogErro(supportFragmentManager, "Não foi possível fazer a busca por bluetooths")
        }
    }

    override fun action(action: String) {
       if(action.compareTo(BluetoothListener.ACTION_DISCOVERY_STARTED) == 0)
           arrayAdapter.add("Busca iniciada")

        else if (action.compareTo(BluetoothListener.ACTION_DISCOVERY_FINISHED) == 0){
           preencheLista()
           arrayAdapter.add("Fim de busca")
       }
    }

    private fun preencheLista(){
        bluetooth = BluetoothHelper()
    }

    private fun listarBluetoothsPareados(){
        val c = ConexaoBluetoothProvisoria()
        val b  = BluetoothHelper()
        val l = b.obterDevicesPareados(baseContext)

        c.showDialog(supportFragmentManager, l)
    }
}
