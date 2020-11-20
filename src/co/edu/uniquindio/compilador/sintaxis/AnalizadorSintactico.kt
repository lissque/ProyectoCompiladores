package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Categoria
import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token

class AnalizadorSintactico(var listaTokens:ArrayList<Token>) {

    var posicionActual = 0
    var tokenActual = listaTokens[0]
    var listaErrores = ArrayList<Error>()

    fun obtenerSiguienteToken() {

        posicionActual++

        if (posicionActual < listaTokens.size) {
            tokenActual = listaTokens[posicionActual]
        }
    }

    /**
     * <UnidadDeCompilacion> ::= Inicio ";" [<DeclaracionVariable>] [<ListaFunciones>] Fin
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "Inicio") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSiguienteToken()
                var listaSentencia: ArrayList<Sentencia>? = esListaSentencias()
                var listaDeclaracionVariable = ArrayList<DeclaracionVariable>()
                var listaFunciones: ArrayList<Funcion> = esListaFunciones()
                if (listaSentencia != null) {
                    for (l in listaSentencia) {
                        if (l is DeclaracionVariable) {
                            listaDeclaracionVariable.add(l)
                        } else {
                            reportarError("No es una declaracion de variable! ")
                        }
                    }
                }
                if (listaFunciones.size > 0 && listaDeclaracionVariable.size > 0) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "Fin") {
                        return UnidadDeCompilacion(listaDeclaracionVariable, listaFunciones)
                    } else {
                        reportarError("Falta la palabra reservada Fin al final")
                    }
                } else if (listaDeclaracionVariable.size > 0) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "Fin") {
                        return UnidadDeCompilacion(listaDeclaracionVariable, null)
                    }else{
                        reportarError("Falta la palabra reservada Fin al final")
                    }
                } else if (listaFunciones.size > 0) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "Fin") {
                        return UnidadDeCompilacion(null, listaFunciones)
                    }else{
                        reportarError("Falta la palabra reservada Fin al final")
                    }
                }

            }
        } else {
            reportarError("Debe iniciar con la palabra reservada Inicio")
        }
        return null
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
    fun esFuncion(): Funcion? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "fun") {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombreFuncion = tokenActual
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.INTERROGACIONIZQ) {
                    obtenerSiguienteToken()

                    var listaParametros = null

                    if (tokenActual.categoria == Categoria.INTERROGACIONDER) {
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                            obtenerSiguienteToken()

                            var tipoRetorno = esTipoRetorno()

                            if (tipoRetorno != null) {
                                obtenerSiguienteToken()

                                if (tokenActual.categoria == Categoria.ADMIRACIONIZQ) {
                                    obtenerSiguienteToken()

                                    var listaSentencias = esListaSentencias()

                                    if (listaSentencias != null) {
                                        obtenerSiguienteToken()

                                        if (tokenActual.categoria == Categoria.ADMIRACIONDER) {
                                            return Funcion(nombreFuncion, tipoRetorno, listaParametros, listaSentencias)
                                        } else {
                                            reportarError("Falta signo de admiración derecho")
                                        }
                                    } else {
                                        reportarError("La lista de sentencias esta vacia")
                                    }
                                } else {
                                    reportarError("Falta signo de admiración izquierdo")
                                }
                            } else {
                                reportarError("Falta el tipo de retorno")
                            }
                        } else {
                            reportarError("Falta el punto y coma")
                        }
                    } else {
                        reportarError("Falta signo de interrogación derecho")
                    }
                } else {
                    reportarError("Falta signo de interrogación izquierdo")
                }
            } else {
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

            if (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                parametro = esParametro()
            } else {
                if (tokenActual.categoria != Categoria.INTERROGACIONIZQ) {

                    reportarError("Falta un separador en la lista de parametros")
                }
                break
            }

        }
        if (listaParametros.size > 0) {
            return listaParametros
        }

        return null
    }

    /**
     * <Parametro> ::= <TipoDato> identificador
     */
    fun esParametro(): Parametro? {
        var tipoDeDato = esTipoDeDato()

        if (tipoDeDato != null) {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombreParametro = tokenActual
                return Parametro(nombreParametro, tipoDeDato)
            }
        }
        return null
    }


    /**
     * <TipoDato> ::= vnum | vcd | v
     */
    fun esTipoDeDato(): Token? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {

            if (tokenActual.lexema == "vnum" || tokenActual.lexema == "vcd" || tokenActual.lexema == "v") {
                return tokenActual
            }
        }

        return null
    }

    /**
     * <TipoRetorno> ::= <TipoDato> | noRetorno
     */
    fun esTipoRetorno(): Token? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            var tipoDeDato = esTipoDeDato()

            if (tipoDeDato != null || tokenActual.lexema == "noRetorno") {
                return tokenActual
            }
        }
        return null
    }

    /**
     * <Expresion> ::= <ExpresionLogica> | <ExpresionAritmetica> | <ExpresionRelacional> | <ExpresionCadena>
     *
     * */
    fun esExpresion(): Expresion? {

        var expresion: Expresion? = esExpresionAritmetica()
        if (expresion != null) {
            return expresion
        }
        expresion = esExpresionLogica()
        if (expresion != null) {
            return expresion
        }
        expresion = esExpresionRelacional()
        if (expresion != null) {
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
        var pos = posicionActual
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
                    }
                    else {

                        return ExpresionAritmetica(eA)
                    }
                }
            }
        } else {
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
        hacerBacktracking(pos)
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
        var pos = posicionActual
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
        } else {
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
        hacerBacktracking(pos)
        return null
    }

    /**
     * <ExpresionLogica> ::= <ExpresionLogica> operadorLogico <ExpresionLogica> | "¿"<ExpresionLogica>"?" | <ExpresionRelacional>
     *
     *  <ExpresionLogica> ::= "¿"<ExpresionLogica>"?" [operadorLogica <ExpresionLogica>] |
    <ExpresionRelacional> [operadorLogica <ExpresionLogica>]
     */
    fun esExpresionLogica(): ExpresionLogica? {
        var pos = posicionActual
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
        hacerBacktracking(pos)
        return null
    }

    /**
     *<ExpresionCadena> ::= cadena [<Concatenador> <Expresion> ] | <Expresion> <Concatenador> cadena
     *
     */

    fun esExpresionCadena(): ExpresionCadena? {
        var pos = posicionActual
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
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Decision> ::= si "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!" [<DeLoContrario>]
     */

    private fun hacerBacktracking(posInicial: Int) {
        //posicionActual = posInicial
        //tokenActual = listaTokens[posicionActual]

        posicionActual = posInicial
        tokenActual = if (posInicial < listaTokens.size) {
            listaTokens[posInicial]
        } else {
            Token("", Categoria.DESCONOCIDO, 0, 0)
        }
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
            sentencia = esSentencia()
        }

        if (listaSentencias.size > 0) {
            return listaSentencias
        }

        return null
    }

    /**
     * <Sentencia> ::= <Decision> | <DeclaracionVariables> | <Asignacion> |<CicloMientras> | <Retorno> | <Impresion>
     *  | <Lectura> | <InvocacionFuncion> | <Incremento> | <Decremento> | <DeclaracionArreglo> | <HacerMientras> | <Detener>
     */
    fun esSentencia(): Sentencia? {

        var sentencia: Sentencia? = esDecision()
        if (sentencia != null) {
            return sentencia
        }

        sentencia = esIncremento()
        if (sentencia != null) {
            return sentencia
        }
        sentencia = esDeclaracionVariable()
        if (sentencia != null) {
            return sentencia
        }
        sentencia = esAsignacion()
        if (sentencia != null) {
            return sentencia
        }

        sentencia = esCicloMientras()
        if (sentencia != null) {
            return sentencia
        }

        sentencia = esRetorno()
        if (sentencia != null) {
            return sentencia
        }
        sentencia = esImpresion()
        if (sentencia != null) {
            return sentencia
        }
        sentencia = esLectura()
        if (sentencia != null) {
            return sentencia
        }
        //No funciona muy bien
        sentencia = esInvocacionFuncion()
        if (sentencia != null) {
            return sentencia
        }

        sentencia = esDecremento()
        if (sentencia != null) {
            return sentencia
        }
        //Falta arreglar
        sentencia = esDeclaracionArreglo()
        if (sentencia != null) {
            return sentencia
        }
        //Falta arreglar
        sentencia = esHacerMientras()
        if (sentencia != null) {
            return sentencia
        }
        sentencia = esDetener()
        return sentencia
    }

    /**
     * si "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!" [<DeLoContrario>]
     */
    fun esDecision(): Decision? {
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "si") {

            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.INTERROGACIONIZQ) {

                obtenerSiguienteToken()
                var expresionLogica = esExpresionLogica()

                if (expresionLogica != null) {

                    if (tokenActual.categoria == Categoria.INTERROGACIONDER) {

                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.ADMIRACIONIZQ) {
                            obtenerSiguienteToken()
                            var listaSentencias = esListaSentencias()
                            if (listaSentencias != null) {

                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.ADMIRACIONDER) {
                                    obtenerSiguienteToken()
                                    var deLoContrario = esDeLoContrario()
                                    if (deLoContrario != null) {
                                        return Decision(expresionLogica, listaSentencias, deLoContrario)

                                    } else {
                                        return Decision(expresionLogica, listaSentencias, deLoContrario)
                                    }
                                }else{
                                    reportarError("Falta signo de admiracion derecho")
                                }
                            }else{
                                reportarError("No hay lista de sentencias")
                            }
                        }else{
                            reportarError("Falta signo de admiracion izquierdo")
                        }
                    }else{
                        reportarError("Falta signo de interrogacion derecho")
                    }
                }else{
                    reportarError("No hay una expresion logica")
                }
            }else{
                reportarError("Falta signo de interrogacion izquierdo")
            }

        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <DeclaracionVariable> ::= <Variable> <TipoDato> identificador operadorAsignacion <Expresion> "/"
     */
    fun esDeclaracionVariable(): DeclaracionVariable? {
        var pos = posicionActual

        var tipoVariable = esVariable()

        if (tipoVariable != null) {
            obtenerSiguienteToken()
            var tipoDeDato = esTipoDeDato()
            if (tipoDeDato != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var identificador = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                        obtenerSiguienteToken()
                        var expresion = esExpresion()
                        if (expresion != null) {
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA) {
                                return DeclaracionVariable(tipoVariable, tipoDeDato, identificador, expresion)
                            }
                            else{
                                reportarError("Falta fin de sentencia")
                            }
                        }
                        else{
                            reportarError("No hay expresion de asignacion")
                        }
                    }
                    else{
                        reportarError("Falta operador de asignacion")
                    }
                }
                else{
                    reportarError("Falta identificador")
                }
            }
            else{
                reportarError("Falta tipo de dato")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Asignacion> ::= identificador operadorAsignacion <Expresion> "/"
     */

    fun esAsignacion(): Asignacion?{
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            var identificador = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                obtenerSiguienteToken()
                var expresion = esExpresion()
                if (expresion != null){
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                        return Asignacion(identificador,expresion)
                    }else{
                        reportarError("Falta fin de sentencia")
                    }
                }else{
                    reportarError("No hay una expresion")
                }
            }else{
                reportarError("Falta operador de asignacion")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <CicloMientras> ::= mientras "¿" <ExpresionLogica> "?" "¡" <ListaSentencias> "!"
     */
    fun esCicloMientras(): CicloMientras?{
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "mientras"){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                obtenerSiguienteToken()
                var expresion = esExpresionLogica()
                if (expresion != null){

                    if (tokenActual.categoria == Categoria.INTERROGACIONDER){
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.ADMIRACIONIZQ){
                            obtenerSiguienteToken()
                            var sentencias = esListaSentencias()
                            if (sentencias != null){
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.ADMIRACIONDER){
                                    return CicloMientras(expresion, sentencias)
                                }else{
                                    reportarError("Falta signo de admiracion derecho")
                                }
                            }else{
                                reportarError("No hay lista de sentencias")
                            }
                        }else{
                            reportarError("Falta signo de admiracion izquierdo")
                        }
                    }else{
                        reportarError("Falta signo de interrogacion derecho")
                    }
                }else{
                    reportarError("No hay una expresion logica")
                }
            }else{
                reportarError("Falta signo de interrogacion izquierdo")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Retorno> ::= retornar <Expresion> "/"
     */
    fun esRetorno(): Retorno?{
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "retornar"){
            obtenerSiguienteToken()
            var expresion = esExpresion()
            if (expresion != null){
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Retorno(expresion)
                }else{
                    reportarError("Falta fin de sentencia")
                }
            }else{
                reportarError("No hay ninguna expresion")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Impresion> ::= imprimir <Expresion> "/"
     */
    fun esImpresion(): Impresion?{
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "imprimir"){
            obtenerSiguienteToken()
            var expresion = esExpresion()
            if (expresion != null){
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Impresion(expresion)
                }else{
                    reportarError("Falta fin de sentencia")
                }
            }else{
                reportarError("No hay ninguna expresion")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Lectura> ::= leer <Expresion> "/"
     */
    fun esLectura(): Lectura?{
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "leer"){
            obtenerSiguienteToken()
            var expresion = esExpresion()
            if (expresion != null){
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Lectura(expresion)
                }else{
                    reportarError("Falta fin de sentencia")
                }
            }else{
                reportarError("No hay ninguna expresion")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <InvocacionFuncion> ::= identificador operadorAsignacion identificador "¿" [<ListaArgumentos>] "?" "/"
     * | identificador "¿" [<ListaArgumentos>] "?" "/"
     */
    fun esInvocacionFuncion(): InvocacionFuncion?{
        var pos = posicionActual

        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            var identificadorVariable = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                    var identificadorFuncion = tokenActual
                    obtenerSiguienteToken()
                    if(tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                        obtenerSiguienteToken()
                        var argumentos = esListaArgumentos()
                        if(argumentos != null){
                            obtenerSiguienteToken()
                            if(tokenActual.categoria == Categoria.INTERROGACIONDER){
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                    return InvocacionFuncion(identificadorVariable,identificadorVariable,argumentos)
                                }else{
                                    reportarError("Falta fin de sentencia")
                                }
                            }else{
                                reportarError("Falta signo de interrogacion derecho")
                            }
                        }else{
                            if(tokenActual.categoria == Categoria.INTERROGACIONDER){
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                    return InvocacionFuncion(identificadorVariable,identificadorVariable,argumentos)
                                }else{
                                    reportarError("Falta fin de sentencia")
                                }
                            }else{
                                reportarError("Falta signo de interrogacion derecho")
                            }
                        }
                    }else{
                        reportarError("Falta signo de interrogacion izquierdo")
                    }
                }
            }else{
                if(tokenActual.categoria == Categoria.INTERROGACIONIZQ){
                    println("ANTES: "+tokenActual)
                    obtenerSiguienteToken()
                    println("DESPUES: "+tokenActual)
                    var argumentos = esListaArgumentos()
                    if(argumentos != null){
                        obtenerSiguienteToken()
                        if(tokenActual.categoria == Categoria.INTERROGACIONDER){
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                return InvocacionFuncion(identificadorVariable,argumentos)
                            }else{
                                reportarError("Falta fin de sentencia")
                            }
                        }else{
                            reportarError("Falta signo de interrogacion derecho")
                        }
                    }else{
                        if(tokenActual.categoria == Categoria.INTERROGACIONDER) {
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                                return InvocacionFuncion(identificadorVariable,argumentos)
                            }else{
                                reportarError("Falta fin de sentencia")
                            }
                        }else{
                            reportarError("Falta signo de interrogacion derecho")
                        }
                    }
                }else{
                    reportarError("Falta signo de interrogacion izquierdo")
                }
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <ListaArgumentos> ::= <Argumento> ["^" <ListaArgumentos> ]
     */
    fun esListaArgumentos(): ArrayList<Expresion>? {

        var pos = posicionActual
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

        hacerBacktracking(pos)
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

        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA&&tokenActual.lexema=="dlc"){
            obtenerSiguienteToken()
            if (tokenActual.categoria==Categoria.ADMIRACIONIZQ){
                obtenerSiguienteToken()
                var listaSentencia=esListaSentencias()
                if (listaSentencia!=null){
                    obtenerSiguienteToken()
                    if(tokenActual.categoria==Categoria.ADMIRACIONDER){
                        return DeLoContrario(listaSentencia)
                    }else{
                        reportarError("Falta signo de admiracion derecho")
                    }
                }else{
                    reportarError("No hay niguna sentecia")
                }
            }else{
                reportarError("Falta signo de admiracion izquierdo")
            }
        }

        return null
    }


    /**
     * <Incremento> ::= identificador operadorIncremento "/"
     */
    fun esIncremento(): Incremento?{
        var pos = posicionActual
        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            var nombreVariable = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.OPERADOR_INCREMENTO){
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Incremento(nombreVariable)
                }else{
                    reportarError("Falta fin de sentencia")
                }
            }else{
                reportarError("Falta operador de inremento")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Decremento> ::= identificador operadorDecremento "/"
     */
    fun esDecremento(): Decremento?{
        var pos = posicionActual
        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            var nombreVariable = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.OPERADOR_DECREMENTO){
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                    return Decremento(nombreVariable)
                }else{
                    reportarError("Falta fin de sentencia")
                }
            }else{
                reportarError("Falta operador de decremento")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     *<Detener> ::= detener "/"
     */
    fun esDetener(): Detener?{
        var pos = posicionActual
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "detener"){
            var sentenciaDetener = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.FIN_DE_SENTENCIA){
                return Detener(sentenciaDetener)
            }else{
                reportarError("Falta fin de sentencia")
            }
        }
        hacerBacktracking(pos)
        return null
    }

    /**
     * <DeclaracionArreglos> ::= mut vnum identificador "[" "]" operadorAsignacion "[" <ListaValoresNumericos> "]" "/"
     */
    fun esDeclaracionArreglo(): DeclaracionArreglo?{
        var pos = posicionActual
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
        hacerBacktracking(pos)
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
        var pos = posicionActual
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
        hacerBacktracking(pos)
        return null
    }

    /**
     * <Variable> ::= mut | inm
     */
    fun esVariable(): Token? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && ((tokenActual.lexema == "mut") || tokenActual.lexema == "inm")) {
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