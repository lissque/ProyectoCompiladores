package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class InvocacionFuncion():Sentencia() {

    var identificadorVariable:Token? = null
    var identificadorFuncion: Token? = null
    var argumentos:ArrayList<Expresion>? = null

    constructor(identificadorVariable:Token?,identificadorFuncion: Token?,argumentos:ArrayList<Expresion>?):this(){
        this.identificadorVariable = identificadorVariable
        this.identificadorFuncion = identificadorFuncion
        this.argumentos = argumentos
    }

    constructor(identificadorVariable:Token?,argumentos:ArrayList<Expresion>?):this(){
        this.identificadorVariable = identificadorVariable
        this.argumentos = argumentos
    }

    override open fun getArbolVisual():TreeItem<String>{
        var raiz = TreeItem("Invocacion a funcion")
        if (identificadorVariable!=null){
            raiz.children.add(TreeItem("Identificador Vble: ${identificadorVariable!!.lexema}"))
        }
        if (identificadorFuncion!=null){
            raiz.children.add(TreeItem("Identificador de funcion:  ${identificadorFuncion!!.lexema}"))
        }
        if (argumentos!=null){
            var raizArgumentos = TreeItem("Argumentos ")
            for (a in argumentos!!){
                raizArgumentos.children.add(a.getArbolVisual())
            }
            raiz.children.add(raizArgumentos)
        }
        return raiz
    }

    override fun toString(): String {
        return "InvocacionFuncion(identificadorVariable=$identificadorVariable, identificadorFuncion=$identificadorFuncion, argumentos=$argumentos)"
    }

    fun obtenerTiposArgumentos(tablaSimbolos: TablaSimbolos, ambito: String): ArrayList<String>{
        var listaArg = ArrayList<String>()
        for (a in argumentos!!){
            listaArg.add(a.obtenerTipo(tablaSimbolos,ambito))
        }
        return listaArg
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var listaTiposArgumentos = obtenerTiposArgumentos(tablaSimbolos,ambito)
        var s = tablaSimbolos.buscarSimboloFuncion(identificadorFuncion!!.lexema, listaTiposArgumentos)
        if (s==null){
            listaErrores.add(co.edu.uniquindio.compilador.lexico.Error("La funcion ${identificadorFuncion!!.lexema} $listaTiposArgumentos no existe", identificadorFuncion!!.fila,identificadorFuncion!!.columna))
        }
    }
}