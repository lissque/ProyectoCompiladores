package co.edu.uniquindio.compilador.sintaxis

class Decision(var listaDescision:ArrayList<String>): Sentencia() {
    override fun toString(): String {
        return "Decision(listaDescision=$listaDescision)"
    }
}