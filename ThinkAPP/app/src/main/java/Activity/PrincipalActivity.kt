package Activity

import Fragments.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ggslmrs.think.R
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {

    private val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        home.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentHome())
            ft.commit()
        }

        rotinas.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentRotina())
            ft.commit()
        }

        perfil.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentPerfil())
            ft.commit()
        }

        novoUsuario.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentNovoPerfil())
            ft.commit()
        }

        configuracoes.setOnClickListener {
            val ft = fm.beginTransaction()
            ft.replace(R.id.exibiFragment, FragmentConfigs())
            ft.commit()
        }
    }
}
