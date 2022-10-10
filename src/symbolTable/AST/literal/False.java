package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.Tipo;
import symbolTable.TipoBoolean;

public class False extends NodoOperando {
    private Token token;

    public False(Token token) {
        this.token = token;
    }

    @Override
    public Tipo check() throws SemanticException {
        return new TipoBoolean();
    }
}
