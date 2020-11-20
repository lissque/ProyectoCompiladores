package co.edu.uniquindio.compilador

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico

fun main()
{

<<<<<<< Updated upstream
    var decision = "si ¿8()12? ¡&a&@++/! dlc ¡&a&@++/!"
    val lexico = AnalizadorLexico("si ¿8()12? ¡&a&@++/! dlc ¡&a&@++/!")
=======
    val lexico = AnalizadorLexico("haha")
>>>>>>> Stashed changes
    lexico.analizar()

<<<<<<< Updated upstream
    val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esDecision())
   // print(sintaxis.listaErrores)
=======
    /*val sintaxis = AnalizadorSintactico(lexico.listaTokens)
    print(sintaxis.esUnidadDeCompilacion())
    print(sintaxis.listaErrores)*/
>>>>>>> Stashed changes

    print(lexico.listaErrores.size)


}