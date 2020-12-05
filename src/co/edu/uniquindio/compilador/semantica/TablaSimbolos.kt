package co.edu.uniquindio.compilador.semantica
import co.edu.uniquindio.compilador.lexico.Error

class TablaSimbolos (var listaErrores: ArrayList<Error>) {


    var listaSimbolos: ArrayList<Simbolo> = ArrayList()
    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     */
    fun guardarSimboloVariable(nombre: String, tipo: String,modificable:Boolean, ambito: String, fila: Int,
                               columna: Int): Simbolo? {
        val s = buscarSimboloVariable(nombre, ambito)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo,modificable, ambito, fila, columna )
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(Error("el campo con el $nombre ya existe dentro del $ambito",fila, columna))
        }
        return null
    }
    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     */
    fun guardarSimboloFuncion(nombre: String, tipo: String, tipoParametros:ArrayList<String>,ambito:String, fila: Int,
                              columna: Int): Simbolo? {
        val s = buscarSimboloFuncion(nombre, tipoParametros)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo, tipoParametros,ambito)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(Error("la funcion con el $nombre ya existe dentro del $ambito",fila, columna))
        }
        return null
    }
    fun buscarSimboloVariable(nombre: String, ambito: String): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.ambito != null) {
                if (nombre == simbolo.nombre && ambito == simbolo.ambito) {
                    return simbolo
                }
            }
        }
        return null
    }
    fun buscarSimboloFuncion(nombre: String, tiposParametros: ArrayList<String>):
            Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.tipoParametros != null) {
                if (nombre == simbolo.nombre && tiposParametros ==
                        simbolo.tipoParametros) {
                    return simbolo
                }

            }
        }
        return null
    }

    override fun toString(): String {
        return "TablaSimbolos(listaSimbolos=$listaSimbolos)"
    }


}