package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class ExpresionAritmetica(): Expresion() {

    var expresionAritmetica1:ExpresionAritmetica? = null
    var expresionAritmetica2:ExpresionAritmetica? = null
    var operador:Token? = null
    var valorNumerico:ValorNumerico? = null

    constructor(expresionAritmetica1: ExpresionAritmetica?,operador: Token?,expresionAritmetica2: ExpresionAritmetica?):this(){
        this.expresionAritmetica1 = expresionAritmetica1
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    constructor(expresionAritmetica1: ExpresionAritmetica?):this(){
        this.expresionAritmetica1 = expresionAritmetica1
    }

    constructor(valorNumerico: ValorNumerico?,operador: Token?,expresionAritmetica2: ExpresionAritmetica?):this(){
        this.valorNumerico = valorNumerico
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    constructor(valorNumerico: ValorNumerico?):this(){
        this.valorNumerico = valorNumerico
    }

    override open fun getArbolVisual(): TreeItem<String>{
        var raiz = TreeItem("Expresion Aritmetica")
        if (expresionAritmetica1!=null){
            raiz.children.add(expresionAritmetica1!!.getArbolVisual())
        }
        if (expresionAritmetica2!=null){
            raiz.children.add(expresionAritmetica2!!.getArbolVisual())
        }
        if (operador!=null){
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
        }
        if (valorNumerico!=null){
            raiz.children.add(valorNumerico!!.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionAritmetica(expresionAritmetica1=$expresionAritmetica1, expresionAritmetica2=$expresionAritmetica2, operador=$operador, valorNumerico=$valorNumerico)"
    }


}