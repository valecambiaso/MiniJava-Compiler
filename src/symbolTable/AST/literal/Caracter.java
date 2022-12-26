package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.Tipo;
import symbolTable.TipoChar;

public class Caracter extends NodoOperando {
    private Token token;

    public Caracter(Token token) {
        this.token = token;
    }

    public Tipo check() throws SemanticException {
        return new TipoChar();
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("PUSH " + token.getLexeme() + " ; Apilo un caracter");
    }
}
