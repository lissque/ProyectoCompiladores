package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

open class Expresion{

    open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expresion")
        return raiz
    }
}