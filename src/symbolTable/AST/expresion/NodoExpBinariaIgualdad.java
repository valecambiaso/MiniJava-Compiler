package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;
import symbolTable.*;

public class NodoExpBinariaIgualdad extends NodoExpBinaria{
    public NodoExpBinariaIgualdad(Token binaryOperator) {
        super(binaryOperator);
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        TipoMetodo expLeftSideType = this.expLeftSide.check();
        TipoMetodo expRightSideType = this.expRightSide.check();
        if(expLeftSideType.isSubtype(expRightSideType) || expRightSideType.isSubtype(expLeftSideType)){
            return new TipoBoolean();
        }else{
            throw new SemanticException(this.binaryOperator, "el operador binario " + this.binaryOperator.getLexeme() + " solo funciona con operandos de tipos que conforman");
        }
    }

    @Override
    public void generate() {
        this.expLeftSide.generate();
        this.expRightSide.generate();
        if(this.binaryOperator.getLexeme().equals("==")){
            SymbolTable.instructions.add("EQ");
        }else if(this.binaryOperator.getLexeme().equals("!=")){
            SymbolTable.instructions.add("NE");
        }
    }
}

