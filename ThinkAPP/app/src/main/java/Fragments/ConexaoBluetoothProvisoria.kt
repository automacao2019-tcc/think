package Fragments

import Activity.ComodoActivity
import Helper.BluetoothSerialService
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ggslmrs.think.R
import kotlinx.android.synthetic.main.conexao_bluetooth_provisoria.*
import kotlin.collections.ArrayList

class ConexaoBluetoothProvisoria : DialogFragment(){

    private var listaBluetooth = arrayListOf<BluetoothDevice>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.conexao_bluetooth_provisoria, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inflarLista()

        isCancelable = false

        listaBluetoothView.setOnItemClickListener { parent, view, position, id ->
            conectar(position)
        }
    }

    private fun conectar(position:Int){
        try{
            val device = listaBluetooth[position]

            val mHandlerBT = Handler()
            val mSerialService = BluetoothSerialService(activity, mHandlerBT)

            (activity as ComodoActivity).servie = mSerialService

            mSerialService.connect(device)

            dismiss()
        }catch (e:Exception){
            ErroBD.showDialogErro(requireFragmentManager(), "Erro ao conectar bluetooth: $e")
        }
    }

    fun showDialog(manager: FragmentManager?, listaBluetooth: ArrayList<BluetoothDevice>) {
        this.listaBluetooth = listaBluetooth
        super.show(manager, "")
    }

    private fun inflarLista(){
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, nameBluetooth())
        listaBluetoothView.adapter = adapter
    }

    private fun nameBluetooth() : ArrayList<String> {
        val lista = arrayListOf<String>()

        for(b in listaBluetooth){
            lista.add(b.name)
        }

        return lista
    }
}