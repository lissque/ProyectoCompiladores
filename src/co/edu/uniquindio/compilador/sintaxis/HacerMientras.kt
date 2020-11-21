package co.edu.uniquindio.compilador.sintaxis

class HacerMientras(var listaSentencias: ArrayList<Sentencia>, var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "HacerMientras(listaSentencias=$listaSentencias, expresion=$expresion)"
    }
}