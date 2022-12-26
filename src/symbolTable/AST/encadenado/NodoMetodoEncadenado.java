package symbolTable.AST.encadenado;

import lexicalAnalyzer.Token;
import symbolTable.*;
import symbolTable.AST.expresion.NodoExpresion;

import java.util.ArrayList;
import java.util.List;

public class NodoMetodoEncadenado extends NodoEncadenado {
    private List<NodoExpresion> params;
    private Metodo method;

    public NodoMetodoEncadenado(Token encadenadoToken, List<NodoExpresion> params){
        super(encadenadoToken);
        this.params = params;
    }

    @Override
    public TipoMetodo check(TipoMetodo t) throws SemanticException {
        Clase encIzq = SymbolTable.getSymbolTableInstance().getClass(t.getTypeName());

        if(encIzq == null){
            throw new SemanticException(encadenadoToken, "la clase del elemento a la izquierda del metodo no existe");
        }else{
            method = encIzq.getMethod(encadenadoToken.getLexeme());
            if(method == null){
                throw new SemanticException(encadenadoToken, "no se puede llamar al metodo " + encadenadoToken.getLexeme() + " ya que no fue declarado");
            }else if(method.getFormalParameters().size() != params.size()){
                throw new SemanticException(encadenadoToken, "la cantidad de parametros actuales no coincide con la cantidad de parametros formales");
            }else{
                List<TipoMetodo> actualParametersTypes = new ArrayList<>();
                for(NodoExpresion expression : params){
                    actualParametersTypes.add(expression.check());
                }
                if(!method.methodConformsParameters(actualParametersTypes)){
                    throw new SemanticException(encadenadoToken, "el tipo de algun parametro actual no conforma con el parametro formal");
                }
            }
        }

        if(this.encadenado != null){
            return this.encadenado.check(method.getReturnType());
        }else{
            return method.getReturnType();
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

    @Override
    public void generate() {
        if(method.isStatic()){
            SymbolTable.instructions.add("POP ; Se descarta el this porque es metodo estatico");
            if(!method.getReturnType().getTypeName().equals("void")){ //Metodo tiene return, hay que reservar lugar
                SymbolTable.instructions.add("RMEM 1 ; Reservo un lugar para el retorno");
            }
            for(NodoExpresion params : params){
                params.generate();
            }
            SymbolTable.instructions.add("PUSH " + method.getMethodLabel() + " ; Guardo la etiqueta del metodo a llamar");
            SymbolTable.instructions.add("CALL ; Llamo al metodo");
        }else{
            if(!method.getReturnType().getTypeName().equals("void")) { //Metodo tiene return, hay que reservar lugar
                SymbolTable.instructions.add("RMEM 1 ; Reservo un lugar para el retorno");
                SymbolTable.instructions.add("SWAP");
            }
            for(NodoExpresion params : params){
                params.generate();
                SymbolTable.instructions.add("SWAP");
            }
            SymbolTable.instructions.add("DUP ; Se duplica el this");
            SymbolTable.instructions.add("LOADREF 0 ; Se carga la VT");
            SymbolTable.instructions.add("LOADREF " + method.getOffset() + " ; Se carga la direccion del metodo");
            SymbolTable.instructions.add("CALL");
        }

        if(this.encadenado != null){
            this.encadenado.setLeftSide(this.isLeftSide);
            this.encadenado.generate();
        }
    }
}