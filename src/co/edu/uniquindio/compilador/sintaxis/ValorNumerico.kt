package co.edu.uniquindio.compilador.sintaxis

import co.edu.uniquindio.compilador.lexico.Token
import javafx.scene.control.TreeItem

class ValorNumerico(var signo: Token?, var numero:Token) {

    fun getArbolVisual():TreeItem<String> {
        var raiz = TreeItem("Valor numerico")
        if (signo!=null){
            raiz.children.add(TreeItem("Signo: ${signo!!.lexema}"))
        } else {
            raiz.children.add(TreeItem("Signo: @+"))
        }
        raiz.children.add(TreeItem("Numero: ${numero.lexema}"))
        return raiz
    }

    override fun toString(): String {
        return "ValorNumerico(signo=$signo, numero=$numero)"
    }
}