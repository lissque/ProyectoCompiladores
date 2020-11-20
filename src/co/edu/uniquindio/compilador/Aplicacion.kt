package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico

fun main()
{
    val lexico = AnalizadorLexico("Inicio ; mut vnum &a& :: 1@+2 / ")
    //val lexico = AnalizadorLexico("mut")
    //val lexico = AnalizadorLexico("&a&@++/")
    lexico.analizar()
    //print(lexico.listaTokens)

    val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esUnidadDeCompilacion())
    print(sintaxis.listaErrores)
}