package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.*;

public class NodoAccesoThis extends NodoAcceso{
    private Token thisToken;

    public NodoAccesoThis(Token thisToken){
        this.thisToken = thisToken;
    }


    @Override
    public TipoMetodo check() throws SemanticException {
        if(SymbolTable.actualMethod.isStatic()){
            throw new SemanticException(thisToken, "en un metodo estatico el acceso no puede comenzar con this");
        }

        if(this.encadenado != null){
            return this.encadenado.check(new TipoClase(SymbolTable.actualClass.getToken()));
        }else{
            return new TipoClase(SymbolTable.actualClass.getToken());
        }
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("LOAD 3 ; Se apila el this en la pila");

        if(this.encadenado != null){
            this.encadenado.setLeftSide(this.isLeftSide);
            this.encadenado.generate();
        }
    }

    @Override
    public boolean isVariable() {
        if(this.encadenado != null){
            return this.encadenado.isVariable();
        }else {
            return false;
        }
    }

    @Override
    public boolean isMethodOrConstructor() {
        if(this.encadenado != null){
            return this.encadenado.isMethodOrConstructor();
        }else {
            return false;
        }
    }
}
