package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

class Retorno(var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String>{
        var raiz = TreeItem("Retorno")
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }
}