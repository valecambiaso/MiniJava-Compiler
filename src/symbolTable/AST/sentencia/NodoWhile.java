package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;

public class NodoWhile extends NodoSentencia{
    private Token whileToken;
    private NodoExpresion condition;
    private NodoSentencia sentence;

    public NodoWhile(Token whileToken, NodoExpresion condition, NodoSentencia sentence){
        this.whileToken = whileToken;
        this.condition = condition;
        this.sentence = sentence;
    }
}
