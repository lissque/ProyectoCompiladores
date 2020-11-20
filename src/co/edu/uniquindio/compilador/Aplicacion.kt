package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico

fun main()
{

    var decision = "si ¿8()12? ¡&a&@++/! dlc ¡&a&@++/!"
    val lexico = AnalizadorLexico("si ¿8()12? ¡&a&@++/! dlc ¡&a&@++/!")
    lexico.analizar()
    //print(lexico.listaTokens)

    val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esDecision())
   // print(sintaxis.listaErrores)



}