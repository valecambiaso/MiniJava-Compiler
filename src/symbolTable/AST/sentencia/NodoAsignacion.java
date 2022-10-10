package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.acceso.NodoAcceso;
import symbolTable.AST.expresion.NodoExpresion;

public class NodoAsignacion extends NodoSentencia{
    protected NodoAcceso asigLeftSide;
    protected NodoExpresion asigRightSide;
    protected Token asigToken;

    public NodoAsignacion(Token asigToken){
        this.asigToken = asigToken;
    }

    public void addAccessAndExpression(NodoAcceso access, NodoExpresion expression){
        this.asigLeftSide = access;
        this.asigRightSide = expression;
    }
}
