package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Incremento(var nombreVariable:Token):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz=  TreeItem("Incremento")

        if (nombreVariable!=null){
            raiz.children.add(TreeItem("Identificador: ${nombreVariable.lexema}"))
        }
        return raiz
    }


        override fun toString(): String {
        return "Incremento(nombreVariable=$nombreVariable)"
    }
}