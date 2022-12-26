package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;
import symbolTable.*;

public class NodoExpUnaria extends NodoExpresion{
    private Token unaryOperator;
    private NodoOperando operand;

    public NodoExpUnaria(Token binaryOperator, NodoOperando operand){
        this.unaryOperator = binaryOperator;
        this.operand = operand;
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        if(unaryOperator == null){
            return operand.check(); //expresion unaria puede no tener operador
        }else{
            if(unaryOperator.getLexeme().equals("!")){
                if(operand.check().getTypeName().equals("boolean")){
                    return new TipoBoolean();
                }else{
                    throw new SemanticException(unaryOperator, "el operador unario " + this.unaryOperator.getLexeme() + " solo funciona con operando de tipo boolean");
                }
            }else{ //el operador es + o -
                if(operand.check().getTypeName().equals("int")){
                    return new TipoInt();
                }else{
                    throw new SemanticException(unaryOperator, "el operador unario " + this.unaryOperator.getLexeme() + " solo funciona con operando de tipo int");
                }
            }
        }
    }

    @Override
    public void generate() {
        this.operand.generate();
        if(this.unaryOperator.getLexeme().equals("!")){
            SymbolTable.instructions.add("NOT");
        }else if(this.unaryOperator.getLexeme().equals("-")){
            SymbolTable.instructions.add("NEG");
        }
    }
}
