package co.edu.uniquindio.compilador.observable

class ErrorSintacticoObservable(var mensaje:String, var fila: String, var columna: String) {
    override fun toString(): String {
        return "ErrorSintacticoObservable(mensaje='$mensaje', fila='$fila', columna='$columna')"
    }
}