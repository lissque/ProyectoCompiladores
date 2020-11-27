package co.edu.uniquindio.compilador.sintaxis

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
}