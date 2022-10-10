package symbolTable;

import lexicalAnalyzer.Token;

public class Atributo {
    private String attributeVisibility;
    private Tipo attributeType;
    private Token attributeToken;

    public Atributo(String attributeVisibility, Tipo attributeType, Token attributeToken){
        this.attributeVisibility = attributeVisibility;
        this.attributeType = attributeType;
        this.attributeToken = attributeToken;
    }

    public Token getToken() {
        return this.attributeToken;
    }

    public void checkStatements() throws SemanticException {
        attributeType.checkIfTypeExists();
    }
}
