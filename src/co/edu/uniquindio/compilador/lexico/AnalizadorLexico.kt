package co.edu.uniquindio.compilador.lexico

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper

class AnalizadorLexico (var codigoFuente:String) {


    //ELIMINAR ESTE COMENTARIO, SOLO ES UNA PRUEBA, ADIÓS
    var caracterActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var posicionActual = 0;
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0
    var listaPalabrasReservadas = ArrayList<String>()


    fun almacenarToken(lexema: String, categoria: Categoria, fila: Int, columna: Int) = listaTokens.add(Token(lexema, categoria, fila, columna))

    fun analizar() {
        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSiguienteCaracter()
                continue
            }

            if (esIdentificador()) continue
            if (esPalabraReservada()) continue
            if (esEntero()) continue
            if (esDecimal()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorAsignacion()) continue
            if (esOperadorIncremento()) continue
            if (esOperadorDecremento()) continue
            if (esOperadorRelacional()) continue
            if (esOperadorLogico()) continue
            if (esAgrupacionIzquierdo()) continue
            if (esAgrupacionDerecho()) continue
            if (esFinalSentencia()) continue
            if (esPunto()) continue
            if (esDosPuntos()) continue
            if (esSeparador()) continue
            if (esComentarioLinea()) continue
            if (esComentarioBloque()) continue
            if (esCadena()) continue
            if (esCaracter()) continue
            if (esArreglo()) continue

            almacenarToken("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()

        }
    }

    fun almacenarPalabrasReservadas(){

        listaPalabrasReservadas.add("vcd")
        listaPalabrasReservadas.add("vnum")
        listaPalabrasReservadas.add("v")
        listaPalabrasReservadas.add("hacer")
        listaPalabrasReservadas.add("mientras")
        listaPalabrasReservadas.add("mut")
        listaPalabrasReservadas.add("inm")
        listaPalabrasReservadas.add("si")
        listaPalabrasReservadas.add("dlc")
        listaPalabrasReservadas.add("imprimir")
        listaPalabrasReservadas.add("leer")
        listaPalabrasReservadas.add("privado")
        listaPalabrasReservadas.add("publico")
        listaPalabrasReservadas.add("objeto")
        listaPalabrasReservadas.add("retornar")
        listaPalabrasReservadas.add("Inicio")
        listaPalabrasReservadas.add("Fin")
        listaPalabrasReservadas.add("noRetorno")
        listaPalabrasReservadas.add("fun")
        listaPalabrasReservadas.add("detener")
    }

    fun hacerBT(posicionInicial: Int, filaInicial: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = columnaInicial
        caracterActual = codigoFuente[posicionActual]
    }


    fun obtenerSiguienteCaracter() {
        if (posicionActual == codigoFuente.length - 1) {
            caracterActual = finCodigo
        } else {
            if (caracterActual == '\n') {
                filaActual++
                columnaActual = 0
            } else {
                columnaActual++
            }
            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }
    }


    fun esIdentificador(): Boolean {
        if (caracterActual == '&') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            var cont = 0
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '&'){
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }
            while (caracterActual.isLetter() || caracterActual == '&' || caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                cont++

                if (lexema[lexema.length - 1] == '&' && cont <= 11) {
                    lexema = lexema.substring(1, lexema.length - 1)
                    almacenarToken(lexema, Categoria.IDENTIFICADOR, filaIncial, columnaInicial)
                    return true
                }

            }
            hacerBT(posicionInicial, filaIncial, columnaInicial)
            return false
        }

        return false
    }

    fun esEntero(): Boolean {
        if (caracterActual.isDigit()) {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual == '.') {
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }
            almacenarToken(lexema, Categoria.NUMERICO, filaIncial, columnaInicial)
            return true
        }
        return false
    }


    fun esDecimal(): Boolean {
        if (caracterActual == '.' || caracterActual.isDigit()) {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            if (caracterActual == '.') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
            } else {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                if (caracterActual == '.') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
            }
            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.NUMERICO, filaIncial, columnaInicial)
            return true
        }
        return false
    }


    fun esOperadorAritmetico(): Boolean {
        if (caracterActual == '@') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == '+' && lexema[lexema.length - 1] == '+' ) {
                    hacerBT(posicionInicial, filaIncial, columnaInicial)
                    return false
                }
                if (caracterActual == '-' && lexema[lexema.length - 1] == '-'){
                    hacerBT(posicionInicial, filaIncial, columnaInicial)
                    return false
                }

                almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaIncial, columnaInicial)
                return true
            }
            hacerBT(posicionInicial, filaIncial, columnaInicial)
            return false
        }
        return false
    }


    fun esOperadorAsignacion(): Boolean {
        if (caracterActual == ':') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == ':') {
                    hacerBT(posicionInicial, filaIncial, columnaInicial)
                    return false
                }
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaIncial, columnaInicial)
                return true
            }
        }

        return false
    }


    fun esCaracter(): Boolean {
        if (caracterActual.isLetter()) {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '°') {
                almacenarToken(lexema, Categoria.CARACTER, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            } else {
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }
        }
        return false
    }


    fun esOperadorIncremento(): Boolean {
        if (caracterActual == '@') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '+') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == '+') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_INCREMENTO, filaIncial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
            }

            hacerBT(posicionInicial, filaIncial, columnaInicial)
            return false
        }
        return false
    }

    fun esOperadorDecremento(): Boolean {
        if (caracterActual == '@') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '-') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == '-') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_DECREMENTO, filaIncial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
            }

            hacerBT(posicionInicial, filaIncial, columnaInicial)
            return false
        }
        return false
    }

    fun esOperadorRelacional(): Boolean {
        if (caracterActual == '<') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '<') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == ':') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaIncial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
            }

            hacerBT(posicionInicial, filaIncial, columnaInicial)
            return false

        }
        if (caracterActual == '>') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '>') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == ':') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaIncial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
            }
            hacerBT(posicionInicial, filaIncial, columnaInicial)
            return false

        }

        if (caracterActual == ':') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == ':') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (caracterActual == ':') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == ':') {
                        lexema += caracterActual
                        almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaIncial, columnaInicial)
                        obtenerSiguienteCaracter()
                        return true
                    }
                    hacerBT(posicionInicial, filaIncial, columnaInicial)
                    return false
                }
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }

        }

        return false
    }

    fun esOperadorLogico(): Boolean {
        if (caracterActual == '(') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            var cont = 0
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == ')') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            hacerBT(posicionInicial,filaIncial,columnaInicial)
            return false
        }
        if (caracterActual == '¬') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            var cont = 0
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '¬') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            hacerBT(posicionInicial,filaIncial,columnaInicial)
            return false
        }
        if (caracterActual == '{') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            var cont = 0
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '}') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            hacerBT(posicionInicial,filaIncial,columnaInicial)
            return false
        }
        return false
    }

    fun esAgrupacionIzquierdo(): Boolean {
        if (caracterActual == '¿') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.AGRUPADORIZQ, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }

        if (caracterActual == '¡') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.AGRUPADORDER, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    fun esAgrupacionDerecho(): Boolean {

        if (caracterActual == '?') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.AGRUPADORDER, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }

        if (caracterActual == '!') {
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.AGRUPADORDER, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    fun esDosPuntos():Boolean{
        if (caracterActual == ';'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.DOS_PUNTOS, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    fun esPunto():Boolean{
        if (caracterActual == '*'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.PUNTO, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    fun esFinalSentencia():Boolean{
        if (caracterActual == '/'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.FIN_DE_SENTENCIA, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    fun esSeparador():Boolean{
        if (caracterActual == '^'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            almacenarToken(lexema, Categoria.SEPARADOR, filaIncial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }


    fun esComentarioLinea():Boolean{
        if (caracterActual == '|'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '|'){
                lexema += caracterActual
                almacenarToken(lexema, Categoria.COMENTARIO_LINEA, filaIncial, columnaInicial)
                filaActual += 1
                columnaInicial = 0
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }

        }
        return false
    }

    fun esComentarioBloque():Boolean{
        if (caracterActual == '#'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual != '#' && caracterActual != finCodigo){
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual == '#'){
                lexema += caracterActual
                almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }

        }
        return false
    }

    fun esCadena():Boolean{
        if (caracterActual == '_'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual != '_' && caracterActual != finCodigo){
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual == '_'){
                lexema += caracterActual
                almacenarToken(lexema, Categoria.CADENA, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }

        }
        return false
    }

    fun esPalabraReservada():Boolean{
        var palabraR = false
        almacenarPalabrasReservadas()
        var lexema = ""
        var filaIncial = filaActual
        var columnaInicial = columnaActual
        var posicionInicial = posicionActual
        for ( palabraReservada in listaPalabrasReservadas){

               for (caracterPR in palabraReservada){
                   if (caracterActual == caracterPR){
                       lexema += caracterActual
                       obtenerSiguienteCaracter()
                       palabraR = true
                   }else{
                       palabraR = false
                       hacerBT(posicionInicial, filaIncial, columnaInicial)
                       lexema = ""
                       break
                   }
               }

               if (palabraR == true){
                   almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaIncial, columnaInicial)
                   return true
               }

           }

        return false
        }

    fun esArreglo():Boolean{
        if (caracterActual == '['){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual != ']' && caracterActual != finCodigo){
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual == ']'){
                lexema += caracterActual
                almacenarToken(lexema, Categoria.ARREGLO, filaIncial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial, filaIncial, columnaInicial)
                return false
            }
        }
        return false
    }

    }

