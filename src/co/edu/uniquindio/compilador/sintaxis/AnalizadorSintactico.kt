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
     *  <ValorNumerico> [operadorAritmetico <ExpAritmetica>]
     */
    fun esExpresionAritmetica(): ExpresionAritmetica? {


        if (tokenActual.categoria == Categoria.INTERROGACIONIZQ) {

            obtenerSiguienteToken()
            val eA = esExpresionAritmetica()
            if (eA != null) {

                if (tokenActual.categoria == Categoria.INTERROGACIONDER) {

                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                        val operador = tokenActual

                        obtenerSiguienteToken()
                        val exp2 = esExpresionAritmetica()
                        if (exp2 != null) {


                            return ExpresionAritmetica(eA, operador, exp2)
                        }
                    } else {

                        return ExpresionAritmetica(eA)
                    }
                }
            }
        }else{
            val valor = esValorNumerico()
            if (valor != null) {

                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                    val operador = tokenActual

                    obtenerSiguienteToken()
                    val exp = esExpresionAritmetica()
                    if (exp != null) {

                        return ExpresionAritmetica(valor, operador, exp)
                    }
                } else {

                    return ExpresionAritmetica(valor)
                }
            }
        }
        return null
    }

    /**
     * <ExpresionRelacional> ::= "¿"<ExpresionRelacional>"?" [operadorRelacional <ExpresionRelacional>] |
     * <ExpresionAritmetica> [operadorRelacional <ExpresionRelacional>]
     *
     * <ExpresionRelacional> ::="<ExpresionRelacional> operadorRelacional <ExpresionRelacional> |
     * "¿"<ExpresionRelacional>"?" | <ExpresionAritmetica>
     */

    fun esExpresionRelacional(): ExpresionRelacional? {


        if (tokenActual.categoria == Categoria.INTERROGACIONIZQ) {

            obtenerSiguienteToken()
            val eA = esExpresionRelacional()
            if (eA != null) {

                if (tokenActual.categoria == Categoria.INTERROGACIONDER) {

                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL) {
                        val operador = tokenActual

                        obtenerSiguienteToken()
                        val exp2 = esExpresionRelacional()
                        if (exp2 != null) {

                            return ExpresionRelacional(eA, operador, exp2)
                        }
                    } else {

                        return ExpresionRelacional(eA)
                    }
                }
            }
        }else{
            val valor = esExpresionAritmetica()
            if (valor != null) {

                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL) {
                    val operador = tokenActual

                    obtenerSiguienteToken()
                    val exp = esExpresionRelacional()
                    if (exp != null) {

                        return ExpresionRelacional(valor, operador, exp)
                    }
                } else {
                    return ExpresionRelacional(valor)
                }
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
        val vl = esExpresionRelacional()

        if (vl != null) {

            if (tokenActual.categoria == Categoria.OPERADOR_LOGICO && (tokenActual.lexema == "()" || tokenActual.lexema == "¬¬")) {
                val operadorLogico = tokenActual
                obtenerSiguienteToken()
                val expresionLogica = esExpresionLogica()
                if (expresionLogica != null) {
                    return ExpresionLogica(vl, operadorLogico, expresionLogica)
                }
            } else if (tokenActual.categoria == Categoria.OPERADOR_LOGICO && (tokenActual.lexema == "{}")) {
                val operadorNegacion = tokenActual
                obtenerSiguienteToken()
                val expresionLogica = esExpresionLogica()
                if (expresionLogica != null) {
                    return ExpresionLogica(vl, operadorNegacion, expresionLogica)
                }
            } else {
                return ExpresionLogica(vl)
            }
        }
        return null
    }

    /**
     *<ExpresionCadena> ::= cadena [<Concatenador> <Expresion> ] | <Expresion> <Concatenador> cadena
     *
     */

    fun esExpresionCadena():ExpresionCadena?{

        if (tokenActual.categoria == Categoria.CADENA) {
            var cadena = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria != Categoria.OPERADOR_ARITMETICO && tokenActual.lexema != "@+") {
                return ExpresionCadena(cadena)
            } else {
                var mas = tokenActual
                obtenerSiguienteToken()
                var ex = esExpresion()
                if (ex != null) {
                    return ExpresionCadena(cadena, mas, ex)
                } else {
                    reportarError("Falta la expresion")
                }
            }
        }
        return null

    }

    /**
     * <Decision> ::= si "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!" [<DeLoContrario>]
     */

    private fun hacerBacktracking(posInicial:Int){
        posicionActual=posInicial
        tokenActual= listaTokens[posicionActual]
    }

    /**
     * <ListaSentencias> ::= <Sentencia>[<ListaSentencias>]
     */
    fun esListaSentencias(): ArrayList<Sentencia>? {
        var listaSentencias = ArrayList<Sentencia>()
        var sentencia = esSentencia()

       while (sentencia != null) {

            listaSentencias.add(sentencia)
            obtenerSiguienteToken()
            sentencia=esSentencia()
        }

        if (listaSentencias.size > 0){
            return listaSentencias
        }

        return null
    }

    /**
     * <Sentencia> ::= <Decision> | <DeclaracionVariables> | <Asignacion> |<CicloMientras> | <Retorno> | <Impresion>
     *  | <Lectura> | <InvocacionFuncion> | <Incremento> | <Decremento> | <DeclaracionArreglo> | <HacerMientras> | <Detener>
     */
    fun esSentencia(): Sentencia?{
        var sentencia:Sentencia? = esDecision()
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

    /**
     * si "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!" [<DeLoContrario>]
     */
    fun esDecision(): Decision?{

        var sentenciaDesision=ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "si"){
            sentenciaDesision.add(""+tokenActual)
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                sentenciaDesision.add(""+tokenActual)
                obtenerSiguienteToken()
                var expresionLogica=esExpresionLogica()
                println("EXPRESION: " + expresionLogica)
                if(expresionLogica!=null){
                    sentenciaDesision.add(""+expresionLogica)
                    if(tokenActual.categoria==Categoria.INTERROGACIONDER){
                        sentenciaDesision.add(""+tokenActual)
                        obtenerSiguienteToken()
                        if (tokenActual.categoria==Categoria.ADMIRACIONIZQ){
                            obtenerSiguienteToken()
                            var listaSentencias=esListaSentencias()
                            if (listaSentencias!=null){
                                sentenciaDesision.add(""+listaSentencias)
                                obtenerSiguienteToken()
                                if(tokenActual.categoria==Categoria.ADMIRACIONDER){
                                    sentenciaDesision.add(""+tokenActual)
                                }
                                var deLoContrario = esDeLoContrario()
                                if(deLoContrario!=null){
                                    sentenciaDesision.add(""+deLoContrario)
                                    obtenerSiguienteToken()
                                    return Decision(sentenciaDesision)

                                }else{
                                    return Decision(sentenciaDesision)
                                }
                            }

                        }
                    }
                }
            }

        }
        return null
    }

    /**
     * <DeclaracionVariable> ::= <Variable> <TipoDato> identificador operadorAsignacion <Expresion> "/"
     */
    fun esDeclaracionVariable(): DeclaracionVariable?{

        var declaracionVariable = ArrayList<String>()
        var tipoVariable = esVariable()

        if (tipoVariable != null) {
            declaracionVariable.add("" + tipoVariable)
            obtenerSiguienteToken()
            var tipoDeDato = esTipoDeDato()
            if (tipoDeDato != null){
                declaracionVariable.add("" + tipoDeDato)
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                    declaracionVariable.add("" + tokenActual)
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                        declaracionVariable.add("" + tokenActual)
                        var expresion = esExpresion()
                        if (expresion != null){
                            declaracionVariable.add("" + expresion)
                            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                declaracionVariable.add("" + tokenActual)
                                return DeclaracionVariable(declaracionVariable)
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * <Asignacion> ::= identificador operadorAsignacion <Expresion> "/"
     */

    fun esAsignacion(): Asignacion?{
        var asignacion = ArrayList<String>()

        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            asignacion.add("" + tokenActual)
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                asignacion.add("" + tokenActual)
                var expresion = esExpresion()
                if (expresion != null){
                    asignacion.add("" + expresion)
                    if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                        asignacion.add("" + tokenActual)
                        return Asignacion(asignacion)
                    }
                }
            }
        }
        return null
    }

    /**
     * <CicloMientras> ::= mientras "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!"
     */
    fun esCicloMientras(): CicloMientras?{
        var cicloMientras = ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "mientras"){
            cicloMientras.add("" + tokenActual)
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                cicloMientras.add("" + tokenActual)
                var expresion = esExpresionLogica()
                if (expresion != null){
                    cicloMientras.add("" + expresion)
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                        cicloMientras.add("" + tokenActual)
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.ADMIRACIONIZQ){
                            cicloMientras.add("" + tokenActual)
                            var sentencias = esListaSentencias()
                            if (sentencias != null){
                                cicloMientras.add("" + tokenActual)
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.ADMIRACIONDER){
                                    cicloMientras.add("" + tokenActual)
                                    return CicloMientras(cicloMientras)
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * <Retorno> ::= retornar <Expresion> "/"
     */
    fun esRetorno(): Retorno?{
        var retorno = ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "retornar"){
            retorno.add("" + tokenActual)
            obtenerSiguienteToken()
            var expresion = esExpresion()
            if (expresion != null){
                retorno.add("" + expresion)
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    retorno.add("" + tokenActual)
                    return Retorno(retorno)
                }
            }
        }
        return null
    }

    /**
     * <Impresion> ::= imprimir <Expresion> "/"
     */
    fun esImpresion(): Impresion?{
        var imprimir = ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "imprimir"){
            imprimir.add("" + tokenActual)
            obtenerSiguienteToken()
            var expresion = esExpresion()
            if (expresion != null){
                imprimir.add("" + expresion)
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    imprimir.add("" + tokenActual)
                    return Impresion(imprimir)
                }
            }
        }
        return null
    }

    /**
     * <Lectura> ::= leer <Expresion> "/"
     */
    fun esLectura(): Lectura?{
        var leer = ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "leer"){
            leer.add("" + tokenActual)
            obtenerSiguienteToken()
            var expresion = esExpresion()
            if (expresion != null){
                leer.add("" + expresion)
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    leer.add("" + tokenActual)
                    return Lectura(leer)
                }
            }
        }
        return null
    }

    /**
     * <InvocacionFuncion> ::= identificador operadorAsignacion identificador "¿" [<ListaArgumentos>] "?" "/"
     * | identificador "¿" [<ListaArgumentos>] "?" "/"
     */
    fun esInvocacionFuncion(): InvocacionFuncion?{
        var invocacionFuncion = ArrayList<String>()

        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            invocacionFuncion.add("" + tokenActual)
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                invocacionFuncion.add("" + tokenActual)
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                    invocacionFuncion.add("" + tokenActual)
                    obtenerSiguienteToken()
                    if(tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                        invocacionFuncion.add("" + tokenActual)
                        obtenerSiguienteToken()
                        var argumentos = esListaArgumentos()
                        if(argumentos != null){
                            invocacionFuncion.add("" + argumentos)
                            obtenerSiguienteToken()
                            if(tokenActual.categoria == Categoria.INTERROGACIONDER){
                                invocacionFuncion.add("" + tokenActual)
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                    invocacionFuncion.add("" + tokenActual)
                                    return InvocacionFuncion(invocacionFuncion)
                                }
                            }
                        }
                    }
                }
            }else{
                if(tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                    invocacionFuncion.add("" + tokenActual)
                    obtenerSiguienteToken()
                    var argumentos = esListaArgumentos()
                    if(argumentos != null){
                        invocacionFuncion.add("" + argumentos)
                        obtenerSiguienteToken()
                        if(tokenActual.categoria == Categoria.INTERROGACIONDER){
                            invocacionFuncion.add("" + tokenActual)
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                invocacionFuncion.add("" + tokenActual)
                                return InvocacionFuncion(invocacionFuncion)
                            }
                        }
                    }else{
                        if(tokenActual.categoria == Categoria.INTERROGACIONDER) {
                            invocacionFuncion.add("" + tokenActual)
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                invocacionFuncion.add("" + tokenActual)
                                return InvocacionFuncion(invocacionFuncion)
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * <ListaArgumentos> ::= <Argumento> ["^" <ListaArgumentos> ]
     */
    fun esListaArgumentos(): ArrayList<Expresion>? {

        var listaArgumentos = ArrayList<Expresion>()
        var argumento = esArgumento()

        while (argumento != null) {

            listaArgumentos.add(argumento)

            if(tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                argumento = esArgumento()
            }else{
                if (tokenActual.categoria != Categoria.INTERROGACIONIZQ){

                    reportarError("Falta un separador en la lista de parametros")
                }
                break
            }

        }
        if (listaArgumentos.size > 0){
            return listaArgumentos
        }

        return null
    }

    /**
     * <Argumento> ::= <Expresion>
     */

    fun esArgumento():Expresion? {
        var expresion = esExpresion()

        if(expresion != null){
            return expresion
        }
        return null
    }

    /**
     * <DeLoContrario> ::= dlc "¡" <ListaSentencias> "!"
     */

    fun esDeLoContrario():DeLoContrario?{
        var sentenciaDeLoContrario=ArrayList<String>()

        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="dlc"){
            sentenciaDeLoContrario.add(""+tokenActual)
            obtenerSiguienteToken()
            if (tokenActual.categoria==Categoria.ADMIRACIONIZQ){
                sentenciaDeLoContrario.add(""+tokenActual)
                obtenerSiguienteToken()
                var listaSentencia=esListaSentencias()
                if (listaSentencia!=null){
                    sentenciaDeLoContrario.add(""+listaSentencia)
                    obtenerSiguienteToken()
                    if(tokenActual.categoria==Categoria.ADMIRACIONDER){
                        sentenciaDeLoContrario.add(""+tokenActual)
                        return DeLoContrario(sentenciaDeLoContrario)
                    }else{
                        return null
                    }

                }

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
     * <DeclaracionArreglos> ::= mut vnum identificador "[" "]" operadorAsignacion "[" <ListaValoresNumericos> "]" "/"
     */
    fun esDeclaracionArreglo(): DeclaracionArreglo?{

        var declaracionArreglo = ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "mut"){
            declaracionArreglo.add(""+tokenActual)
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "vnum"){
                declaracionArreglo.add(""+tokenActual)
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                    declaracionArreglo.add(""+tokenActual)
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.CORCHETEIZQ){
                        declaracionArreglo.add(""+tokenActual)
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.CORCHETEDER){
                            declaracionArreglo.add(""+tokenActual)
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                                declaracionArreglo.add(""+tokenActual)
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.CORCHETEIZQ){
                                    declaracionArreglo.add(""+tokenActual)
                                    obtenerSiguienteToken()
                                    var listaValoresNumericos = esListaValoresNumericos()
                                    if (listaValoresNumericos != null){
                                        declaracionArreglo.add(""+listaValoresNumericos)
                                        obtenerSiguienteToken()
                                        if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                            declaracionArreglo.add(""+tokenActual)
                                            return DeclaracionArreglo(declaracionArreglo)
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return null
    }
    /**
     * <ListaValoresNumericos> ::= <ValorNumerico> ["^"<ValorNumerico>]
     */
    fun esListaValoresNumericos(): ArrayList<ValorNumerico>? {

        var listaValoresNumericos = ArrayList<ValorNumerico>()
        var valorNumerico = esValorNumerico()

        while (valorNumerico != null) {

            listaValoresNumericos.add(valorNumerico)

            if(tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                valorNumerico = esValorNumerico()
            }else{
                if (tokenActual.categoria != Categoria.INTERROGACIONIZQ){

                    reportarError("Falta un separador en la lista de parametros")
                }
                break
            }

        }
        if (listaValoresNumericos.size > 0){
            return listaValoresNumericos
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
     * <HacerMientras> ::= hacer "¡" <ListaSentencias> "!" mientras "¿" <ExpresionLogica> "?" "/"
     */
    fun esHacerMientras(): HacerMientras?{

        var hacerMientras = ArrayList<String>()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "hacer"){
            hacerMientras.add(""+tokenActual)
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.ADMIRACIONIZQ){
                hacerMientras.add(""+tokenActual)
                obtenerSiguienteToken()
                var sentencias = esListaSentencias()
                if (sentencias != null){
                    hacerMientras.add(""+sentencias)
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.ADMIRACIONDER){
                        hacerMientras.add(""+tokenActual)
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "mientras"){
                            hacerMientras.add(""+tokenActual)
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                                hacerMientras.add(""+tokenActual)
                                obtenerSiguienteToken()
                                var expresion = esExpresionLogica()
                                if (expresion != null){
                                    hacerMientras.add(""+expresion)
                                    obtenerSiguienteToken()
                                    if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                                        hacerMientras.add(""+tokenActual)
                                        obtenerSiguienteToken()
                                        if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                            hacerMientras.add(""+tokenActual)
                                            return HacerMientras(hacerMientras)
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * <Variable> ::= mut | inm
     */
    fun esVariable(): Token? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && (tokenActual.lexema == "mut" || tokenActual.lexema == "inm")) {
            return tokenActual
        }

        return null
    }

    /**
     *<Concatenador> ::= @+
     */
    fun esConcatenador(): Token? {
        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual.lexema == "@+") {
            return tokenActual
        }

        return null
    }

    /**
     *<Signo> ::= @+ | @-
     */
    fun esSigno(): Token? {
        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual.lexema == "@+" || tokenActual.lexema == "@-")) {
            return tokenActual
        }

        return null
    }


    fun reportarError(mensaje:String){
        listaErrores.add( Error(mensaje, tokenActual.fila, tokenActual.columna))
    }

}