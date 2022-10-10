package symbolTable.AST.sentencia;

import symbolTable.AST.acceso.NodoAcceso;

public class NodoLlamada extends NodoSentencia{
    private NodoAcceso access;

    public NodoLlamada(NodoAcceso access){
        this.access = access;
    }
}
