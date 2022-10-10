package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.Tipo;

import java.util.List;

public class NodoAccesoMetodoEstatico extends NodoPrimario{
    private Token classToken;
    private Token staticMethodToken;
    private List<NodoExpresion> expressionList;

    public NodoAccesoMetodoEstatico(Token classToken, Token staticMethodToken, List<NodoExpresion> expressionList){
        this.classToken = classToken;
        this.staticMethodToken = staticMethodToken;
        this.expressionList = expressionList;
    }

}
