package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.*;
import symbolTable.AST.expresion.NodoExpresion;

import java.util.ArrayList;
import java.util.List;

public class NodoAccesoMetodo extends NodoAcceso{
    private Token methodToken;
    private List<NodoExpresion> expressionList;
    private Metodo method;

    public NodoAccesoMetodo(Token methodToken, List<NodoExpresion> expressionList){
        this.methodToken = methodToken;
        this.expressionList = expressionList;
    }

    @Override
    public TipoMetodo check() throws SemanticException {
        method = SymbolTable.actualClass.getMethod(methodToken.getLexeme());
        if(method == null){
            throw new SemanticException(methodToken, "no se puede llamar al metodo " + methodToken.getLexeme() + " ya que no fue declarado");
        }else if(SymbolTable.actualMethod.isStatic() && !method.isStatic()){
            throw new SemanticException(methodToken, "no se puede llamar a un metodo no estatico en un metodo estatico");
        }else if(method.getFormalParameters().size() != expressionList.size()){
            throw new SemanticException(methodToken, "la cantidad de parametros actuales no coincide con la cantidad de parametros formales");
        }else{
            List<TipoMetodo> actualParametersTypes = new ArrayList<>();
            for(NodoExpresion expression : expressionList){
                actualParametersTypes.add(expression.check());
            }
            if(!method.methodConformsParameters(actualParametersTypes)){
                throw new SemanticException(methodToken, "el tipo de algun parametro actual no conforma con el parametro formal");
            }
        }

        if(this.encadenado != null){
            return this.encadenado.check(method.getReturnType());
        }else{
            return method.getReturnType();
        }
    }

    @Override
    public void generate() {
        if(!method.isStatic()) {
            SymbolTable.instructions.add("LOAD 3 ; Se apila la referencia al this");
            if (!method.getReturnType().getTypeName().equals("void")) { //Metodo tiene return, hay que reservar lugar
                SymbolTable.instructions.add("RMEM 1 ; Reservo un lugar para el retorno");
                SymbolTable.instructions.add("SWAP");
            }
            for (NodoExpresion params : expressionList) {
                params.generate();
                SymbolTable.instructions.add("SWAP");
            }
            SymbolTable.instructions.add("DUP ; Se duplica el this");
            SymbolTable.instructions.add("LOADREF 0 ; Se carga la VT");
            SymbolTable.instructions.add("LOADREF " + method.getOffset() + " ; Se carga la direccion del metodo");
            SymbolTable.instructions.add("CALL");
        }else{
            if(!method.getReturnType().getTypeName().equals("void")){ //Metodo tiene return, hay que reservar lugar
                SymbolTable.instructions.add("RMEM 1 ; Reservo un lugar para el retorno");
            }
            for(NodoExpresion params : expressionList){
                params.generate();
            }

            SymbolTable.instructions.add("PUSH " + method.getMethodLabel() + " ; Guardo la etiqueta del metodo a llamar");
            SymbolTable.instructions.add("CALL ; Llamo al metodo");
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
