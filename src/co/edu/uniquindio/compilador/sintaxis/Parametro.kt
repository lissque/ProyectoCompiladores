package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Parametro(var nombreParametro:Token, var tipoDeDato:Token) {

    override fun toString(): String {
        return "Parametro(nombreParametro=$nombreParametro, tipoDeDato=$tipoDeDato)"
    }

    fun getArbolVisual(): TreeItem<String> {

        return TreeItem("${nombreParametro.lexema} : ${tipoDeDato.lexema}" )
    }
}