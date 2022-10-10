package symbolTable.AST.encadenado;

import lexicalAnalyzer.Token;
import symbolTable.Tipo;

public abstract class NodoEncadenado {
    protected Token encadenadoToken;
    protected NodoEncadenado encadenado;

    public NodoEncadenado(Token encadenadoToken){
        this.encadenadoToken = encadenadoToken;
    }

    public void addEncadenado(NodoEncadenado enc){
        this.encadenado = enc;
    }

    //public abstract Tipo check(Tipo t);
}
