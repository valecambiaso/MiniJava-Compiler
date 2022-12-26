package symbolTable;

import lexicalAnalyzer.Token;

public class ParametroFormal{
    private Tipo formalParameterType;
    private Token formalParameterToken;
    private Integer parameterOffset;

    public ParametroFormal(Tipo formalParameterType, Token formalParameterToken){
        this.formalParameterType = formalParameterType;
        this.formalParameterToken = formalParameterToken;
    }

    public Token getToken(){
        return this.formalParameterToken;
    }

    public String getFormalParameterTypeName(){
        return formalParameterType.getTypeName();
    }
    public Tipo getFormalParameterType(){
        return formalParameterType;
    }

    public void checkIfTypeExists() throws SemanticException {
        formalParameterType.checkIfTypeExists();
    }

    public void setParameterOffset(int offset){
        this.parameterOffset = offset;
    }

    public int getParameterOffset(){
        return this.parameterOffset;
    }
}
