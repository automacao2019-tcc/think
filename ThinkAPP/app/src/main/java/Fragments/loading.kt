package Fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ggslmrs.think.R

class loading : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.loagin_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    companion object{
        private val instance = loading()

        fun getInstance() = instance

        fun showDialog(fragmentManager: FragmentManager){
            with(getInstance()){
                if(!isAdded){
                    isCancelable = false
                    show(fragmentManager, "progress")
                }
            }
        }

        fun dismissDialog(){
            with(getInstance()){
                if(isAdded)
                    dismiss()
            }
        }
    }
}