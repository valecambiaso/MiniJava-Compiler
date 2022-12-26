package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.Tipo;
import symbolTable.TipoInt;

public class Entero extends NodoOperando {
    private Token token;

    public Entero(Token token) {
        this.token = token;
    }

    public Tipo check() throws SemanticException {
        return new TipoInt();
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("PUSH " + token.getLexeme() + " ; Apilo un entero");
    }
}
