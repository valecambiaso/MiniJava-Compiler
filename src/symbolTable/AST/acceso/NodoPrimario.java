package symbolTable.AST.acceso;


import symbolTable.AST.encadenado.NodoEncadenado;

public abstract class NodoPrimario extends NodoAcceso {
    protected NodoEncadenado encadenado;

    public void addEncadenado(NodoEncadenado enc){
        this.encadenado = enc;
    }
}
