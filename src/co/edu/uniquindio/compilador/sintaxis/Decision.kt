package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Decision(var expresionLogica: ExpresionLogica, var listaSentencia: ArrayList<Sentencia>?, var deLoContrario: DeLoContrario?): Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Decision")
        raiz.children.add(expresionLogica.getArbolVisual())

        if (listaSentencia!=null){
            var raizVerdad = TreeItem("Sentencias Verdaderas")
            for (s in listaSentencia!!){
                raizVerdad.children.add(s.getArbolVisual())
            }
            raiz.children.add(raizVerdad)
        }

        if (deLoContrario!=null){
            var raizFalso = TreeItem("Sentencias Falsas")
            for (sd in deLoContrario!!.listaSentencia!!){
                raizFalso.children.add(sd.getArbolVisual())
            }
            raiz.children.add(raizFalso)
        }

        return raiz
    }

    override fun toString(): String {
        return "Decision(expresionLogica=$expresionLogica, listaSentencia=$listaSentencia, deLoContrario=$deLoContrario)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentencia!!){
            s.llenarTablaSimbolos(tablaSimbolos,listaErrores,ambito)
        }
        if(deLoContrario!=null){
            for (s in deLoContrario!!.listaSentencia!!){
                s.llenarTablaSimbolos(tablaSimbolos,listaErrores,ambito)
            }
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentencia!!){
            s.analizarSemantica(tablaSimbolos,listaErrores,ambito)
        }
        if(deLoContrario!=null){
            for (s in deLoContrario!!.listaSentencia!!){
                s.analizarSemantica(tablaSimbolos,listaErrores,ambito)
            }
        }
    }
}
