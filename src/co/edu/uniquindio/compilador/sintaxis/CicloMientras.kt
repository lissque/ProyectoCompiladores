package co.edu.uniquindio.compilador.sintaxis

class CicloMientras(var expresion: Expresion, var sentencia: ArrayList<Sentencia>?):Sentencia() {
    override fun toString(): String {
        return "CicloMientras(expresion=$expresion, sentencia=$sentencia)"
    }
}