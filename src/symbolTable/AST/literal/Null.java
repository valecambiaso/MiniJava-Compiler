package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.Tipo;
import symbolTable.TipoNull;

public class Null extends NodoOperando {
    private Token token;

    public Null(Token token) {
        this.token = token;
    }

    @Override
    public Tipo check() throws SemanticException {
        return new TipoNull();
    }
}
