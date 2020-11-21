package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Decremento(var nombreVariable: Token):Sentencia() {

    override open fun getArbolVisual():TreeItem<String>{
        return TreeItem("Decremento: Variable: ${nombreVariable.lexema}")
    }

    override fun toString(): String {
        return "Decremento(nombreVariable=$nombreVariable)"
    }
}