package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
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

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        expresion.analizarSemantica(tablaSimbolos, listaErrores, ambito)
    }
}