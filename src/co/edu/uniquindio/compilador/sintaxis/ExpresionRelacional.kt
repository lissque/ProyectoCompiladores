package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionRelacional(): Expresion() {
    var expresionRelacional1:ExpresionRelacional? = null
    var expresionRelacional2:ExpresionRelacional? = null
    var operador: Token? = null
    var expresionAritmetica:ExpresionAritmetica? = null

    constructor(expresionRelacional1:ExpresionRelacional?, operador: Token?, expresionRelacional2:ExpresionRelacional?):this(){
        this.expresionRelacional1 = expresionRelacional1
        this.operador = operador
        this.expresionRelacional2 = expresionRelacional2
    }

    constructor(expresionRelacional1:ExpresionRelacional?):this(){
        this.expresionRelacional1 = expresionRelacional1
    }

    constructor(expresionAritmetica:ExpresionAritmetica?, operador: Token?, expresionRelacional2:ExpresionRelacional?):this(){
        this.expresionAritmetica = expresionAritmetica
        this.operador = operador
        this.expresionRelacional2 = expresionRelacional2
    }

    constructor(expresionAritmetica:ExpresionAritmetica?):this(){
        this.expresionAritmetica = expresionAritmetica
    }

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expresion Relacional")
        if (expresionRelacional1!=null){
            raiz.children.add(expresionRelacional1!!.getArbolVisual())
        }
        if (expresionRelacional2!=null){
            raiz.children.add(expresionRelacional2!!.getArbolVisual())
        }
        if (operador!=null){
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
        }
        if (expresionAritmetica!=null){
            raiz.children.add(expresionAritmetica!!.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionRelacional(expresionRelacional1=$expresionRelacional1, expresionRelacional2=$expresionRelacional2, operador=$operador, expresionAritmetica=$expresionAritmetica)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String): String {
        return "estado"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (expresionAritmetica!=null){
            expresionAritmetica!!.analizarSemantica(tablaSimbolos,listaErrores,ambito)
        }else if (expresionRelacional1!=null&&expresionRelacional2!=null){
            expresionRelacional1!!.analizarSemantica(tablaSimbolos,listaErrores,ambito)
            expresionRelacional2!!.analizarSemantica(tablaSimbolos,listaErrores, ambito)
        }
    }


}