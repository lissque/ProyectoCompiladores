package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
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

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        expresion.analizarSemantica(tablaSimbolos, listaErrores, ambito)
    }
}