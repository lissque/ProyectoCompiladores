package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionCadena(): Expresion() {
    var cadena:Token? = null
    var mas: Token? = null
    var exp: Expresion? = null

    constructor(cadena:Token?, mas: Token?, exp: Expresion?):this(){
        this.cadena = cadena
        this.mas = mas
        this.exp = exp
    }

    constructor(cadena:Token?):this(){
        this.cadena = cadena
    }

    override open fun getArbolVisual():TreeItem<String>{
        var raiz = TreeItem("Expresion Cadena")
        if (cadena!=null){
            raiz.children.add(TreeItem("Cadena: ${cadena!!.lexema} "))
        }
        if (exp!=null){
            raiz.children.add(exp!!.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionCadena(cadena=$cadena, mas=$mas, exp=$exp)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String): String {
        return "vcd"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (exp!=null){
            exp!!.analizarSemantica(tablaSimbolos,listaErrores,ambito)
        }
    }
}