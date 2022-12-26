package symbolTable;

import lexicalAnalyzer.Token;
import symbolTable.AST.sentencia.NodoBloque;

import java.awt.desktop.PreferencesEvent;
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
    private Integer methodOffset;
    private int nextParameterOffset;

    public Metodo(boolean isStatic, TipoMetodo returnType, Token methodToken, Token classCont){
        formalParameters = new HashMap<>();
        formalParametersList = new ArrayList<>();

        this.isStatic = isStatic;
        this.returnType = returnType;
        this.methodToken = methodToken;
        this.classCont = classCont;
        nextParameterOffset = isStatic ? 3 : 4;
    }

    public Token getToken(){
        return this.methodToken;
    }

    public Token getClassCont(){return this.classCont;}

    public boolean isStatic(){
        return isStatic;
    }

    public HashMap<String,ParametroFormal> getFormalParameters(){
        return formalParameters;
    }

    public boolean equalSignature(Metodo otherMethod){
        boolean equalStatic = otherMethod.isStatic() == isStatic();
        boolean equalReturn = otherMethod.getReturnTypeName().equals(returnType.getTypeName());
        boolean equalName = otherMethod.getToken().getLexeme().equals(methodToken.getLexeme());
        boolean equalParameters = true;
        if(otherMethod.getFormalParameters().size() == formalParameters.size()){
            for(int i = 0; i < formalParameters.size(); i++){
                ParametroFormal pf1 = otherMethod.getFormalParametersList().get(i);
                ParametroFormal pf2 = formalParametersList.get(i);
                if(!pf1.getFormalParameterTypeName().equals(pf2.getFormalParameterTypeName())){
                    equalParameters = false;
                    break;
                }
            }
        }else{
            equalParameters = false;
        }
        return equalStatic && equalReturn && equalName && equalParameters;
    }

    public boolean methodConformsParameters(List<TipoMetodo> actualParametersType) throws SemanticException {
        boolean conforms = true;
        for(int i = 0; i < formalParameters.size(); i++){
            TipoMetodo fpType = formalParametersList.get(i).getFormalParameterType();
            TipoMetodo apType = actualParametersType.get(i);
            if(!apType.isSubtype(fpType)){
                conforms = false;
                break;
            }
        }
        return conforms;
    }

    public String getReturnTypeName(){
        return returnType.getTypeName();
    }
    public TipoMetodo getReturnType(){return returnType;}

    public ParametroFormal getFormalParameter(String formalParameterName){return formalParameters.get(formalParameterName);}

    public List<ParametroFormal> getFormalParametersList(){
        return formalParametersList;
    }

    public boolean isFormalParameter(String formalParameterName){
        for(ParametroFormal pf : formalParametersList){
            if(pf.getToken().getLexeme().equals(formalParameterName))
                return true;
        }
        return false;
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
        addOffsetToParameters();
    }

    private void addOffsetToParameters(){
        for(int i = formalParameters.size() - 1; i >= 0; i--){ //Stack crece de max a 0, entonces asigno offset al reves.
            formalParametersList.get(i).setParameterOffset(nextParameterOffset);
            nextParameterOffset++;
        }
    }

    public void addBlock(NodoBloque bloque) {
        this.bloque = bloque;
    }

    public void checkSentences() throws SemanticException{
        SymbolTable.actualMethod = this;
        bloque.checkSentences();
    }

    public String getMethodLabel(){
        return this.classCont.getLexeme() + "_" + this.methodToken.getLexeme();
    }

    public int getOffsetPlaceToReturn(){
        if(this.isStatic()){
            return formalParametersList.size();
        }else{
            return formalParametersList.size() + 1; //this
        }
    }

    public int getOffsetThingToReturn(){
        if(this.isStatic()){
            return 3 + formalParametersList.size();
        }else{
            return 4 + formalParametersList.size();
        }
    }

    public Integer getOffset(){
        return this.methodOffset;
    }

    public void setOffset(int offset){
        this.methodOffset = offset;
    }

    public void changeOffset(){
        int biggestOffsetAvailable = 0;
        for(Clase c : SymbolTable.classes.values()){
            if(c.getMethod(methodToken.getLexeme()) != null && c.getBiggestAvailableOffset() > biggestOffsetAvailable){
                biggestOffsetAvailable = c.getBiggestAvailableOffset();
                c.setBiggestAvailableOffset(biggestOffsetAvailable+1);
            }
        }
        for(Clase c : SymbolTable.classes.values()){
            if(c.getMethod(methodToken.getLexeme()) != null){
                c.methodsByOffset.remove(methodOffset);
                c.getMethod(methodToken.getLexeme()).setOffset(biggestOffsetAvailable);
                c.methodsByOffset.put(biggestOffsetAvailable,this);
            }
        }
    }

    public void generate(){
        SymbolTable.actualMethod = this;
        SymbolTable.instructions.add(getMethodLabel() + ": LOADFP ; Apila el enlace dinamico del RA del llamador");
        SymbolTable.instructions.add("LOADSP ; Apila el lugar donde comienza el RA de la unidad llamada");
        SymbolTable.instructions.add("STOREFP ; Actualiza el FP con el valor del tope de la pila");
        bloque.generate();
        SymbolTable.instructions.add("STOREFP ; Actualiza el FP con el valor del tope de la pila");
        SymbolTable.instructions.add("RET " + getOffsetPlaceToReturn() + " ; Retorna de la unidad liberando n lugares de la pila");
    }
}
