package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class PrintSln extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("LOAD 3 ; printSln carga en el tope de la pila el parametro a imprimir");
        SymbolTable.instructions.add("SPRINT ; printSln imprime");
        SymbolTable.instructions.add("PRNLN ; printSln salta de linea");
    }
}
