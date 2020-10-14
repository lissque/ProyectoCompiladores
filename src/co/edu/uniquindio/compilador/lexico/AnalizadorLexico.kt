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
            if (caracterActual==' '||caracterActual=='\t'||caracterActual=='\n'){
                obtenerSiguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esIdentificador()) continue
            if (esDecimal()) continue

            almacenarToken(""+caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
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

    fun hacerBT(posicionInicial:Int, filaInicial:Int, columnaInicial:Int){
        posicionActual=posicionInicial
        filaActual=filaInicial
        columnaActual=columnaInicial
        caracterActual=codigoFuente[posicionActual]
    }

    fun esIdentificador():Boolean{
        if (caracterActual.isLetter()||caracterActual=='$'||caracterActual=='_'){
            var lexema = ""
            var filaIncial = filaActual
            var columnaInicial = columnaActual
            lexema+= caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual.isLetter()||caracterActual=='$'||caracterActual=='_'||caracterActual.isDigit()){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.IDENTIFICADOR,filaIncial,columnaInicial)
            return true
        }
        return false
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