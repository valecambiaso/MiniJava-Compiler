package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.acceso.NodoAcceso;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.TipoMetodo;

public class NodoAsignacion extends NodoSentencia{
    protected NodoAcceso asigLeftSide;
    protected TipoMetodo accessType;
    protected NodoExpresion asigRightSide;
    protected TipoMetodo expressionType;
    protected Token asigToken;

    public NodoAsignacion(Token asigToken){
        this.asigToken = asigToken;
    }

    public void addAccessAndExpression(NodoAcceso access, NodoExpresion expression){
        this.asigLeftSide = access;
        this.asigRightSide = expression;
    }

    @Override
    public void checkSentences() throws SemanticException {
        this.accessType = asigLeftSide.check();

        if(!asigLeftSide.isVariable()){
            throw new SemanticException(asigToken, "el acceso debe corresponderse con una variable/parametro/atributo o tener un encadenado que finalice en variable");
        }

        this.expressionType = asigRightSide.check();

        if(!expressionType.isSubtype(accessType)){
            throw new SemanticException(asigToken, "el tipo de la expresion no conforma con el tipo del acceso");
        }
    }

    @Override
    public void generate() {
        asigRightSide.generate();
        asigLeftSide.setLeftSide(true);
        asigLeftSide.generate();
    }
}
