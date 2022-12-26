package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.*;
import symbolTable.AST.expresion.NodoExpresion;

import java.util.LinkedList;
import java.util.List;

public class NodoAccesoMetodoEstatico extends NodoAcceso{
    private Token classToken;
    private Token staticMethodToken;
    private List<NodoExpresion> expressionList;
    private Metodo staticMethod;

    public NodoAccesoMetodoEstatico(Token classToken, Token staticMethodToken, List<NodoExpresion> expressionList){
        this.classToken = classToken;
        this.staticMethodToken = staticMethodToken;
        this.expressionList = expressionList;
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        Clase classStaticCall = SymbolTable.getSymbolTableInstance().getClass(classToken.getLexeme());
        if(classStaticCall == null){
            throw new SemanticException(classToken, "se hace referencia a una clase que no existe");
        }else if(!classStaticCall.isConcreteClass()){
            throw new SemanticException(classToken, "no se puede hacer una llamada estatica a una interface");
        }

        staticMethod = classStaticCall.getMethod(staticMethodToken.getLexeme());
        if(staticMethod == null){
            throw new SemanticException(staticMethodToken, "el metodo no existe");
        }else if(!staticMethod.isStatic()){
            throw new SemanticException(staticMethodToken, "no se puede referenciar de forma estatica a un método no estatico");
        }else if(staticMethod.getFormalParameters().size() != expressionList.size()){
            throw new SemanticException(staticMethodToken, "la cantidad de parametros actuales no coincide con la cantidad de parametros formales");
        }else{
            List<TipoMetodo> actualParametersTypes = new LinkedList<>();
            for(NodoExpresion expression : expressionList){
                actualParametersTypes.add(expression.check());
            }
            if(!staticMethod.methodConformsParameters(actualParametersTypes)){
                throw new SemanticException(staticMethodToken, "el tipo de algun parametro actual no conforma con el parametro formal");
            }
        }

        if(this.encadenado != null){
            return this.encadenado.check(staticMethod.getReturnType());
        }else{
            return staticMethod.getReturnType();
        }
    }

    @Override
    public void generate() {
        if(staticMethod.isStatic()){
            if(!staticMethod.getReturnType().getTypeName().equals("void")){ //Metodo tiene return, hay que reservar lugar
                SymbolTable.instructions.add("RMEM 1 ; Reservo un lugar para el retorno");
            }
            for(NodoExpresion params : expressionList){
                params.generate();
            }

            SymbolTable.instructions.add("PUSH " + staticMethod.getMethodLabel() + " ; Guardo la etiqueta del metodo a llamar");
            SymbolTable.instructions.add("CALL ; Llamo al metodo");
        }else{
            SymbolTable.instructions.add("LOAD 3 ; Se apila la referencia al this");
            if (!staticMethod.getReturnType().getTypeName().equals("void")) { //Metodo tiene return, hay que reservar lugar
                SymbolTable.instructions.add("RMEM 1 ; Reservo un lugar para el retorno");
                SymbolTable.instructions.add("SWAP");
            }
            for (NodoExpresion params : expressionList) {
                params.generate();
                SymbolTable.instructions.add("SWAP");
            }
            SymbolTable.instructions.add("DUP ; Se duplica el this");
            SymbolTable.instructions.add("LOADREF 0 ; Se carga la VT");
            SymbolTable.instructions.add("LOADREF " + staticMethod.getOffset() + " ; Se carga la direccion del metodo");
            SymbolTable.instructions.add("CALL");
        }

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
            return true;
        }
    }
}
