package Helper

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentManager

class BluetoothHelper : BroadcastReceiver {

    constructor(fragmentManager: FragmentManager) : super() {
        this.fragmentManager = fragmentManager
    }

    constructor() : super()

    private var listener : BluetoothListener? = null
    lateinit var lista: ArrayList<BluetoothDevice>
    private lateinit var dispositivo: BluetoothAdapter
    private lateinit var fragmentManager : FragmentManager


    companion object {
        fun getDefaultAdapter(): BluetoothAdapter? {
            return BluetoothAdapter.getDefaultAdapter() //retorna null se não tiver bluetooth
        }

        fun verificaBluetoothHabilitado(): Boolean {
            return BluetoothAdapter.getDefaultAdapter().isEnabled //verifica se o bluetooth esta ativado
        }

        fun iniciaProcuraPorBluetoths(context: Context, listener: BluetoothListener): BluetoothHelper {
            val bluetooth = BluetoothHelper()
            bluetooth.dispositivo = BluetoothAdapter.getDefaultAdapter()

            val filter = IntentFilter(BluetoothListener.ACTION_FOUND)
            val filter2 = IntentFilter(BluetoothListener.ACTION_DISCOVERY_FINISHED)
            val filter3 = IntentFilter(BluetoothListener.ACTION_DISCOVERY_STARTED)

            context.registerReceiver(bluetooth, filter)
            context.registerReceiver(bluetooth, filter2)
            context.registerReceiver(bluetooth, filter3)

            bluetooth.dispositivo.startDiscovery()
            return bluetooth
        }
    }

    fun obterDevicesPareados(applicationContext: Context) : ArrayList<BluetoothDevice> {
        val bluetooth = BluetoothHelper()
        lista = ArrayList()
        bluetooth.dispositivo = BluetoothAdapter.getDefaultAdapter()

        val pairedDevices = bluetooth.dispositivo.bondedDevices

        if(pairedDevices.isNotEmpty()){
            for(device in pairedDevices)
                lista.add(device)
        }

        return lista
    }

    private constructor(listener: BluetoothListener){
        this.listener = listener
        lista = ArrayList()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        if(action != null){
            if(action.compareTo(BluetoothDevice.ACTION_FOUND) == 0){
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                if(lista.contains(device))
                    return

                lista.add(device)
            }
            else if (action.compareTo(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) == 0)
                context!!.unregisterReceiver(this)

            listener?.action(action)
        }
    }
}