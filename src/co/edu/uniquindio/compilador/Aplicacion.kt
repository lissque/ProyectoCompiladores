package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico

fun main()
{
    val lexico = AnalizadorLexico("_sahsah56 hs 21.12")
    lexico.analizar()
    print(lexico.listaTokens)
}