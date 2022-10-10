package symbolTable;

import lexicalAnalyzer.Token;
import java.util.HashMap;

public abstract class Clase {
    protected Token classToken;
    protected HashMap<String,Interfaz> interfaces;
    protected HashMap<String,Metodo> methods;
    protected boolean isConsolidated;

    public abstract void checkStatements() throws SemanticException;
    public abstract boolean isConcreteClass();
    public abstract void checkCircularInheritance(HashMap<String,Token> classAncestors) throws SemanticException;
    public abstract void consolidate() throws SemanticException;
    public abstract HashMap<String,Atributo> getAttributes();

    public Token getToken(){
        return this.classToken;
    }

    public HashMap<String,Metodo> getMethods(){
        return methods;
    }
    public HashMap<String,Interfaz> getInterfaces(){return interfaces;}

    public void setInheritsFrom(Token inheritsFrom) {}
    public Token getInheritsFrom(){return null; }

    public void addInterface(String interfaceLexeme,Interfaz interfaz) throws SemanticException {
        if(interfaces.get(interfaceLexeme) == null){
            this.interfaces.put(interfaceLexeme,interfaz);
        }else{
            throw new SemanticException(interfaz.getToken(), "ya se implementa/extiende a la interfaz " + interfaceLexeme + " en " + this.classToken.getLexeme());
        }
    }

    public void insertMethod(String methodLexeme, Metodo method) throws SemanticException {
        if(methods.get(methodLexeme) == null){
            this.methods.put(methodLexeme,method);
        }else{
            throw new SemanticException(method.getToken(), "ya hay un metodo de nombre " + methodLexeme + " en " + this.classToken.getLexeme());
        }
    }

    public void insertAttribute(String attributeLexeme,Atributo attribute) throws SemanticException {}
    public void checkSentences() throws SemanticException{}
}
