package symbolTable.AST.literal;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoOperando;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.Tipo;
import symbolTable.TipoClase;

public class Cadena extends NodoOperando {
    private Token token;

    public Cadena(Token token){
        this.token = token;
    }

    public Tipo check() throws SemanticException {
        return new TipoClase(new Token("idClase", "String", token.getLineNumber()));
    }

    @Override
    public void generate() {
        int i = SymbolTable.getIndex();
        SymbolTable.instructions.add(".DATA");
        SymbolTable.instructions.add("string"+i+": DW " + token.getLexeme() + ",0 ; Apilo una cadena");
        SymbolTable.instructions.add(".CODE");
        SymbolTable.instructions.add("PUSH string"+i);
    }
}
