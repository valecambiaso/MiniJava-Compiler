package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class PrintCln extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; printCln carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("CPRINT ; printCln imprime");
        SymbolTable.instructions.add("PRNLN ; printCln salta de linea");
    }
}
