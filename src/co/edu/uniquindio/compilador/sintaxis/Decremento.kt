package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class Decremento(var nombreVariable: Token):Sentencia() {
    override fun toString(): String {
        return "Decremento(nombreVariable=$nombreVariable)"
    }
}