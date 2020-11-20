package co.edu.uniquindio.compilador.observable

class ErrorObservable (var lexema:String, var categoria: String, var fila: String, var columna: String) {

    override fun toString(): String {
        return "ErrorObservable(lexema=${lexema}, categoria=${categoria}, fila=${fila}, columna=${columna}"
    }
}