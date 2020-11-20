package co.edu.uniquindio.compilador.sintaxis

class Impresion(var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "Impresion(expresion=$expresion)"
    }
}