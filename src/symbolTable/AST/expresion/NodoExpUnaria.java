package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;

public class NodoExpUnaria extends NodoExpresion{
    private Token binaryOperator;
    private NodoOperando operand;

    public NodoExpUnaria(Token binaryOperator, NodoOperando operand){
        this.binaryOperator = binaryOperator;
        this.operand = operand;
    }
}
