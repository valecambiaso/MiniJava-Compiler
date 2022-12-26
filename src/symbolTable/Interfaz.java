package symbolTable;

import lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.Map;

public class Interfaz extends Clase{
    private boolean isFreeFromCircularInheritance;
    public boolean hasOffsets;

    public Interfaz(Token interfaceToken){
        interfaces = new HashMap<>();
        methods = new HashMap<>();
        methodsByOffset = new HashMap<>();

        classToken = interfaceToken;
        isConsolidated = false;
        isFreeFromCircularInheritance = false;
        hasOffsets = false;
    }


    public void checkStatements() throws SemanticException {
        for(Map.Entry<String,Interfaz> interfaz:interfaces.entrySet()){
            if(SymbolTable.getSymbolTableInstance().getClass(interfaz.getKey()) == null){
                throw new SemanticException(interfaz.getValue().getToken(), "se intenta extender la interface " + interfaz.getValue() + " que no fue declarada");
            }else if(SymbolTable.getSymbolTableInstance().getClass(interfaz.getKey()).isConcreteClass()){
                throw new SemanticException(interfaz.getValue().getToken(), "se intenta extender una clase concreta en la interfaz " + classToken.getLexeme());
            }
        }

        for(Metodo method: methods.values()){
            if(method.isStatic()){
                throw new SemanticException(method.getToken(),"el metodo " + method.getToken().getLexeme() + " de la interfaz " + classToken.getLexeme() + " no puede ser estatico");
            }
            method.checkStatements();
        }
    }

    @Override
    public boolean isConcreteClass() {
        return false;
    }

    @Override
    public void consolidate() throws SemanticException {
        if(!isConsolidated){

            checkCircularInheritance(new HashMap<String,Token>());

            for(Interfaz interfaz: interfaces.values()) {
                Clase extendedInterface = SymbolTable.getSymbolTableInstance().getClass(interfaz.getToken().getLexeme());

                if(!extendedInterface.isConsolidated){
                    extendedInterface.consolidate();
                }

                for (Metodo methodInterface : extendedInterface.getMethods().values()) {
                    Metodo methodThisInterface = methods.get(methodInterface.getToken().getLexeme());
                    if (methodThisInterface == null) {
                        methods.put(methodInterface.getToken().getLexeme(),methodInterface);
                    } else {
                        if(!methodInterface.equalSignature(methodThisInterface)){
                            throw new SemanticException(methodThisInterface.getToken(),"el metodo " + methodInterface.getToken().getLexeme() + " ya se encuentra en la interfaz " + interfaz.getToken().getLexeme() + " con diferente signatura");
                        }
                    }
                }
            }
        }
    }


    @Override
    public void checkCircularInheritance(HashMap<String, Token> classAncestors) throws SemanticException {
        if(!isFreeFromCircularInheritance)
            for(Interfaz extendedInterface:interfaces.values()) {
                if(classAncestors.get(extendedInterface.getToken().getLexeme()) == null){
                    classAncestors.put(classToken.getLexeme(),classToken);
                    SymbolTable.getSymbolTableInstance().getClass(extendedInterface.getToken().getLexeme()).checkCircularInheritance(classAncestors);
                }else{
                    throw new SemanticException(extendedInterface.getToken(), "se produce extension circular");
                }
                isFreeFromCircularInheritance = true;
                this.ancestors = classAncestors;
                classAncestors.remove(this.getToken().getLexeme());
            }
    }

    @Override
    public HashMap<String, Atributo> getAttributes() {
        return null;
    }

    @Override
    public Atributo getAttribute(String attributeName) {
        return null;
    }

    @Override
    public void generate() {

    }


}