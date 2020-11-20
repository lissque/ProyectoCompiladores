package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class Funcion(var nombreFuncion:Token, var tipoRetorno:Token, var listaParametros: ArrayList<Parametro>?, var listaSentencias: ArrayList<Sentencia>) {

    override fun toString(): String {
        return "Funcion(nombreFuncion=$nombreFuncion, tipoRetorno=$tipoRetorno, listaParametros=$listaParametros, listaSentencias=$listaSentencias)"
    }

    fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem("Funcion")
        raiz.children.add(TreeItem("Nombre: ${nombreFuncion.lexema}"))
        raiz.children.add(TreeItem("Tipo de Retorno: ${tipoRetorno.lexema}"))
        var raizParametros = TreeItem("Parametros")
        if (listaParametros!=null){
            for ( parametro in listaParametros!!){
                raizParametros.children.add(parametro.getArbolVisual())
            }
        }
        raiz.children.add(raizParametros)
        var raizSentencia = TreeItem("Sentencias")
        for ( sentencia in listaSentencias){
            raizSentencia.children.add(sentencia.getArbolVisual())
        }
        raiz.children.add(raizSentencia)
        return raiz
    }
}