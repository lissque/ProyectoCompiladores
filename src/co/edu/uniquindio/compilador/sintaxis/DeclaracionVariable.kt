package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class DeclaracionVariable(var tipoVariable: Token?, var tipoDato: Token?, var identificador: Token, var expresion: Expresion):Sentencia() {

    override fun toString(): String {
        return "DeclaracionVariable(tipoVariable=$tipoVariable, tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }
}