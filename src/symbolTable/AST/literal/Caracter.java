package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.Tipo;
import symbolTable.TipoChar;

public class Caracter extends NodoOperando {
    private Token token;

    public Caracter(Token token) {
        this.token = token;
    }

    @Override
    public Tipo check() throws SemanticException {
        return new TipoChar();
    }
}
