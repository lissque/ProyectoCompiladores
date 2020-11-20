package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class Asignacion(var identificador: Token,var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "Asignacion(identificador=$identificador, expresion=$expresion)"
    }
}