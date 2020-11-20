package co.edu.uniquindio.compilador.sintaxis

class Asignacion(var asignacion: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "Asignacion(asignacion=$asignacion)"
    }
}