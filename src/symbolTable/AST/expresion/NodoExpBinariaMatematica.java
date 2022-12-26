package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.TipoInt;
import symbolTable.TipoMetodo;

public class NodoExpBinariaMatematica extends NodoExpBinaria{
    public NodoExpBinariaMatematica(Token binaryOperator){
        super(binaryOperator);
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        if(this.expLeftSide.check().getTypeName().equals("int") && this.expRightSide.check().getTypeName().equals("int")){
            return new TipoInt();
        }else{
            throw new SemanticException(this.binaryOperator, "el operador binario " + this.binaryOperator.getLexeme() + " solo funciona con operandos de tipo entero");
        }
    }

    @Override
    public void generate() {
        this.expLeftSide.generate();
        this.expRightSide.generate();
        if(this.binaryOperator.getLexeme().equals("+")){
            SymbolTable.instructions.add("ADD");
        }else if(this.binaryOperator.getLexeme().equals("-")){
            SymbolTable.instructions.add("SUB");
        }else if(this.binaryOperator.getLexeme().equals("*")){
            SymbolTable.instructions.add("MUL");
        }else if(this.binaryOperator.getLexeme().equals("/")){
            SymbolTable.instructions.add("DIV");
        }else if(this.binaryOperator.getLexeme().equals("%")){
            SymbolTable.instructions.add("MOD");
        }
    }
}
