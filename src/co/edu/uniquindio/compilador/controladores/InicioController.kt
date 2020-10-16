package co.edu.uniquindio.compilador.controladores

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.observable.TokenObservable
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.cell.PropertyValueFactory

class InicioController {

    @FXML
    lateinit var codigoFuente : TextArea
    @FXML
    lateinit var tablaTokens : TableView<TokenObservable>
    @FXML
    lateinit var columLexema : TableColumn<TokenObservable,String>
    @FXML
    lateinit var columCategoria : TableColumn<TokenObservable,String>
    @FXML
    lateinit var columFila : TableColumn<TokenObservable,String>
    @FXML
    lateinit var columColumna : TableColumn<TokenObservable,String>

    @FXML
    fun analizarCodigo(e : ActionEvent){
        if (codigoFuente.length>0){
            iniciarTablaTokens()
            val lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()
            llenarTabla(lexico)
            print(lexico.listaTokens)
        }
    }

    private fun iniciarTablaTokens(){
        columLexema.cellValueFactory = PropertyValueFactory<TokenObservable,String>("lexema")
        columCategoria.cellValueFactory = PropertyValueFactory<TokenObservable,String>("categoria")
        columFila.cellValueFactory = PropertyValueFactory<TokenObservable,String>("fila")
        columColumna.cellValueFactory = PropertyValueFactory<TokenObservable,String>("columna")
    }

    private fun llenarTabla(lexico : AnalizadorLexico){
        tablaTokens.items.clear()
        for (elemento in lexico.listaTokens){
            tablaTokens.items.add(TokenObservable(elemento.lexema,elemento.categoria.toString(),"".plus(elemento.fila),"".plus(elemento.columna)))
        }
        tablaTokens.refresh()
    }


}