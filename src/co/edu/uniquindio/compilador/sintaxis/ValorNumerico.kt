package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class ValorNumerico(var signo: Token?, var numero:Token) {
    override fun toString(): String {
        return "ValorNumerico(signo=$signo, numero=$numero)"
    }
}