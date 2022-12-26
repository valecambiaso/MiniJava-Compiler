package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.Tipo;
import symbolTable.TipoNull;

public class Null extends NodoOperando {
    private Token token;

    public Null(Token token) {
        this.token = token;
    }

    public Tipo check() throws SemanticException {
        return new TipoNull();
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("PUSH 0 ; Apilo null");
    }
}
