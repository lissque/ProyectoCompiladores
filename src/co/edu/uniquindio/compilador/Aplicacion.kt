package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico

fun main()
{

    val lexico = AnalizadorLexico("&a&@++/")
    lexico.analizar()
    //print(lexico.listaTokens)

    val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esIncremento())
    print(sintaxis.listaErrores)



}