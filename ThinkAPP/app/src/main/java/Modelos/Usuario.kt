package Modelos

import com.google.firebase.database.Exclude

class Usuario {
    lateinit var nome: String
    lateinit var email: String
    @Exclude
    lateinit var senha: String
}