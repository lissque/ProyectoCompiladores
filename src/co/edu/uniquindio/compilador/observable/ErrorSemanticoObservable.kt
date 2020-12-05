package co.edu.uniquindio.compilador.observable

class ErrorSemanticoObservable(var mensaje:String, var fila: String, var columna: String) {
    override fun toString(): String {
        return "ErrorSemanticoObservable(mensaje='$mensaje', fila='$fila', columna='$columna')"
    }
}