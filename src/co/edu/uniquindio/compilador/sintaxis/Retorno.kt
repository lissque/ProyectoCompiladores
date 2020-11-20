package co.edu.uniquindio.compilador.sintaxis

class Retorno(var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }
}