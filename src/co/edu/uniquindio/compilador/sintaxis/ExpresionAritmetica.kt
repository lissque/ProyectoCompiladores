package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Categoria
import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionAritmetica(): Expresion() {

    var expresionAritmetica1: ExpresionAritmetica? = null
    var expresionAritmetica2: ExpresionAritmetica? = null
    var operador: Token? = null
    var valorNumerico: ValorNumerico? = null

    constructor(expresionAritmetica1: ExpresionAritmetica?, operador: Token?, expresionAritmetica2: ExpresionAritmetica?) : this() {
        this.expresionAritmetica1 = expresionAritmetica1
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    constructor(expresionAritmetica1: ExpresionAritmetica?) : this() {
        this.expresionAritmetica1 = expresionAritmetica1
    }

    constructor(valorNumerico: ValorNumerico?, operador: Token?, expresionAritmetica2: ExpresionAritmetica?) : this() {
        this.valorNumerico = valorNumerico
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    constructor(valorNumerico: ValorNumerico?) : this() {
        this.valorNumerico = valorNumerico
    }

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expresion Aritmetica")
        if (expresionAritmetica1 != null) {
            raiz.children.add(expresionAritmetica1!!.getArbolVisual())
        }
        if (expresionAritmetica2 != null) {
            raiz.children.add(expresionAritmetica2!!.getArbolVisual())
        }
        if (operador != null) {
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
        }
        if (valorNumerico != null) {
            raiz.children.add(valorNumerico!!.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionAritmetica(expresionAritmetica1=$expresionAritmetica1, expresionAritmetica2=$expresionAritmetica2, operador=$operador, valorNumerico=$valorNumerico)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String): String {
        if (expresionAritmetica1 != null && expresionAritmetica2 != null && valorNumerico == null) {

            var tipo1= expresionAritmetica1!!.obtenerTipo(tablaSimbolos,ambito)
            var tipo2= expresionAritmetica2!!.obtenerTipo(tablaSimbolos,ambito)
            return "vnum"

        } else if (expresionAritmetica1 != null && expresionAritmetica2 == null && valorNumerico == null) {
            return expresionAritmetica1!!.obtenerTipo(tablaSimbolos,ambito)

        } else if (valorNumerico != null && expresionAritmetica1 != null && expresionAritmetica2 == null) {
           var tipo1= ""
            if (valorNumerico!!.numero.categoria==Categoria.NUMERICO){
                tipo1= "vnum"
            }else{
                var simbolo = tablaSimbolos.buscarSimboloVariable(valorNumerico!!.numero.lexema,ambito)
                if (simbolo!=null){
                    tipo1=  simbolo.tipo
                }
            }
            var tipo2= expresionAritmetica1!!.obtenerTipo(tablaSimbolos,ambito)

            if(tipo2=="vnum"){
                return tipo2
            }

        } else if (expresionAritmetica1 == null && expresionAritmetica2 == null && valorNumerico != null) {
            if (valorNumerico!!.numero.categoria==Categoria.NUMERICO){
                return "vnum"
            }else{
                var simbolo = tablaSimbolos.buscarSimboloVariable(valorNumerico!!.numero.lexema,ambito)
                if (simbolo!=null){
                    return  simbolo.tipo
                }
            }
        }
        return  ""
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (valorNumerico!=null){
            if (valorNumerico!!.numero.categoria == Categoria.IDENTIFICADOR){
                var simbolo = tablaSimbolos.buscarSimboloVariable(valorNumerico!!.numero.lexema,ambito)
                if (simbolo==null){
                    listaErrores.add(Error("El campo (${valorNumerico!!.numero.lexema}) no existe dentro del ambito (${ambito})",valorNumerico!!.numero.fila, valorNumerico!!.numero.columna))
                }
            }
        }
        if (expresionAritmetica1!=null){
            expresionAritmetica1!!.analizarSemantica(tablaSimbolos,listaErrores,ambito)
        }
        if (expresionAritmetica2!=null){
            expresionAritmetica2!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }
}