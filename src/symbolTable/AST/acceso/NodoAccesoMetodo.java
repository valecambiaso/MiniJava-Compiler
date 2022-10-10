package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.Tipo;

import java.util.List;

public class NodoAccesoMetodo extends NodoPrimario{
    private Token methodToken;
    private List<NodoExpresion> expressionList;

    public NodoAccesoMetodo(Token methodToken, List<NodoExpresion> expressionList){
        this.methodToken = methodToken;
        this.expressionList = expressionList;
    }


}
