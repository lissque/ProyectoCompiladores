package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class Incremento(var nombreVariable:Token):Sentencia() {
    override fun toString(): String {
        return "Incremento(nombreVariable=$nombreVariable)"
    }
}