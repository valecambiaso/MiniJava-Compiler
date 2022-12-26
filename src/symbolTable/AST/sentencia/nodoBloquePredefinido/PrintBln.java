package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class PrintBln extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; printBln carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("BPRINT ; printBln imprime");
        SymbolTable.instructions.add("PRNLN ; printBln salta de linea");
    }
}
