package co.edu.uniquindio.compilador.controladores

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TextArea

class InicioController {

    @FXML
    lateinit var codigoFuente : TextArea

    @FXML
    fun analizarCodigo(e : ActionEvent){
        if (codigoFuente.length>0){
            val lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()
            print(lexico.listaTokens)
        }
    }

}