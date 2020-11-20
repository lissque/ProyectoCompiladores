package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico

fun main()
{
<<<<<<< Updated upstream
    val lexico = AnalizadorLexico("Inicio ; mut vnum &a& :: 1@+2 / ")
    //val lexico = AnalizadorLexico("mut")
=======
    //val lexico = AnalizadorLexico("Inicio ; mut vnum &a& :: 1@+2 / Fin")

    val lexico = AnalizadorLexico("mientras ¿ 8()9 ? ¡ &a&@++/ !")
>>>>>>> Stashed changes
    //val lexico = AnalizadorLexico("&a&@++/")
    lexico.analizar()
    //print(lexico.listaTokens)

    val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esCicloMientras())
    println(sintaxis.listaErrores)
}