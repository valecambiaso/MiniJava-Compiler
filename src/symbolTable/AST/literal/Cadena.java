package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.Tipo;
import symbolTable.TipoClase;

public class Cadena extends NodoOperando {
    private Token token;

    public Cadena(Token token){
        this.token = token;
    }

    @Override
    public Tipo check() throws SemanticException {
        return new TipoClase(new Token("idClase", "String", token.getLineNumber()));
    }
}
