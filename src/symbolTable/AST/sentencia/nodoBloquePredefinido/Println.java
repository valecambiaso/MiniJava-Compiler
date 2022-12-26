package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class Println extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("PRNLN ; println salta de linea");
    }
}
