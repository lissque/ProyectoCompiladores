package co.edu.uniquindio.compilador.sintaxis


import co.edu.uniquindio.compilador.lexico.Error
import co.edu.uniquindio.compilador.lexico.Token
import co.edu.uniquindio.compilador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem


class Asignacion(var identificador: Token,var expresion: Expresion):Sentencia() {

    override open fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Asignacion")
        raiz.children.add(TreeItem("Identificador: ${identificador.lexema}"))
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }
    /**
     * metodo
     */
        override fun toString(): String {
            return "Asignacion(identificador=$identificador, expresion=$expresion)"
        }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {7

        var s=tablaSimbolos.buscarSimboloVariable(identificador.lexema,ambito)
        if(s==null){
            listaErrores.add(Error("el campo ${identificador.lexema} no existe dentro del ambito $ambito",identificador.fila,identificador.columna))
        }else{
            var tipo = s.tipo
            if (expresion!=null){
                expresion.analizarSemantica(tablaSimbolos, listaErrores, ambito)
                var tipoExp = expresion.obtenerTipo(tablaSimbolos,ambito)
                if (tipoExp!=tipo){
                    listaErrores.add(Error("el tipo de dato de la expresion ($tipoExp) no coincide con el tipo de dato del campo (${identificador.lexema}) que es $tipo",identificador.fila,identificador.columna))
                }

            }
        }

    }

    }
