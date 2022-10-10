package symbolTable.AST.encadenado;

import lexicalAnalyzer.Token;
import symbolTable.AST.encadenado.NodoEncadenado;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.Tipo;

import java.util.List;

public class NodoMetodoEncadenado extends NodoEncadenado {
    private List<NodoExpresion> params;

    public NodoMetodoEncadenado(Token encadenadoToken, List<NodoExpresion> params){
        super(encadenadoToken);
        this.params = params;
    }


}
