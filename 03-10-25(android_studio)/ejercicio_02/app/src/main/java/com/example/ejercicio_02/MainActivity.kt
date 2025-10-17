package com.example.ejercicio_02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicio_02.model.Usuarios
import com.example.ejercicio_02.ui.theme.Ejercicio_02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Elementos del activity_main
        val mailtext = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passtext = findViewById<EditText>(R.id.editTextTextPassword)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val mail = mailtext.text.toString().trim()
            val pass = passtext.text.toString().trim()

            if (Usuarios.Verificar(mail, pass)) {
                Toast.makeText(this, "Bienvenido, $mail", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("correo", mail)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}