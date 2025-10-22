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
    // Referencia a la ListView definida en el layout (second_main.xml)
    private lateinit var contactListView: ListView

    // "Launcher" que gestiona el resultado de la petición de permisos.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // Este bloque se ejecuta después de que el usuario responde al diálogo de permisos.
            if (isGranted) {
                // Si el permiso fue concedido, mostramos un mensaje y cargamos los contactos.
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                mostrarContactos()
            } else {
                // Si el permiso fue denegado, informamos al usuario.
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.second_main)
        // Inicializamos la ListView a partir de su ID en el layout.
        contactListView = findViewById(R.id.lista_contactos)

        // Comprobamos si ya tenemos permiso para leer contactos. Si no, lo solicitamos.
        solicitarPermisoContactos()
    }

    /**
     * Comprueba el estado del permiso READ_CONTACTS y actúa en consecuencia.
     */
    private fun solicitarPermisoContactos() {
        when {
            // Caso 1: El permiso ya ha sido concedido previamente.
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Mostramos un mensaje y procedemos a cargar los contactos directamente.
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                mostrarContactos()
            }

            // Caso 2: El usuario ha denegado el permiso antes. Debemos mostrar una justificación.
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                // Mostramos un Toast explicando por qué necesitamos el permiso.
                Toast.makeText(this, "Necesitamos permiso para leer contactos", Toast.LENGTH_SHORT)
                    .show()
                // Volvemos a lanzar el diálogo de petición de permisos.
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }

            // Caso 3: Es la primera vez que se solicita el permiso.
            else -> {
                // Lanzamos el diálogo de petición de permisos directamente.
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    /**
     * Realiza una consulta al Content Provider de contactos para obtener y mostrar la información.
     */
    @SuppressLint("Range")
    private fun mostrarContactos() {
        // Lista donde guardaremos la información formateada de cada contacto como un String.
        val contactList = mutableListOf<String>()
        // Obtenemos el 'ContentResolver', el objeto que nos permite consultar los datos del sistema.
        val contentResolver = contentResolver

        // 1. REALIZAR LA CONSULTA PRINCIPAL PARA OBTENER TODOS LOS CONTACTOS
        val contactsCursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, // URI para la tabla de contactos.
            null, // Proyección (null para traer todas las columnas).
            null, // Selección (null para traer todos los contactos).
            null, // Argumentos de selección.
            ContactsContract.Contacts.DISPLAY_NAME + " ASC" // Ordenar por nombre alfabéticamente.
        )

        // El bloque 'use' asegura que el cursor se cierre automáticamente al finalizar.
        contactsCursor?.use { cursor ->
            // Comprobamos si la consulta devolvió algún resultado.
            if (cursor.count > 0) {
                // 2. ITERAR SOBRE CADA CONTACTO ENCONTRADO
                while (cursor.moveToNext()) {
                    // StringBuilder para construir eficientemente la cadena de texto de cada contacto.
                    val contactInfo = StringBuilder()

                    // Obtenemos el ID y el Nombre del contacto actual.
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val nombre = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    contactInfo.append("Nombre: $nombre\n")

                    // 3. OBTENER TELÉFONOS (SIN DUPLICADOS)
                    // Realizamos una subconsulta para obtener los teléfonos de este contacto usando su ID.
                    val phonesCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", // Filtramos por ID.
                        arrayOf(id), // El '?' se reemplaza por el ID del contacto.
                        null
                    )
                    // Usamos un 'Set' para guardar los números y evitar duplicados automáticamente.
                    val phoneNumbers = mutableSetOf<String>()
                    phonesCursor?.use { pCursor ->
                        while (pCursor.moveToNext()) {
                            val telefono = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            phoneNumbers.add(telefono)
                        }
                    }

                    // Añadimos los teléfonos (sin duplicados) al texto del contacto.
                    contactInfo.append("Teléfonos:\n")
                    if (phoneNumbers.isNotEmpty()) {
                        phoneNumbers.forEach { telefono ->
                            contactInfo.append("- $telefono\n")
                        }
                    } else {
                        contactInfo.append("- (Ninguno)\n")
                    }

                    // 4. OBTENER EMAILS (SIN DUPLICADOS)
                    // Realizamos otra subconsulta para los correos, similar a la de los teléfonos.
                    val emailsCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    // Usamos otro 'Set' para evitar emails duplicados.
                    val emailAddresses = mutableSetOf<String>()
                    emailsCursor?.use { eCursor ->
                        while (eCursor.moveToNext()) {
                            val email = eCursor.getString(eCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                            emailAddresses.add(email)
                        }
                    }

                    // Añadimos los emails (sin duplicados) al texto del contacto.
                    contactInfo.append("Emails:\n")
                    if (emailAddresses.isNotEmpty()) {
                        emailAddresses.forEach { email ->
                            contactInfo.append("- $email\n")
                        }
                    } else {
                        contactInfo.append("- (Ninguno)\n")
                    }

                    // 5. AÑADIR EL TEXTO FINAL DEL CONTACTO A LA LISTA
                    contactList.add(contactInfo.toString())
                }
            }
        }

        // 6. ACTUALIZAR LA INTERFAZ DE USUARIO
        // Creamos un adaptador simple para mostrar la lista de strings en la ListView.
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactList)
        // Asignamos el adaptador a nuestra ListView para que se muestren los datos en pantalla.
        contactListView.adapter = adapter
    }
}