package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.Tipo;
import symbolTable.TipoBoolean;

public class True extends NodoOperando {
    private Token token;

    public True(Token token) {
        this.token = token;
    }

    @Override
    public Tipo check() throws SemanticException {
        return new TipoBoolean();
    }
}
