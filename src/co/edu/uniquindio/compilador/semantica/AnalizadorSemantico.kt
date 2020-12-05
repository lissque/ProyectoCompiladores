package co.edu.uniquindio.compilador.semantica

import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.sintaxis.UnidadDeCompilacion

class AnalizadorSemantico(var unidadDeCompilacion: UnidadDeCompilacion) {

    var listaErrores: ArrayList<Error> = ArrayList()
    var tablaSimbolos: TablaSimbolos = TablaSimbolos(listaErrores)

    fun llenarTablaSimbolos() {
        unidadDeCompilacion.llenarTablaSimbolos(tablaSimbolos, listaErrores)
    }
    fun analizarSemantica() {
        unidadDeCompilacion.analizarSemantica(tablaSimbolos, listaErrores)
    }
}