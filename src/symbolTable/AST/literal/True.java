package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.Tipo;
import symbolTable.TipoBoolean;

public class True extends NodoOperando {
    private Token token;

    public True(Token token) {
        this.token = token;
    }

    public Tipo check() throws SemanticException {
        return new TipoBoolean();
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("PUSH 1 ; Apilo true");
    }
}
