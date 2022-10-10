package symbolTable.AST.expresion;

import lexicalAnalyzer.Token;

public class NodoExpBinariaMatematica extends NodoExpBinaria{
    public NodoExpBinariaMatematica(Token binaryOperator){
        super(binaryOperator);
    }
}
