package co.edu.uniquindio.compilador.sintaxis

class Lectura(var leer: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "Lectura(leer=$leer)"
    }
}