package com.example.ejercicio_03

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class Contacto : ComponentActivity() {
    private lateinit var lista_contacto_view: ListView
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.second_main)

        //Comprobar y solicitar permiso
        solicitarPermisoContactos()
    }

    private fun solicitarPermisoContactos() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                //El permiso fue concedido. Mostrar los contactos
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                //El usuario ha denegado el permiso anteriormente. Mostrar una explicación al usuario
                Toast.makeText(this, "Necesitamos permiso para leer contactos", Toast.LENGTH_SHORT)
                    .show()
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }

            else -> {
                //Solicita el permiso por primera vez
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
    @SuppressLint("Range")
    private fun mostrarContactos() {
        val lista_contactos = mutableListOf<String>()

        val content_resolver = contentResolver

        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        val cursor = content_resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            if(it.count > 0) {
                while (it.moveToNext()) {
                    val nombre = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    lista_contactos.add(nombre)
                }
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contactos)
        lista_contacto_view.adapter = adapter
    }
}