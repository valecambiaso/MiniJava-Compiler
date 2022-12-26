package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;
import symbolTable.*;

public class NodoExpBinariaBooleana extends NodoExpBinaria{
    public NodoExpBinariaBooleana(Token binaryOperator) {
        super(binaryOperator);
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        if(this.expLeftSide.check().getTypeName().equals("boolean") && this.expRightSide.check().getTypeName().equals("boolean")){
            return new TipoBoolean();
        }else{
            throw new SemanticException(this.binaryOperator, "el operador binario " + this.binaryOperator.getLexeme() + " solo funciona con operandos de tipo boolean");
        }
    }

    @Override
    public void generate() {
        this.expLeftSide.generate();
        this.expRightSide.generate();
        if(this.binaryOperator.getLexeme().equals("&&")){
            SymbolTable.instructions.add("AND");
        }else if(this.binaryOperator.getLexeme().equals("||")){
            SymbolTable.instructions.add("OR");
        }
    }
}
