package symbolTable.AST.acceso;

import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.Tipo;

public class NodoExpresionParentizada extends NodoPrimario{
    private NodoExpresion expression;

    public NodoExpresionParentizada(NodoExpresion expression){
        this.expression = expression;
    }


}
