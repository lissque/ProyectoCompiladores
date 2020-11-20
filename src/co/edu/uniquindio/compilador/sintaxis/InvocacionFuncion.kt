package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token

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

    override fun toString(): String {
        return "InvocacionFuncion(identificadorVariable=$identificadorVariable, identificadorFuncion=$identificadorFuncion, argumentos=$argumentos)"
    }


}