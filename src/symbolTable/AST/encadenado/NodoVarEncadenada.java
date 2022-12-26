package symbolTable.AST.encadenado;

import lexicalAnalyzer.Token;
import symbolTable.*;

public class NodoVarEncadenada extends NodoEncadenado {
    private Atributo classAttribute;

    public NodoVarEncadenada(Token encadenadoToken){
        super(encadenadoToken);
    }

    @Override
    public TipoMetodo check(TipoMetodo t) throws SemanticException {
        Clase encIzq = SymbolTable.getSymbolTableInstance().getClass(t.getTypeName());

        if(encIzq == null){
            throw new SemanticException(encadenadoToken, "la clase del elemento a la izquierda de la variable no existe");
        }else if(!encIzq.isConcreteClass()){
            throw new SemanticException(encadenadoToken,"no se pueden buscar atributos en la interfaz");
        }else{
            classAttribute = encIzq.getAttribute(encadenadoToken.getLexeme());
            if(classAttribute == null){
                throw new SemanticException(encadenadoToken, "no existe el atributo " + encadenadoToken.getLexeme() + " en la clase " + encIzq.getToken().getLexeme());
            }else if(classAttribute.getAttributeVisibility().equals("kw_private") &&
                    (!SymbolTable.actualClass.getToken().getLexeme().equals(classAttribute.getClassCont().getLexeme())
                            || !encIzq.getToken().getLexeme().equals(classAttribute.getClassCont().getLexeme()))){
                throw new SemanticException(encadenadoToken, "el atributo es privado");
            }
        }

        if(this.encadenado != null){
            return this.encadenado.check(classAttribute.getAttributeType());
        }else{
            return classAttribute.getAttributeType();
        }
    }

    @Override
    public boolean isVariable() {
        if(this.encadenado != null){
            return this.encadenado.isVariable();
        }else{
            return true;
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

    @Override
    public void generate() {
        if(!isLeftSide || encadenado != null){
            SymbolTable.instructions.add("LOADREF " + classAttribute.getOffset() + " ; Se busca el atributo " + this.encadenadoToken.getLexeme());
        }else{
            SymbolTable.instructions.add("SWAP");
            SymbolTable.instructions.add("STOREREF " + classAttribute.getOffset() + " ; Se almacena en el atributo " + this.encadenadoToken.getLexeme());
        }

        if(this.encadenado != null){
            this.encadenado.setLeftSide(this.isLeftSide);
            this.encadenado.generate();
        }
    }


}
