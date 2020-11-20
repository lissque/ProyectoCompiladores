package co.edu.uniquindio.compilador.sintaxis

class InvocacionFuncion(var invocacionFuncion: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "InvocacionFuncion(invocacionFuncion=$invocacionFuncion)"
    }
}