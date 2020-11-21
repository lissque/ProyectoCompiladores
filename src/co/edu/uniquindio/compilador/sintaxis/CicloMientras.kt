package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

class CicloMientras(var expresion: Expresion, var sentencia: ArrayList<Sentencia>?):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem("Ciclo Mientras")
        raiz.children.add(expresion.getArbolVisual())

        if (sentencia!=null){
            var raizSentencias = TreeItem("Sentencias")
            for (s in sentencia!!){
                raizSentencias.children.add(s.getArbolVisual())
            }
            raiz.children.add(raizSentencias)
        }

        return raiz
    }

    override fun toString(): String {
        return "CicloMientras(expresion=$expresion, sentencia=$sentencia)"
    }
}