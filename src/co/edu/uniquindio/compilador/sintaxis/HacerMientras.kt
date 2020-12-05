package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class HacerMientras(var listaSentencias: ArrayList<Sentencia>, var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem("Hacer Mientras")

        var raizSentencias = TreeItem("Sentencias")

        for (s in listaSentencias){
            raizSentencias.children.add(s.getArbolVisual())
        }
        raiz.children.add(raizSentencias)

        raiz.children.add(expresion.getArbolVisual())

        return raiz
    }
    override fun toString(): String {
        return "HacerMientras(listaSentencias=$listaSentencias, expresion=$expresion)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentencias){
            s.llenarTablaSimbolos(tablaSimbolos,listaErrores,ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentencias){
            s.analizarSemantica(tablaSimbolos,listaErrores,ambito)
        }
    }
}