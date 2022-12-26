package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.TipoMetodo;

public class NodoAsignacionCompuesta extends NodoAsignacion{

    public NodoAsignacionCompuesta(Token asigToken) {
        super(asigToken);
    }

    public void checkSentences() throws SemanticException {
        super.checkSentences();

        if(!this.accessType.getTypeName().equals("int")){
            throw new SemanticException(asigToken, "el tipo del acceso debe ser int");
        }

        if(!this.expressionType.getTypeName().equals("int")){
            throw new SemanticException(asigToken, "el tipo de la expresion debe ser int");
        }
    }

    public void generate(){
        asigRightSide.generate();
        asigLeftSide.generate();

        if(this.asigToken.getLexeme().equals("+=")){
            SymbolTable.instructions.add("ADD ; Se suma el lado derecho de la asignacion al izquierdo");
        }else if(this.asigToken.getLexeme().equals("-=")){
            SymbolTable.instructions.add("SUB ; Se resta el lado derecho de la asignacion al izquierdo");
        }

        asigLeftSide.setLeftSide(true);
        asigLeftSide.generate();
    }
}
