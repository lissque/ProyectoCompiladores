package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class UnidadDeCompilacion() {
    var listaDeclaracionVariable: ArrayList<DeclaracionVariable>? = null
    var listaFunciones: ArrayList<Funcion>? = null

    constructor(listaDeclaracionVariable: ArrayList<DeclaracionVariable>?, listaFunciones: ArrayList<Funcion>?):this(){
        this.listaDeclaracionVariable = listaDeclaracionVariable
        this.listaFunciones = listaFunciones
    }

    fun getArbolVisual(): TreeItem<String>{
        var raiz = TreeItem("Unidad de Compilacion")

        var raizDeclaracionVariable = TreeItem("Variables Globales")
        if (listaDeclaracionVariable != null){
            for (d in listaDeclaracionVariable!!){
                raizDeclaracionVariable.children.add(d.getArbolVisual())
            }
            raiz.children.add(raizDeclaracionVariable)
        }

        if (listaFunciones!= null){
            for (f in listaFunciones!!){
                raiz.children.add(f.getArbolVisual())
            }
        }
        return raiz
    }

    override fun toString(): String {
        return "UnidadDeCompilacion(listaDeclaracionVariable=$listaDeclaracionVariable, listaFunciones=$listaFunciones)"
    }


}