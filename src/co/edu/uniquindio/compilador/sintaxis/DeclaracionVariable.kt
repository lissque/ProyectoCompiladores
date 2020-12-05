package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class DeclaracionVariable(var tipoVariable: Token?, var tipoDato: Token?, var identificador: Token, var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Declaracion de Variable")

        raiz.children.add(TreeItem("Tipo de variable: ${tipoVariable!!.lexema}"))
        raiz.children.add(TreeItem("Tipo de dato: ${tipoDato!!.lexema}"))
        raiz.children.add(TreeItem("Identificador: ${identificador.lexema}"))
        raiz.children.add(expresion.getArbolVisual())

        return raiz
    }

    override fun toString(): String {
        return "DeclaracionVariable(tipoVariable=$tipoVariable, tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(identificador.lexema, tipoDato!!.lexema,true,ambito,identificador.fila,identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (expresion!=null){
            expresion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }
}