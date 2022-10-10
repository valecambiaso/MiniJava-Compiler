package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;

public abstract class NodoExpBinaria extends NodoExpresion{
    protected Token binaryOperator;
    protected NodoExpresion expLeftSide;
    protected NodoExpresion expRightSide;

    public NodoExpBinaria(Token binaryOperator){
        this.binaryOperator = binaryOperator;
    }

    public void addExp(NodoExpresion expLeftSide, NodoExpresion expRightSide){
        this.expLeftSide = expLeftSide;
        this.expRightSide = expRightSide;
    }
}
