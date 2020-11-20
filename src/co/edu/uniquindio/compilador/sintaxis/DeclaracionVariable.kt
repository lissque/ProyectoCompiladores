package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class DeclaracionVariable(var tipoVariable: Token?, var tipoDato: Token?, var identificador: Token, var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Declaracion de Variable")

        raiz.children.add(TreeItem("Tipo de variable: ${tipoVariable!!.lexema}"))
        raiz.children.add(TreeItem("Tipo de dato: ${tipoDato!!.lexema}"))
        raiz.children.add(TreeItem("Identificador: ${identificador.lexema}"))
        raiz.children.add(expresion.getArbolVisual())

        return raiz
    }

    override fun toString(): String {
        return "DeclaracionVariable(tipoVariable=$tipoVariable, tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }
}