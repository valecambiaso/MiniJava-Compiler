package symbolTable;

import lexicalAnalyzer.Token;
import symbolTable.AST.sentencia.NodoBloque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Metodo {
    private boolean isStatic;
    private TipoMetodo returnType;
    private Token methodToken;
    private HashMap<String,ParametroFormal> formalParameters;
    private List<ParametroFormal> formalParametersList;
    private NodoBloque bloque;
    private Token classCont;

    public Metodo(boolean isStatic, TipoMetodo returnType, Token methodToken, Token classCont){
        formalParameters = new HashMap<>();
        formalParametersList = new ArrayList<>();

        this.isStatic = isStatic;
        this.returnType = returnType;
        this.methodToken = methodToken;
        this.classCont = classCont;
    }

    public Token getToken(){
        return this.methodToken;
    }

    public boolean isStatic(){
        return isStatic;
    }

    public HashMap<String,ParametroFormal> getFormalParameters(){
        return formalParameters;
    }

    public boolean equalSignature(Metodo otherMethod){
        boolean equalStatic = otherMethod.isStatic() == isStatic();
        boolean equalReturn = otherMethod.getReturnType().equals(returnType.getTypeName());
        boolean equalName = otherMethod.getToken().getLexeme().equals(methodToken.getLexeme());
        boolean equalParameters = true;
        if(otherMethod.getFormalParameters().size() == formalParameters.size()){
            for(int i = 0; i < formalParameters.size(); i++){
                ParametroFormal pf1 = otherMethod.getFormalParametersList().get(i);
                ParametroFormal pf2 = formalParametersList.get(i);
                if(!pf1.getFormalParameterType().equals(pf2.getFormalParameterType())){
                    equalParameters = false;
                    break;
                }
            }
        }else{
            equalParameters = false;
        }
        return equalStatic && equalReturn && equalName && equalParameters;
    }

    public String getReturnType(){
        return returnType.getTypeName();
    }

    public List<ParametroFormal> getFormalParametersList(){
        return formalParametersList;
    }

    public void insertFormalParameter(String formalParameterLexeme, ParametroFormal formalParameter) throws SemanticException {
        if(formalParameters.get(formalParameterLexeme) == null){
            this.formalParameters.put(formalParameterLexeme,formalParameter);
            this.formalParametersList.add(formalParameter);
        }else{
            throw new SemanticException(formalParameter.getToken(), "ya hay un parametro formal de nombre " + formalParameterLexeme + " en el metodo " + this.methodToken.getLexeme());
        }
    }

    public void checkStatements() throws SemanticException {
        returnType.checkIfTypeExists();
        for(ParametroFormal pf:formalParameters.values()){
            pf.checkIfTypeExists();
        }
    }

    public void addBlock(NodoBloque bloque) {
        this.bloque = bloque;
    }

    public void checkSentences() throws SemanticException{
        SymbolTable.actualMethod = this;
        bloque.checkSentences();
    }
}
