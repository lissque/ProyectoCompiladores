package co.edu.uniquindio.compilador.sintaxis

class ExpresionRelacional(var listaExpresionRelacional: ArrayList<String>): Expresion() {
    override fun toString(): String {
        return "ExpresionRelacional(listaExpresionRelacional=$listaExpresionRelacional)"
    }
}