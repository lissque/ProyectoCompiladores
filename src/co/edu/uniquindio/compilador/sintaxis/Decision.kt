package co.edu.uniquindio.compilador.sintaxis

class Decision(var expresionLogica: ExpresionLogica, var listaSentencia: ArrayList<Sentencia>?, var deLoContrario: DeLoContrario?): Sentencia() {
    override fun toString(): String {
        return "Decision(expresionLogica=$expresionLogica, listaSentencia=$listaSentencia, deLoContrario=$deLoContrario)"
    }
}