package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

open class Sentencia {
    fun getArbolVisual(): TreeItem<String>{
        return TreeItem("Sentencia")
    }
}