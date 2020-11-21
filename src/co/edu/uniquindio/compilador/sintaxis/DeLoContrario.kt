package co.edu.uniquindio.compilador.sintaxis

import javafx.scene.control.TreeItem

class DeLoContrario(var listaSentencia: ArrayList<Sentencia>?) {

    open fun getArbolVisual(): TreeItem<String>{
        var raiz = TreeItem("De lo contrario")
        if (listaSentencia!=null){
            var raizSentencia = TreeItem("Sentencias")
            for (s in listaSentencia!!){
                raizSentencia.children.add(s.getArbolVisual())
            }
            raiz.children.add(raizSentencia)
        }
        return raiz
    }

    override fun toString(): String {
        return "DeLoContrario(listaSentencia=$listaSentencia)"
    }
}