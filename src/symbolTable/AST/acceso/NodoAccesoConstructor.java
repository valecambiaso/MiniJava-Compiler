package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.Tipo;

public class NodoAccesoConstructor extends NodoPrimario{
    private Token classToken;

    public NodoAccesoConstructor(Token classToken){
        this.classToken = classToken;
    }


}
