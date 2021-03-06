package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class DeclaracionArreglo(var tipoVariable: Token, var tipoDeDato: Token, var identificador: Token, var listaValoresNumericos: ArrayList<ValorNumerico>?):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String>{
        var raiz = TreeItem("Declaracion de Arreglo")
        raiz.children.add(TreeItem("Tipo de variable: ${tipoVariable.lexema}"))
        raiz.children.add(TreeItem("Tipo de dato: ${tipoDeDato.lexema}"))
        raiz.children.add(TreeItem("Identificador: ${identificador.lexema}"))

        if (listaValoresNumericos!=null){
            var raizValoresNumerico = TreeItem("Valores")
            for (v in listaValoresNumericos!!){
                raizValoresNumerico.children.add(v.getArbolVisual())
            }
            raiz.children.add(raizValoresNumerico)
        }

        return raiz
    }

    override fun toString(): String {
        return "DeclaracionArreglo(tipoVariable=$tipoVariable, tipoDeDato=$tipoDeDato, identificador=$identificador, listaValoresNumericos=$listaValoresNumericos)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {

        tablaSimbolos.guardarSimboloVariable(identificador.lexema,tipoDeDato.lexema,true,ambito,identificador.fila,identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (e in listaValoresNumericos!! ){

        }
    }
}