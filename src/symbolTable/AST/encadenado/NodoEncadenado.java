package symbolTable.AST.encadenado;

import lexicalAnalyzer.Token;
import symbolTable.SemanticException;
import symbolTable.TipoMetodo;

public abstract class NodoEncadenado {
    protected Token encadenadoToken;
    protected NodoEncadenado encadenado;
    protected boolean isLeftSide = false;

    public NodoEncadenado(Token encadenadoToken){
        this.encadenadoToken = encadenadoToken;
    }

    public void addEncadenado(NodoEncadenado enc){
        this.encadenado = enc;
    }

    public abstract TipoMetodo check(TipoMetodo t) throws SemanticException;
    public abstract boolean isVariable();
    public abstract boolean isMethodOrConstructor();
    public abstract void generate();

    public void setLeftSide(boolean leftSide){this.isLeftSide = leftSide;}
}
