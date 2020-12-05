package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

open class Expresion{

    open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expresion")
        return raiz
    }

    open fun obtenerTipo(tablaSimbolos: TablaSimbolos,ambito:String):String{
        return ""
    }

    open fun analizarSemantica(tablaSimbolos: TablaSimbolos,listaErrores: ArrayList<Error>,ambito: String){


    }

}