package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

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

    override fun toString(): String {
        return "ExpresionRelacional(expresionRelacional1=$expresionRelacional1, expresionRelacional2=$expresionRelacional2, operador=$operador, expresionAritmetica=$expresionAritmetica)"
    }


}