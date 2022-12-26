package symbolTable.AST.sentencia.nodoBloquePredefinido;

import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.SymbolTable;

public class Read extends NodoBloque {
    public void generate(){
        SymbolTable.instructions.add("READ ; read lee un entero");
        SymbolTable.instructions.add("PUSH 48 ; hay que restar 48 por el char");
        SymbolTable.instructions.add("SUB ; se resta 48 a lo que se leyo para obtener el digito entero");
        SymbolTable.instructions.add("STORE 3 ; read guarda el tope de la pila en la locacion reservada para el retorno");
    }
}
