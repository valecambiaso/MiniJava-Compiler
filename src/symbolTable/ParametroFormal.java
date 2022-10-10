package symbolTable;

import lexicalAnalyzer.Token;

public class ParametroFormal {
    private Tipo formalParameterType;
    private Token formalParameterToken;

    public ParametroFormal(Tipo formalParameterType, Token formalParameterToken){
        this.formalParameterType = formalParameterType;
        this.formalParameterToken = formalParameterToken;
    }

    public Token getToken(){
        return this.formalParameterToken;
    }

    public String getFormalParameterType(){
        return formalParameterType.getTypeName();
    }

    public void checkIfTypeExists() throws SemanticException {
        formalParameterType.checkIfTypeExists();
    }
}
