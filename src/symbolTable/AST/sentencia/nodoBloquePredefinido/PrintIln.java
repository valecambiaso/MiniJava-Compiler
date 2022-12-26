package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class PrintIln extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; printIln carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("IPRINT ; printIln imprime");
        SymbolTable.instructions.add("PRNLN ; printIln salta de linea");
    }
}
