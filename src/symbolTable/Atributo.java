package symbolTable;

import lexicalAnalyzer.Token;

public class Atributo  {
    private String attributeVisibility;
    private Tipo attributeType;
    private Token attributeToken;
    private Token classCont;
    private Integer attributeOffset;

    public Atributo(String attributeVisibility, Tipo attributeType, Token attributeToken, Token classCont){
        this.attributeVisibility = attributeVisibility;
        this.attributeType = attributeType;
        this.attributeToken = attributeToken;
        this.classCont = classCont;
    }

    public Token getToken() {
        return this.attributeToken;
    }

    public Tipo getAttributeType(){return this.attributeType;}

    public Token getClassCont(){return this.classCont;}

    public String getAttributeVisibility(){return this.attributeVisibility;}

    public void checkStatements() throws SemanticException {
        attributeType.checkIfTypeExists();
    }

    public Integer getOffset(){
        return this.attributeOffset;
    }

    public void setOffset(int offset){
        this.attributeOffset = offset;
    }
}
