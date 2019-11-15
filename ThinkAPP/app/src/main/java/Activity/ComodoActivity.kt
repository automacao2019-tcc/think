package Activity

import Fragments.ErroBD
import Helper.BluetoothHelper
import Helper.BluetoothListener
import Helper.BluetoothSerialService
import Helper.comodoHelper
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import br.com.ggslmrs.think.R
import kotlinx.android.synthetic.main.activity_area.*
import kotlinx.android.synthetic.main.activity_banheiro.*
import kotlinx.android.synthetic.main.activity_comodo.*
import kotlinx.android.synthetic.main.activity_corredor.*
import kotlinx.android.synthetic.main.activity_cozinha.*
import kotlinx.android.synthetic.main.activity_sala.*
import java.lang.Exception

class ComodoActivity : Fragment(), BluetoothListener {

    private lateinit var bluetooth: BluetoothHelper
    private lateinit var arrayAdapter : ArrayAdapter<String>
    var servie: BluetoothSerialService? = null
    private var isSecondy : Boolean = false
    private lateinit var extra : String

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var layout = 0

        when(extra) {
            comodoHelper.AREA.name -> layout = R.layout.activity_area
            comodoHelper.COZINHA.name -> layout = R.layout.activity_cozinha
            comodoHelper.QUARTO.name -> layout = R.layout.activity_comodo
            comodoHelper.SALA.name -> layout = R.layout.activity_sala
            comodoHelper.BANHEIRO.name -> layout = R.layout.activity_banheiro
            comodoHelper.AREA.name -> layout = R.layout.activity_area
            comodoHelper.CORREDOR.name -> layout = R.layout.activity_corredor
        }
        if(layout != 0)
            return inflater.inflate(layout, container, false)
        else{
            ErroBD.showDialogErro(fragmentManager!!, "Erro ao obter o layout")
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetooth = BluetoothHelper()
    }

    override fun onResume() {
        super.onResume()

        when(extra) {
            comodoHelper.COZINHA.name -> {
                acende_luz_cozinha.setOnClickListener {
                    mandarMensagem("acende_cozinha")
                }

                apaga_luz_cozinha.setOnClickListener {
                    mandarMensagem("apaga_cozinha")
                }
            }
            comodoHelper.QUARTO.name -> {
                acende_luz_quarto.setOnClickListener {
                    if(isSecondy)
                        mandarMensagem("acende_quarto_2")
                    else
                        mandarMensagem("acende_quarto")
                }

                apaga_luz_quarto.setOnClickListener {
                    if(isSecondy)
                        mandarMensagem("apaga_quarto_2")
                    else
                        mandarMensagem("apaga_quarto")
                }
            }
            comodoHelper.SALA.name -> {
                acende_luz_sala.setOnClickListener {
                    mandarMensagem("acende_sala")
                }

                apaga_luz_sala.setOnClickListener {
                    mandarMensagem("apaga_sala")
                }
            }
            comodoHelper.BANHEIRO.name -> {
                acende_luz_banheiro.setOnClickListener {
                    if(isSecondy)
                        mandarMensagem("acende_banheiro_2")
                    else
                        mandarMensagem("acende_banheiro")
                }

                apaga_luz_banheiro.setOnClickListener {
                    if(isSecondy)
                        mandarMensagem("apaga_banheiro_2")
                    else
                        mandarMensagem("apaga_banheiro")
                }
            }

            comodoHelper.AREA.name -> {
                abrir_porrtao.setOnClickListener {
                    mandarMensagem("abrir_portao")
                }

                fechar_porrtao.setOnClickListener {
                    mandarMensagem("fechar_portao")
                }
            }

            comodoHelper.CORREDOR.name -> {
                acende_luz_corredor.setOnClickListener {
                    mandarMensagem("acende_corredor")
                }

                apaga_luz_corredor.setOnClickListener {
                    mandarMensagem("apaga_corredor")
                }
            }
        }

    }

    private fun mandarMensagem(msg: String){
        Thread {
            try {
                servie?.write(obtemByteArray(msg))

            }catch (e:Exception){
                ErroBD.showDialogErro(fragmentManager!!, "Erro ao mandar mensagem bluetooth: $e")
            }
        }.start()
    }

    private fun obtemByteArray(string:String) : ByteArray{
        val charset = Charsets.UTF_8
        val byteArray = string.toByteArray(charset)

        return byteArray
    }




    private fun procuraBluetoothsAtiviso(){
        try {
            bluetooth = BluetoothHelper.iniciaProcuraPorBluetoths(context!!, this)
        }catch (e:Exception){
            ErroBD.showDialogErro(fragmentManager!!, "Não foi possível fazer a busca por bluetooths")
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

    private fun armazenaSharedPreferences(sharedPreferences: SharedPreferences){
        val editor = sharedPreferences.edit()
        editor.putInt(BluetoothSerialService.STATE_KEY, BluetoothSerialService.STATE_NONE)
        editor.apply()
    }

    companion object {
        private val instance = ComodoActivity()
        private fun getInstance() = instance

        fun build(service: BluetoothSerialService?, isSecond: Boolean, comodo: String, fm : FragmentManager?){
            with(getInstance()){
                this.servie = service
                this.isSecondy = isSecond
                this.extra = comodo

                val ft = fm?.beginTransaction()
                ft?.replace(R.id.exibiFragment, this)
                ft?.commit()
            }
        }
    }
}
