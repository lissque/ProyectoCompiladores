package co.edu.uniquindio.compilador.sintaxis

class DeclaracionArreglo(var invocacionFuncion: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "DeclaracionArreglo(invocacionFuncion=$invocacionFuncion)"
    }
}