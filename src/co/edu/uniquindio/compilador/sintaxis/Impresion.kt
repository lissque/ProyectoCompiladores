package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

class Impresion(var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String>{
        var raiz = TreeItem("Impresion")
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "Impresion(expresion=$expresion)"
    }
}