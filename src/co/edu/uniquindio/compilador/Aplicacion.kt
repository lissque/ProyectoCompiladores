package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico

fun main()
{

    val decision = "si ¿8()9? ¡&A&@--/! dlc ¡&A&@++/!"
    val declaracionVariable = "mut vcd &numero& :: 1@+2 /"
    val asignacion = "&numero& :: 1@+2 /"
    val cicloMientras = "mientras ¿8()9? ¡&A&@--/!"
    val retorno = "retornar 1@+2 /"
    val impresion = "imprimir 1@+2 /"
    val lectura = "leer 1@+2 /"
    val invocacionFuncion = "&UNO& ¿ 1^2 ? /"
    val incremento = "&A&@++/"
    val decremento = "&A&@--/"
    val declaracionArreglo = "mut vnum &UMF& [ ] :: [1^2^48] /"
    val hacerMientras = "hacer ¡ &a&@++/ ! mientras ¿ 1 >> 2?/"
    val lexico = AnalizadorLexico(hacerMientras)
    lexico.analizar()

    val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esHacerMientras())
    println(sintaxis.listaErrores)
}