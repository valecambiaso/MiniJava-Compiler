package symbolTable.AST.acceso;

import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.TipoMetodo;

public class NodoExpresionParentizada extends NodoAcceso{
    private NodoExpresion expression;

    public NodoExpresionParentizada(NodoExpresion expression){
        this.expression = expression;
    }


    @Override
    public TipoMetodo check() throws SemanticException {
        if(this.encadenado != null) {
            return this.encadenado.check(expression.check());
        }else{
            return this.expression.check();
        }
    }

    @Override
    public void generate() {
        expression.generate();

        if(this.encadenado != null){
            this.encadenado.setLeftSide(this.isLeftSide);
            this.encadenado.generate();
        }
    }

    @Override
    public boolean isVariable() {
        if(this.encadenado != null){
            return this.encadenado.isVariable();
        }else{
            return false;
        }
    }

    @Override
    public boolean isMethodOrConstructor() {
        if(this.encadenado != null){
            return this.encadenado.isMethodOrConstructor();
        }else{
            return false;
        }
    }
}
