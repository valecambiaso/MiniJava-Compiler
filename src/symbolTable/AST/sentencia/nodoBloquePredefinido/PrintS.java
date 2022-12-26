package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class PrintS extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; printS carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("SPRINT ; printS imprime");
    }
}
