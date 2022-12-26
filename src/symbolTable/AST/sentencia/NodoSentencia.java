package symbolTable.AST.sentencia;

import symbolTable.SemanticException;

public abstract class NodoSentencia {

    public abstract void checkSentences() throws SemanticException;

    public abstract void generate();
}
