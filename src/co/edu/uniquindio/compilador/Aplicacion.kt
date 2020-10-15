package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico

fun main()
{
    val lexico = AnalizadorLexico("!12345678912!214541")
    lexico.analizar()
    println(lexico.listaTokens)
}