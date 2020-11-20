package co.edu.uniquindio.compilador.sintaxis

class HacerMientras(var invocacionFuncion: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "HacerMientras(invocacionFuncion=$invocacionFuncion)"
    }
}