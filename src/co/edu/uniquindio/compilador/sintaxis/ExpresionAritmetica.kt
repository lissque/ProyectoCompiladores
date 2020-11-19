package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class ExpresionAritmetica(var listaExpresionAritmetica: ArrayList<String>): Expresion() {
    override fun toString(): String {
        return "ExpresionAritmetica(listaExpresionAritmetica=$listaExpresionAritmetica)"
    }
}