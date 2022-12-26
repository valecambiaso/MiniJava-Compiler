package symbolTable.AST.expresion;

import symbolTable.SemanticException;
import symbolTable.TipoMetodo;

public abstract class NodoExpresion {
    public abstract TipoMetodo check() throws SemanticException;
    public abstract void generate();
}
