package co.edu.uniquindio.compilador.sintaxis

class ExpresionLogica(var listaExpresionLogica: ArrayList<String>): Expresion() {
    override fun toString(): String {
        return "ExpresionLogica(listaExpresionLogica=$listaExpresionLogica)"
    }
}