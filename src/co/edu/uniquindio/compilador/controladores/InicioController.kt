package co.edu.uniquindio.compilador.controladores

import co.edu.uniquindio.compilador.lexico.AnalizadorLexico
import co.edu.uniquindio.compilador.lexico.Categoria
import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.observable.ErrorObservable
import co.edu.uniquindio.compilador.observable.ErrorSemanticoObservable
import co.edu.uniquindio.compilador.observable.ErrorSintacticoObservable
import co.edu.uniquindio.compilador.observable.TokenObservable
import co.edu.uniquindio.compilador.semantica.AnalizadorSemantico
import co.edu.uniquindio.compilador.sintaxis.AnalizadorSintactico
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
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
    lateinit var tablaError: TableView<ErrorObservable>
    @FXML
    lateinit var colLexema: TableColumn<ErrorObservable, String>
    @FXML
    lateinit var colCategoria: TableColumn<ErrorObservable, String>
    @FXML
    lateinit var colFila: TableColumn<ErrorObservable, String>
    @FXML
    lateinit var colColumna: TableColumn<ErrorObservable, String>

    @FXML
    lateinit var tablaErrorSintactico: TableView<ErrorSintacticoObservable>
    @FXML
    lateinit var columnaMensaje: TableColumn<ErrorSintacticoObservable, String>
    @FXML
    lateinit var columnaFila: TableColumn<ErrorSintacticoObservable, String>
    @FXML
    lateinit var columnaColumna: TableColumn<ErrorSintacticoObservable, String>

    @FXML
    lateinit var tablaErrorSemantico: TableView<ErrorSemanticoObservable>
    @FXML
    lateinit var mensaje: TableColumn<ErrorSemanticoObservable, String>
    @FXML
    lateinit var filaSemantica: TableColumn<ErrorSemanticoObservable, String>
    @FXML
    lateinit var columnaSemantica: TableColumn<ErrorSemanticoObservable, String>

    @FXML
    lateinit var arbolVisual : TreeView<String>

    @FXML
    fun analizarCodigo(e : ActionEvent){
        if (codigoFuente.length>0){
            iniciarTablaTokens()
            iniciarTablaErrores()
            iniciarTablaErroresSintacticos()
            iniciarTablaErroresSemanticos()
            val lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()
            llenarTabla(lexico)
            llenarTablaError(lexico)

            if (lexico.listaErrores.isEmpty()) {
                val sintaxis = AnalizadorSintactico(lexico.listaTokens)
                val uc = sintaxis.esUnidadDeCompilacion()
                llenarTablaErrorSintactico(sintaxis)
                if (uc != null) {
                    arbolVisual.root = uc.getArbolVisual()

                    val semantica=AnalizadorSemantico(uc!!)
                    semantica.llenarTablaSimbolos()
                    print(semantica.tablaSimbolos)
                    semantica.analizarSemantica()
                    llenarTablaErrorSemantico(semantica)
                    print(semantica.listaErrores)
                }
            }
            else {
                var alerta = Alert(Alert.AlertType.ERROR)
                alerta.headerText = "Error"
                alerta.contentText ="Hay errores léxicos en el código fuente, corríjalos"
            }
        }
    }

    private fun iniciarTablaTokens(){
        columLexema.cellValueFactory = PropertyValueFactory<TokenObservable,String>("lexema")
        columCategoria.cellValueFactory = PropertyValueFactory<TokenObservable,String>("categoria")
        columFila.cellValueFactory = PropertyValueFactory<TokenObservable,String>("fila")
        columColumna.cellValueFactory = PropertyValueFactory<TokenObservable,String>("columna")
    }

    private fun iniciarTablaErrores(){
        colLexema.cellValueFactory = PropertyValueFactory<ErrorObservable,String>("lexema")
        colCategoria.cellValueFactory = PropertyValueFactory<ErrorObservable, String>("categoria")
        colFila.cellValueFactory = PropertyValueFactory<ErrorObservable,String>("fila")
        colColumna.cellValueFactory = PropertyValueFactory<ErrorObservable,String>("columna")
    }

    private fun iniciarTablaErroresSintacticos(){
        columnaMensaje.cellValueFactory = PropertyValueFactory<ErrorSintacticoObservable,String>("mensaje")
        columnaFila.cellValueFactory = PropertyValueFactory<ErrorSintacticoObservable,String>("fila")
        columnaColumna.cellValueFactory = PropertyValueFactory<ErrorSintacticoObservable,String>("columna")
    }

    private fun iniciarTablaErroresSemanticos(){
        mensaje.cellValueFactory = PropertyValueFactory<ErrorSemanticoObservable,String>("mensaje")
        filaSemantica.cellValueFactory = PropertyValueFactory<ErrorSemanticoObservable,String>("fila")
        columnaSemantica.cellValueFactory = PropertyValueFactory<ErrorSemanticoObservable,String>("columna")
    }

    private fun llenarTabla(lexico : AnalizadorLexico){
        tablaTokens.items.clear()
        for (elemento in lexico.listaTokens){
            tablaTokens.items.add(TokenObservable(elemento.lexema,elemento.categoria.toString(),"".plus(elemento.fila),"".plus(elemento.columna)))
        }
        tablaTokens.refresh()
    }

    private fun llenarTablaError(lexico : AnalizadorLexico){
        tablaError.items.clear()
        for (elemento in lexico.listaErrores){
            tablaError.items.add(ErrorObservable(elemento.lexema,elemento.categoria.toString(),"".plus(elemento.fila),"".plus(elemento.columna)))
        }
        tablaTokens.refresh()
    }

    private fun llenarTablaErrorSintactico(sintaxis : AnalizadorSintactico){
        tablaErrorSintactico.items.clear()
        for (elemento in sintaxis.listaErrores){
            tablaErrorSintactico.items.add(ErrorSintacticoObservable(elemento.error,"".plus(elemento.fila),"".plus(elemento.columna.toString())))
        }
        tablaTokens.refresh()
    }

    private fun llenarTablaErrorSemantico(semantico: AnalizadorSemantico){
        tablaErrorSemantico.items.clear()
        for (elemento in semantico.listaErrores){
            tablaErrorSemantico.items.add(ErrorSemanticoObservable(elemento.error,"".plus(elemento.fila),"".plus(elemento.columna.toString())))
        }
        tablaErrorSemantico.refresh()
    }
}