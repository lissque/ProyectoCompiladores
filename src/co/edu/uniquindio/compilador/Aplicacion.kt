package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico

fun main()
{
    val lexico = AnalizadorLexico("%jnidjenvwinv%")
    lexico.analizar()
    println(lexico.listaTokens)


}