package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class PrintI extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; printI carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("IPRINT ; printI imprime");
    }
}
