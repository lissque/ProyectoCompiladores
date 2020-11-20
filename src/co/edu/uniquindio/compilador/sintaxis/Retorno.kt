package co.edu.uniquindio.compilador.sintaxis

class Retorno(var retorno: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "Retorno(retorno=$retorno)"
    }
}