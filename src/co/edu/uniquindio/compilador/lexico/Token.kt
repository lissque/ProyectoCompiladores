package co.edu.uniquindio.compilador.lexico

import co.edu.uniquindio.compilador.lexico.Categoria

class Token (var lexema:String, var categoria: Categoria, var fila:Int, var columna:Int) {

    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna)"
    }
}