package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

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

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Logica")
        if (vl!=null){
            raiz.children.add(vl!!.getArbolVisual())
        }
        if (operador!=null){
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
        }
        if (expresionLogica!=null){
            raiz.children.add(expresionLogica!!.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionLogica(vl=$vl, operador=$operador, expresionLogica=$expresionLogica)"
    }


}