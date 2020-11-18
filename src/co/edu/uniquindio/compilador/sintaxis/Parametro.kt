package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class Parametro(var nombreParametro:Token, var tipoDeDato:Token) {

    override fun toString(): String {
        return "Parametro(nombreParametro=$nombreParametro, tipoDeDato=$tipoDeDato)"
    }
}