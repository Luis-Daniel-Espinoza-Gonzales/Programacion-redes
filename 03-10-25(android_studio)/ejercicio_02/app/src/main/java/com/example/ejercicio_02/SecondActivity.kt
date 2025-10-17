package com.example.ejercicio_02

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class SecondActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //Elemento del activity_second
        val text = findViewById<TextView>(R.id.textView)
        val btn_cerrar = findViewById<Button>(R.id.button4)

        //Informacion del login
        val correo = intent.getStringExtra("correo") ?: "Usuario"
        text.text = "Correo Electronico: $correo"

        btn_cerrar.setOnClickListener {
            // Cerramos la sesión y volver al login
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}