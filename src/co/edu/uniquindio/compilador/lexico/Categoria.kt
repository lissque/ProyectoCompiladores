package co.edu.uniquindio.compilador.lexico

enum class Categoria {
    PALABRA_RESERVADA, ENTERO, DECIMAL, CARACTER, CADENA, BOOLEAN, IDENTIFICADOR, OPERADOR_ARITMETICO, OPERADOR_LOGICO,
    OPERADOR_RELACIONAL, OPERADOR_ASIGNACION, OPERADOR_INCREMENTO, OPERADOR_DECREMENTO, AGRUPADORES,
    FIN_DE_SENTENCIA, PUNTO, DOS_PUNTOS, SEPARADOR, COMENTARIO_LINEA, COMENTARIO_BLOQUE, DESCONOCIDO
}