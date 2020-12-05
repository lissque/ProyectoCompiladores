package co.edu.uniquindio.compilador.semantica

class Simbolo {

    var nombre:String= ""
    var tipo:String=""
    var modificable:Boolean=false
    var ambito:String?=""
    var fila:Int=0
    var columna:Int=0
    var tipoParametros: ArrayList<String>? = null

    /**
     * constructor para crear un simbolo de tipo valor
     */

    constructor(nombre:String,tipoDato:String,modificable:Boolean,ambito:String,fila:Int,columna:Int){

        this.nombre=nombre
        this.tipo=tipoDato
        this.modificable=modificable
        this.ambito=ambito
        this.fila=fila
        this.columna=columna

    }

    /**
     * constructor para crear un simbolo de tipo metodo(funcion)
     */
    constructor(nombre:String,tipoRetorno:String,tipoParametros:ArrayList<String>,ambito:String) {
        this.nombre = nombre
        this.tipo = tipoRetorno
        this.tipoParametros = tipoParametros
        this.ambito = ambito
    }

    override fun toString(): String {

        if(tipoParametros==null){
            return "Simbolo(nombre='$nombre', tipo='$tipo', modificable=$modificable, ambito=$ambito, fila=$fila, columna=$columna)"
        }
        return "Simbolo(nombre='$nombre', tipo='$tipo', tipoParametros=$tipoParametros, ambito=$ambito)"
    }


}