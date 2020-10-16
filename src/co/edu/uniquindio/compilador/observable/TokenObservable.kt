package co.edu.uniquindio.compilador.observable

class TokenObservable(var lexema:String, var categoria: String, var fila: String, var columna: String){
    override fun toString(): String {
        return "TokenObservable(lexema=${lexema}, categoria=${categoria}, fila=${fila}, columna=${columna}"
    }
}