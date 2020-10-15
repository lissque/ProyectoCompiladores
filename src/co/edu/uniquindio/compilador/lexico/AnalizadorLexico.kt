package co.edu.uniquindio.compilador.lexico

class AnalizadorLexico (var codigoFuente:String) {

    var caracterActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var posicionActual = 0;
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0

    fun almacenarToken(lexema:String, categoria: Categoria, fila:Int, columna:Int) = listaTokens.add(Token(lexema, categoria, fila, columna))

    fun analizar(){

        while (caracterActual != finCodigo){
            println(caracterActual)
            if (caracterActual==' '||caracterActual=='\t'||caracterActual=='\n'){
                obtenerSiguienteCaracter()
                continue
            }
            if (esIdentificador()) continue
            //if (esPalabraReservada()) continue
            if (esEntero()) continue
            if (esDecimal()) continue
            if (esOperadorAritmetico()) continue
            if (esCaracter()) continue



            almacenarToken(""+caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()

        }
    }

    fun esIdentificador():Boolean{
        if (caracterActual == '!'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            var cont = 0
            lexema+= caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual.isLetter() || caracterActual=='!' || caracterActual.isDigit()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                cont++

                if (lexema[lexema.length-1] == '!' && cont <= 11){
                    lexema = lexema.substring(1,lexema.length-1)
                    almacenarToken(lexema, Categoria.IDENTIFICADOR,filaIncial,columnaInicial)
                    return true
                }

            }
            if (cont > 11){
                hacerBT(posicionInicial,filaIncial,columnaInicial)
                return false
            }


        }

        return false
    }

    fun esPalabraReservada(){

    }


    fun esEntero():Boolean{
        if (caracterActual.isDigit()){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema+= caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isDigit()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual=='.') {
                hacerBT(posicionInicial,filaIncial,columnaInicial)
                return false
            }
            almacenarToken(lexema, Categoria.ENTERO,filaIncial,columnaInicial)
            return true
        }
        return false
    }


    fun esDecimal():Boolean{
        if (caracterActual=='.'||caracterActual.isDigit()){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            if (caracterActual=='.'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual.isDigit()){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
            }
            else{
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                while (caracterActual.isDigit())
                {
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
                if (caracterActual=='.'){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
            }
            while (caracterActual.isDigit()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.DECIMAL, filaIncial, columnaInicial)
            return true
        }
        return false
    }


    fun esOperadorAritmetico():Boolean{
        if (caracterActual == '@'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema+= caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/'){
                lexema+= caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO,filaIncial,columnaInicial)
                return true
            }
            return false
        }
        return false
    }


    fun esOperadorAsignacion():Boolean{
        if (caracterActual == ':'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema+= caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '-'){
                lexema+= caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION,filaIncial,columnaInicial)
                return true
            }
        }
        return false
    }


    fun esCaracter():Boolean{
        if (caracterActual.isLetter()){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            lexema+= caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == 'c'){
                almacenarToken(lexema, Categoria.CARACTER,filaIncial,columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial,filaIncial,columnaInicial)
                return false
            }
        }
        return false
    }


    fun hacerBT(posicionInicial:Int, filaInicial:Int, columnaInicial:Int){
        posicionActual=posicionInicial
        filaActual=filaInicial
        columnaActual=columnaInicial
        caracterActual=codigoFuente[posicionActual]
    }


    fun obtenerSiguienteCaracter(){
        if (posicionActual==codigoFuente.length-1){
            caracterActual=finCodigo
        }
        else{
            if (caracterActual=='\n'){
                filaActual++
                columnaActual=0
            }
            else{
                columnaActual++
            }
            posicionActual++
            caracterActual=codigoFuente[posicionActual]
        }
    }
}