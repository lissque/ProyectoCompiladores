package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Categoria
import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token

class AnalizadorSintactico(var listaTokens:ArrayList<Token>) {

    var posicionActual = 0
    var tokenActual = listaTokens[0]
    var listaErrores = ArrayList<Error>()

    fun obtenerSiguienteToken(){

        posicionActual++

        if(posicionActual < listaTokens.size){
            tokenActual = listaTokens[posicionActual]
        }
    }

    /**
     * <UnidadDeCompilacion> ::= Inicio ";" [<DeclaracionVariable>] [<ListaFunciones>] Fin
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        val listaFunciones: ArrayList<Funcion> = esListaFunciones()

        if (listaFunciones.size > 0) {

            return UnidadDeCompilacion(listaFunciones)
        } else{

            return null
        }
    }

    /**
     * <ListaFunciones> ::= <Funcion> [<Funcion>]
     */
    fun esListaFunciones(): ArrayList<Funcion> {

        var listaFunciones = ArrayList<Funcion>()
        var funcion = esFuncion()

        while (funcion != null) {
            listaFunciones.add(funcion)
            funcion = esFuncion()
        }


        return listaFunciones
    }

    /**
     * <Funcion> ::= fun identificador "¿" [<ListaParametros>] "?" ";" <tipoRetorno> "¡"<ListaSentencias> "!"
     */
    fun esFuncion(): Funcion?{

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "fun"){
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                var nombreFuncion = tokenActual
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                    obtenerSiguienteToken()
                    var listaParametros = esListaParametros()

                    if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                        obtenerSiguienteToken()

                        if(tokenActual.categoria == Categoria.DOS_PUNTOS){
                            obtenerSiguienteToken()
                            var tipoRetorno = esTipoRetorno()

                            if (tipoRetorno != null){
                                obtenerSiguienteToken()

                                if (tokenActual.categoria == Categoria.ADMIRACIONIZQ) {
                                    obtenerSiguienteToken()
                                    var listaSentencias = esListaSentencias()

                                    if (listaSentencias != null) {
                                        obtenerSiguienteToken()

                                        if (tokenActual.categoria == Categoria.ADMIRACIONDER) {
                                            return Funcion(nombreFuncion,tipoRetorno,listaParametros,listaSentencias)
                                        }else{
                                            reportarError("Falta signo de admiración derecho")
                                        }
                                    }else{
                                        reportarError("La lista de sentencias esta vacia")
                                    }
                                }else{
                                    reportarError("Falta signo de admiración izquierdo")
                                }
                            }else{
                                reportarError("Falta el tipo de retorno")
                            }
                        }else{
                            reportarError("Falta el punto y coma")
                        }
                    }else{
                        reportarError("Falta signo de interrogación derecho")
                    }
                }else{
                    reportarError("Falta signo de interrogación izquierdo")
                }
            }else{
                reportarError("Falta el nombre de la funcion")
            }

        }
        return null
    }

    /**
     * <ListaParametros> ::= <Parametro> ["^" <ListaParametros>]
     */
    fun esListaParametros(): ArrayList<Parametro>? {

        var listaParametros = ArrayList<Parametro>()
        var parametro = esParametro()

        while (parametro != null) {

            listaParametros.add(parametro)

            if(tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                parametro = esParametro()
            }else{
                if (tokenActual.categoria != Categoria.INTERROGACIONIZQ){

                    reportarError("Falta un separador en la lista de parametros")
                }
                break
            }

        }
        if (listaParametros.size > 0){
            return listaParametros
        }

        return null
    }

    /**
     * <Parametro> ::= <TipoDato> identificador
     */
    fun esParametro(): Parametro?{
        var tipoDeDato = esTipoDeDato()

        if(tipoDeDato != null){
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                var nombreParametro = tokenActual
                return Parametro(nombreParametro,tipoDeDato)
            }
        }
        return null
    }







    /**
     * <TipoDato> ::= vnum | vcd | v
     */
    fun esTipoDeDato(): Token?{

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA){

            if (tokenActual.lexema == "vnum" || tokenActual.lexema == "vcd" || tokenActual.lexema == "v"){
                return tokenActual
            }
        }

        return null
    }

    /**
     * <TipoRetorno> ::= <TipoDato> | noRetorno
     */
    fun esTipoRetorno(): Token?{

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA){
            var tipoDeDato = esTipoDeDato()

            if (tipoDeDato != null || tokenActual.lexema == "noRetorno"){
                return tokenActual
            }
        }
        return null
    }

    /**
     * <Expresion> ::= <ExpresionLogica> | <ExpresionAritmetica> | <ExpresionRelacional> | <ExpresionCadena>
     *
     * */
    fun esExpresion(): Expresion?{

        var expresion:Expresion? = esExpresionLogica()

        if (expresion != null){
            return expresion
        }
        expresion = esExpresionAritmetica()
        if (expresion != null){
            return expresion
        }
        expresion = esExpresionRelacional()
        if (expresion != null){
            return expresion
        }
        expresion = esExpresionCadena()
        return expresion

    }

    /**
     * <ExpresionAritmetica> ::= <ExpresionAritmetica> operadorAritmetico <ExpresionAritmetica>
     *  |"¿"<ExpresionAritmetica>"?" | <ValorNumerico>
     *
     *  <ExpAritmetica> ::= "¿"<ExpAritmetica>"?" [operadorAritmetico <ExpAritmetica>] |
    <ValorNumerico> [operadorAritmetico <ExpAritmetica>]
     */
    fun esExpresionAritmetica(): ExpresionAritmetica? {

        var listaExpresionAritmetica = ArrayList<String>()
        var posicion=posicionActual

        if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
            listaExpresionAritmetica.add(""+tokenActual)
            obtenerSiguienteToken()
            var valorNumerico = esValorNumerico()

            while (valorNumerico != null){
                listaExpresionAritmetica.add(""+valorNumerico)
                if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO){
                    listaExpresionAritmetica.add(""+tokenActual)
                    obtenerSiguienteToken()
                    valorNumerico = esValorNumerico()

                }else{
                    break
                }
            }
            if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                listaExpresionAritmetica.add(""+tokenActual)
                obtenerSiguienteToken()
                return ExpresionAritmetica(listaExpresionAritmetica)
            }else{
                hacerBacktracking(posicion)

                return null
            }
        }
        var expresion= esValorNumerico()
        if (expresion!=null){
            listaExpresionAritmetica.add(""+expresion)
            while (tokenActual.categoria==Categoria.OPERADOR_ARITMETICO){
                listaExpresionAritmetica.add(""+tokenActual)
                obtenerSiguienteToken()
                expresion=esValorNumerico()
                if (expresion!=null){
                    listaExpresionAritmetica.add(""+expresion)
                }else{
                    break
                }

            }
            if(listaExpresionAritmetica.size>0){
                return ExpresionAritmetica(listaExpresionAritmetica)
            }

        }

        return null
    }

    /**
     * <ExpresionRelacional> ::= "¿"<ExpresionRelacional>"?" [operadorRelacional <ExpresionRelacional>] |
    <ExpresionAritmetica> [operadorRelacional <ExpresionRelacional>]
     *
     * <ExpresionRelacional> ::="<ExpresionRelacional> operadorRelacional <ExpresionRelacional> |
    "¿"<ExpresionRelacional>"?" | <ExpresionAritmetica>
     */

    fun esExpresionRelacional(): ExpresionRelacional? {

        var listaExpresionRelacional = ArrayList<String>()
        var expresionAritmetica= esExpresionAritmetica()

        if (expresionAritmetica!=null){
            listaExpresionRelacional.add(""+expresionAritmetica)
            while (tokenActual.categoria==Categoria.OPERADOR_RELACIONAL){
                listaExpresionRelacional.add(""+tokenActual)
                obtenerSiguienteToken()
                expresionAritmetica=esExpresionAritmetica()
                if (expresionAritmetica!=null){
                    listaExpresionRelacional.add(""+expresionAritmetica)
                }else{
                    break
                }

            }
            if(listaExpresionRelacional.size>0){
               // obtenerSiguienteToken()
                return ExpresionRelacional(listaExpresionRelacional)

            }
            else{
                return null
            }
        }
        if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
            listaExpresionRelacional.add(""+tokenActual)
            obtenerSiguienteToken()
            expresionAritmetica = esExpresionAritmetica()
            while (expresionAritmetica != null){
                listaExpresionRelacional.add(""+expresionAritmetica)
                if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL){
                    listaExpresionRelacional.add(""+tokenActual)
                    obtenerSiguienteToken()
                    expresionAritmetica = esExpresionAritmetica()

                }else{
                    break
                }
            }
            if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                listaExpresionRelacional.add(""+tokenActual)
                obtenerSiguienteToken()
                return ExpresionRelacional(listaExpresionRelacional)
            }else{
                return null
            }

        }
        return null
    }

    /**
     * <ExpresionLogica> ::= <ExpresionLogica> operadorLogico <ExpresionLogica> | "¿"<ExpresionLogica>"?" | <ExpresionRelacional>
     *
     *  <ExpresionLogica> ::= "¿"<ExpresionLogica>"?" [operadorLogica <ExpresionLogica>] |
    <ExpresionRelacional> [operadorLogica <ExpresionLogica>]
     */
    fun esExpresionLogica(): ExpresionLogica? {

        var listaExpresionLogica = ArrayList<String>()
        var expresionRelacional= esExpresionRelacional()
        if (expresionRelacional!=null){
            listaExpresionLogica.add(""+expresionRelacional)
            println(tokenActual)
            while (tokenActual.categoria==Categoria.OPERADOR_LOGICO){
                listaExpresionLogica.add(""+tokenActual)
                obtenerSiguienteToken()
                expresionRelacional=esExpresionRelacional()
                if (expresionRelacional!=null){
                    listaExpresionLogica.add(""+expresionRelacional)
                }else{
                    break
                }

            }
            if(listaExpresionLogica.size>0){
                return ExpresionLogica(listaExpresionLogica)

            }
            else{
                return null
            }
        }
        if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
            listaExpresionLogica.add(""+tokenActual)
            obtenerSiguienteToken()
            expresionRelacional = esExpresionRelacional()
            while (expresionRelacional != null){
                listaExpresionLogica.add(""+expresionRelacional)
                  obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_LOGICO){
                    listaExpresionLogica.add(""+tokenActual)
                    obtenerSiguienteToken()
                    expresionRelacional = esExpresionRelacional()

                }else{
                    break
                }
            }
            if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                listaExpresionLogica.add(""+tokenActual)
                return ExpresionLogica(listaExpresionLogica)
            }else{
                return null
            }

        }
        return null
    }

    /**
     *
    <ExpresionCadena> ::= cadena [<Concatenador> <Expresion> ] | <Expresion> <Concatenador>
    cadena
     *
     *
     */

    fun esExpresionCadena():ExpresionCadena?{
        var listaExpresionCadena = ArrayList<String>()
        var expresion=esExpresionAritmetica()

        if(tokenActual.categoria==Categoria.CADENA){
            listaExpresionCadena.add(""+tokenActual)
            obtenerSiguienteToken()
            if(tokenActual.categoria==Categoria.OPERADOR_ARITMETICO&&tokenActual.lexema=="@+"){
                listaExpresionCadena.add(""+tokenActual)
                expresion=esExpresionAritmetica()
                if(expresion!=null){
                    listaExpresionCadena.add(""+expresion)
                    return ExpresionCadena(listaExpresionCadena)
                }else{
                    return null
                }
            }

        }

        if(expresion!=null){
            listaExpresionCadena.add(""+expresion)
            if(tokenActual.categoria==Categoria.OPERADOR_ARITMETICO&&tokenActual.lexema=="@+") {
                listaExpresionCadena.add("" + tokenActual)
                obtenerSiguienteToken()
                if(tokenActual.categoria==Categoria.CADENA) {
                    listaExpresionCadena.add("" + tokenActual)
                    return ExpresionCadena(listaExpresionCadena)
                }else{
                    return null
                }


            }
        }
        return null

    }

    private fun hacerBacktracking(posInicial:Int){
        posicionActual=posInicial
        tokenActual= listaTokens[posicionActual]
    }

    /**
     * <ListaSentencias> ::= <Sentencia>[<ListaSentencias>]
     */
    fun esListaSentencias(): ArrayList<Sentencia>? {
        var listaSentencias = ArrayList<Sentencia>()
       // var sentencia = esSentencia()

      /**  while (sentencia != null) {

            listaSentencias.add(sentencia)
            obtenerSiguienteToken()

            if(tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                sentencia = esSentencia()
            }else{
                //reportarError("Falta el separador")
                break
            }

        }
      */
        if (listaSentencias.size > 0){
            return listaSentencias
        }

        return null
    }

    /**
     * <Sentencia> ::= <Decision> | <DeclaracionVariables> | <Asignacion> |<CicloMientras> | <Retorno> | <Impresion>
     *  | <Lectura> | <InvocacionFuncion> | <Incremento> | <Decremento> | <DeclaracionArreglo> | <HacerMientras> | <Detener>
     *
    fun esSentencia(): Sentencia?{
        var sentencia:Sentencia? = esSentencia()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esDeclaracionVariable()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esAsignacion()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esCicloMientras()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esRetorno()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esImpresion()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esLectura()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esInvocacionFuncion()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esIncremento()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esDecremento()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esDeclaracionArreglo()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esHacerMientras()
        if(sentencia != null){
            return sentencia
        }
        sentencia = esDetener()
        return sentencia
    }
*/
    /**
     * si "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!" [<DeLoContrario>]
     */
    fun esDecision(): Decision?{

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "si"){
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){

                return null
            }

        }
        return null
    }


    /**
     * <Incremento> ::= identificador operadorIncremento "/"
     */
    fun esIncremento(): Incremento?{
        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            var nombreVariable = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.OPERADOR_INCREMENTO){
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Incremento(nombreVariable)
                }

            }
        }

        return null
    }

    /**
     * <Decremento> ::= identificador operadorDecremento "/"
     */
    fun esDecremento(): Decremento?{
        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            var nombreVariable = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.OPERADOR_DECREMENTO){
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Decremento(nombreVariable)
                }

            }
        }

        return null
    }

    /**
     *<Detener> ::= detener "/"
     */
    fun esDetener(): Detener?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "detener"){
            var sentenciaDetener = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                return Detener(sentenciaDetener)
            }
        }

        return null
    }

    /**
     * <ValorNumerico> ::= [<Signo>] <Numerico> | [<Signo>] identificador
     */
    fun esValorNumerico(): ValorNumerico?{
        var signo = esSigno()
        if(signo!=null) {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.NUMERICO || tokenActual.categoria == Categoria.IDENTIFICADOR){
                var numero = tokenActual
                obtenerSiguienteToken()
                return ValorNumerico(signo,numero)
            }
        }
        if (tokenActual.categoria == Categoria.NUMERICO || tokenActual.categoria == Categoria.IDENTIFICADOR){
            var numero = tokenActual
            obtenerSiguienteToken()
            return ValorNumerico(signo,numero)
        }

        return null
    }

    /**
     * <Variable> ::= mut | inm
     */
    fun esVariable(): Token? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            var tipoVariable = tokenActual
            return tipoVariable
        }

        return null
    }

    /**
     *<Concatenador> ::= @+
     */
    fun esConcatenador(): Token? {
        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual.lexema == "@+") {
            var concatenador = tokenActual
            return concatenador
        }

        return null
    }

    /**
     *<Signo> ::= @+ | @-
     */
    fun esSigno(): Token? {
        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual.lexema == "@+" || tokenActual.lexema == "@-")) {

            var signo = tokenActual
            return signo
        }

        return null
    }


    fun reportarError(mensaje:String){
        listaErrores.add( Error(mensaje, tokenActual.fila, tokenActual.columna))
    }

}