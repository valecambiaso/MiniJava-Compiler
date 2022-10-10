package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;

public class NodoIf extends NodoSentencia{
    private Token ifToken;
    private NodoExpresion condition;
    private NodoSentencia sentence;
    private NodoSentencia else_;

    public NodoIf(Token ifToken, NodoExpresion condition, NodoSentencia sentence){
        this.ifToken = ifToken;
        this.condition = condition;
        this.sentence = sentence;
    }

    public void addElse(NodoSentencia else_){
        this.else_ = else_;
    }
}
