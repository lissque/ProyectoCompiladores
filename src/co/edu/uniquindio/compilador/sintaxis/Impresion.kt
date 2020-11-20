package co.edu.uniquindio.compilador.sintaxis

class Impresion(var imprimir: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "Impresion(imprimir=$imprimir)"
    }
}