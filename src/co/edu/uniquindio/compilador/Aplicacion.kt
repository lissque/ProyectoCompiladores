package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico

fun main()
{
    //val lexico = AnalizadorLexico("::")
    //lexico.analizar()
    //println(lexico.listaTokens)
    var a=0
    var b=2
    do {
        println(a)
        a++
        println(a)
    } while (a<b);

    println(a)
    println(b)
}