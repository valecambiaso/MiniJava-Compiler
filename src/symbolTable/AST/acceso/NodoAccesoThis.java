package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.Tipo;

public class NodoAccesoThis extends NodoPrimario{
    private Token thisToken;

    public NodoAccesoThis(Token thisToken){
        this.thisToken = thisToken;
    }


}
