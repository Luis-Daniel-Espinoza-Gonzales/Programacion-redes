package com.example.ejercicio_02.model

import android.text.BoringLayout

class Usuarios(private val correo: String, private val contrasena: String) {

    companion object {

        //Usuarios
        private val lista_usuario = mutableListOf(
            Usuarios("danidex129@gmail.com", "1234"),
            Usuarios("juan@gmail.com", "asdf"),
            Usuarios("maria@gmail.com", "abcd")
        )

        //Verifica si existe un usuario
        fun Verificar(correo: String, pass: String): Boolean {
            return lista_usuario.any {it.correo.equals(correo, ignoreCase = true) && it.contrasena == pass}
        }

        // Registrar nuevo usuario (si no existe ese correo)
        fun registrar(correo: String, pass: String): Boolean {
            if (lista_usuario.any { it.correo.equals(correo, ignoreCase = true) }) {
                return false // correo existe
            }
            lista_usuario.add(Usuarios(correo, pass))
            return true
        }

        //Ver lista actual
        fun listar(): List<Usuarios> = lista_usuario
    }
}