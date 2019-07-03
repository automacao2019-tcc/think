package Activity

import Helper.comodoHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ggslmrs.think.R

class ComodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extra = intent.getStringExtra("comodo")

        when(extra) {
            comodoHelper.AREA.name -> preenche(R.layout.activity_area)
            comodoHelper.COZINHA.name -> preenche(R.layout.activity_cozinha)
            comodoHelper.QUARTO.name -> preenche(R.layout.activity_comodo)
            comodoHelper.SALA.name -> preenche(R.layout.activity_sala)
        }
    }

    private fun preenche(layout: Int) {
        setContentView(layout)

    }

}
