package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico

fun main()
{
    val lexico = AnalizadorLexico(":::")
    lexico.analizar()
    println(lexico.listaTokens)
}