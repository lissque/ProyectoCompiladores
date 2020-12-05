package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

open class Sentencia {

    open fun getArbolVisual(): TreeItem<String>{
        return TreeItem("Sentencia")
    }

    open fun  llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores:ArrayList<Error>, ambito:String){

    }

   open fun analizarSemantica(tablaSimbolos:TablaSimbolos, listaErrores:ArrayList<Error>,ambito: String){

   }
}