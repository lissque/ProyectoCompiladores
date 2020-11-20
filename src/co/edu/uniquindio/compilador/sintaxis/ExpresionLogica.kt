package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

class ExpresionLogica(): Expresion() {

    var vl:ExpresionRelacional? = null
    var operador:Token? = null
    var expresionLogica: ExpresionLogica? = null

    constructor(vl:ExpresionRelacional?, operador:Token?, expresionLogica: ExpresionLogica?):this(){
        this.vl = vl
        this.operador = operador
        this.expresionLogica = expresionLogica
    }

    constructor(vl:ExpresionRelacional?):this(){
        this.vl = vl
    }

    override fun toString(): String {
        return "ExpresionLogica(vl=$vl, operador=$operador, expresionLogica=$expresionLogica)"
    }


}