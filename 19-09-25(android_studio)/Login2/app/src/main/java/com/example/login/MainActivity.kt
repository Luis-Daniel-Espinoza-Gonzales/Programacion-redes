package com.example.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity


class   MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Elementos del Layout
        val emailText = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val passText = findViewById<EditText>(R.id.editTextTextPassword2)
        val button = findViewById<Button>(R.id.button2)

        //lista de usuarios
        val users = mapOf(
            "admin@gmail.com" to "1234",
            "daniel@gmail.com" to "asdf",
            "usuario@gmail.com" to "tirate_de_un_puente"
        )

        button.setOnClickListener {
            val email = emailText.text.toString().trim()
            val pass = passText.text.toString().trim()

            if (users.containsKey(email) && users[email] == pass) {
                Toast.makeText(this, "Bienvenido $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}