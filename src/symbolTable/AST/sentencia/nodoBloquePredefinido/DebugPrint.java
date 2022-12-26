package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class DebugPrint extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; debugPrint carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("IPRINT ; debugPrint imprime");
    }
}
