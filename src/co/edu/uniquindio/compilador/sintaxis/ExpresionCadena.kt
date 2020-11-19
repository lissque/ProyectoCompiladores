package co.edu.uniquindio.compilador.sintaxis

class ExpresionCadena(var listaExpresionCadena: ArrayList<String>): Expresion() {
    override fun toString(): String {
        return "ExpresionCadena(listaExpresionCadena=$listaExpresionCadena)"
    }
}