package co.edu.uniquindio.compilador.sintaxis

class DeclaracionVariable(var declaracionVariable: ArrayList<String>):Sentencia() {
    override fun toString(): String {
        return "DeclaracionVariable(declaracionVariable=$declaracionVariable)"
    }
}