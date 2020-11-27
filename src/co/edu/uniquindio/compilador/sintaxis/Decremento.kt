package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Decremento(var nombreVariable: Token):Sentencia() {

    override open fun getArbolVisual():TreeItem<String>{
        var raiz=  TreeItem("Decremento")

        if (nombreVariable!=null){
            raiz.children.add(TreeItem("Identificador: ${nombreVariable.lexema}"))
        }
        return raiz
    }

    override fun toString(): String {
        return "Decremento(nombreVariable=$nombreVariable)"
    }
}