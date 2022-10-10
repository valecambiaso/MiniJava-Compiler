package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.Tipo;

public class NodoVarLocal extends NodoSentencia{
    private Token localVarToken;
    private Token equalsToken;
    private NodoExpresion expression;
    private Tipo varType;

    public NodoVarLocal(Token localVar, Token equals, NodoExpresion expression){
        this.localVarToken = localVar;
        this.equalsToken = equals;
        this.expression = expression;
    }

    public void addType(Tipo type){
        this.varType = type;
    }
}
