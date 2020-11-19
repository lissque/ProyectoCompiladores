package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class Detener(var sentenciaDetener: Token):Sentencia() {
    override fun toString(): String {
        return "Detener(sentenciaDetener=$sentenciaDetener)"
    }
}