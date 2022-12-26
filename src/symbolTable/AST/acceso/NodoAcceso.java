package symbolTable.AST.acceso;

import symbolTable.AST.encadenado.NodoEncadenado;
import symbolTable.AST.expresion.NodoOperando;

public abstract class NodoAcceso extends NodoOperando {
    protected NodoEncadenado encadenado;
    protected boolean isLeftSide = false;

    public void addEncadenado(NodoEncadenado enc){
        this.encadenado = enc;
    }

    public abstract boolean isVariable();
    public abstract boolean isMethodOrConstructor();
    public void setLeftSide(boolean leftSide){
        this.isLeftSide = leftSide;
    }
}
