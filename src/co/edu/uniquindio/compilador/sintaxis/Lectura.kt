package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

class Lectura(var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual():TreeItem<String>{
        var raiz = TreeItem("Lectura")
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "Lectura(expresion=$expresion)"
    }
}