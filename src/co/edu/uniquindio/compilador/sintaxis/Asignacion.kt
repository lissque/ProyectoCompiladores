package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Asignacion(var identificador: Token,var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Asignacion")
        raiz.children.add(TreeItem("Identificador: ${identificador.lexema}"))
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "Asignacion(identificador=$identificador, expresion=$expresion)"
    }
}