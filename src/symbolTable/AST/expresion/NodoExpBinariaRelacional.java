package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.TipoBoolean;
import symbolTable.TipoMetodo;

public class NodoExpBinariaRelacional extends NodoExpBinaria{
    public NodoExpBinariaRelacional(Token binaryOperator) {
        super(binaryOperator);
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        if(this.expLeftSide.check().getTypeName().equals("int") && this.expRightSide.check().getTypeName().equals("int")){
            return new TipoBoolean();
        }else{
            throw new SemanticException(this.binaryOperator, "el operador binario " + this.binaryOperator.getLexeme() + " solo funciona con operandos de tipo entero");
        }
    }

    @Override
    public void generate() {
        this.expLeftSide.generate();
        this.expRightSide.generate();
        if(this.binaryOperator.getLexeme().equals("<")){
            SymbolTable.instructions.add("LT");
        }else if(this.binaryOperator.getLexeme().equals(">")){
            SymbolTable.instructions.add("GT");
        }else if(this.binaryOperator.getLexeme().equals("<=")){
            SymbolTable.instructions.add("LE");
        }else if(this.binaryOperator.getLexeme().equals(">=")){
            SymbolTable.instructions.add("GE");
        }
    }
}
