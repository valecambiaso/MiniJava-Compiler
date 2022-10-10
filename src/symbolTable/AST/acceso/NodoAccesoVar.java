package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.Tipo;

public class NodoAccesoVar extends NodoPrimario{
    private Token varToken;

    public NodoAccesoVar(Token varToken){
        this.varToken = varToken;
    }


}
