package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Detener(var sentenciaDetener: Token):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        return TreeItem("${sentenciaDetener.lexema}")
    }

    override fun toString(): String {
        return "Detener(sentenciaDetener=$sentenciaDetener)"
    }
}