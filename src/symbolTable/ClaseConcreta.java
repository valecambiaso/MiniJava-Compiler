package symbolTable;

import lexicalAnalyzer.Token;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClaseConcreta extends Clase{

    private Token inheritsFrom;
    private HashMap<String,Atributo> attributes;
    protected HashMap<String,Metodo> methodsThisClass;
    private Constructor constructor;
    private boolean isFreeFromCircularInheritance;
    private LinkedList<Metodo> notStaticMethods;
    public int biggestAvailableOffset;

    public ClaseConcreta(Token concreteClassToken){
        attributes = new HashMap<>();
        interfaces = new HashMap<>();
        methods = new HashMap<>();
        methodsByOffset = new HashMap<>();
        methodsThisClass = new HashMap<>();

        classToken = concreteClassToken;
        isConsolidated = false;
        isFreeFromCircularInheritance = false;
        notStaticMethods = new LinkedList<>();
        biggestAvailableOffset = 0;

        if(classToken.getLexeme().equals("Object")){
            isConsolidated = true;
            isFreeFromCircularInheritance = true;
        }
    }

    public void insertMethod(String methodLexeme, Metodo method) throws SemanticException {
        super.insertMethod(methodLexeme,method);
        methodsThisClass.put(methodLexeme,method);
    }

    public void checkStatements() throws SemanticException {
        if(!classToken.getLexeme().equals("Object")) {
            if (SymbolTable.getSymbolTableInstance().getClass(inheritsFrom.getLexeme()) == null){
                throw new SemanticException(inheritsFrom, "se intenta heredar de la clase " + inheritsFrom.getLexeme() + " que no fue declarada");
            }else if (!SymbolTable.getSymbolTableInstance().getClass(inheritsFrom.getLexeme()).isConcreteClass()) {
                throw new SemanticException(inheritsFrom, "se intenta heredar de una inteface en la clase concreta " + classToken.getLexeme());
            }

            for(Map.Entry<String,Interfaz> interfaz:interfaces.entrySet()){
                if(SymbolTable.getSymbolTableInstance().getClass(interfaz.getKey()) == null){
                    throw new SemanticException(interfaz.getValue().getToken(), "se intenta implementar la interface " + interfaz.getValue() + " que no fue declarada");
                }else if(SymbolTable.getSymbolTableInstance().getClass(interfaz.getKey()).isConcreteClass()){
                    throw new SemanticException(interfaz.getValue().getToken(), "se intenta implementar una clase concreta en la clase concreta " + classToken.getLexeme());
                }
            }

            for(Metodo method: methods.values()){
                method.checkStatements();
            }

            for(Atributo attribute: attributes.values()){
                attribute.checkStatements();
            }
        }

        this.constructor = new Constructor(new Token("idClase",classToken.getLexeme(),0));
    }

    @Override
    public void consolidate() throws SemanticException {
        if(!isConsolidated){
            checkCircularInheritance(new HashMap<String,Token>());

            Clase ancestor = SymbolTable.getSymbolTableInstance().getClass(inheritsFrom.getLexeme());

            if(!ancestor.isConsolidated){
                ancestor.consolidate();
            }

            for(Interfaz interfaz:interfaces.values()){
                Clase implementedInterface = SymbolTable.getSymbolTableInstance().getClass(interfaz.getToken().getLexeme());
                if(!implementedInterface.isConsolidated){
                    implementedInterface.consolidate();
                }
            }

            for(Metodo methodAncestor:ancestor.getMethods().values()){
                Metodo methodThisClass = methods.get(methodAncestor.getToken().getLexeme());
                if(methodThisClass == null){
                    methods.put(methodAncestor.getToken().getLexeme(), methodAncestor);
                }else{
                    if(!methodAncestor.equalSignature(methodThisClass)){
                        throw new SemanticException(methodThisClass.getToken(), "no se redefine de manera adecuada el metodo " + methodAncestor.getToken().getLexeme() + " en la clase " + classToken.getLexeme());
                    }
                }
            }

            overrideInterfaceMethods();

            setOffsetMethods(ancestor);

            for(Atributo attributeAncestor:ancestor.getAttributes().values()){
                Atributo attributeThisClass = attributes.get(attributeAncestor.getToken().getLexeme());
                if(attributeThisClass != null){
                    throw new SemanticException(attributeThisClass.getToken(), "ya hay una variable de instancia de nombre " + attributeAncestor.getToken().getLexeme() + " en el ancestro " + ancestor.getToken().getLexeme());
                }else{
                    attributes.put(attributeAncestor.getToken().getLexeme(),attributeAncestor);
                }
            }

            setOffsetAttributes(ancestor);

            isConsolidated = true;
        }
    }

    private void setOffsetMethods(Clase ancestor){
        int cantNotStaticMethodsAncestor = ancestor.getNotStaticMethods().size();
        for(Metodo method : methods.values()){
            if(!method.isStatic()){
                Metodo ancestorMethod = ancestor.getMethod(method.getToken().getLexeme());
                if(ancestorMethod == null){
                    method.setOffset(cantNotStaticMethodsAncestor);
                    methodsByOffset.put(cantNotStaticMethodsAncestor,method);
                    cantNotStaticMethodsAncestor++;
                }else{
                    methodsByOffset.put(ancestorMethod.getOffset(),method);
                    method.setOffset(ancestorMethod.getOffset());
                }
                notStaticMethods.add(method);
            }
        }
        biggestAvailableOffset = cantNotStaticMethodsAncestor;
    }

    private void setOffsetAttributes(Clase ancestor){
        int cantAttributesAncestor = ancestor.getAttributes().size();
        for(Atributo atr : attributes.values()){
            if(atr.getOffset() == null){
                cantAttributesAncestor++;
                atr.setOffset(cantAttributesAncestor);
            }
        }
    }

    public void checkCircularInheritance(HashMap<String,Token> classAncestors) throws SemanticException {
        if(!isFreeFromCircularInheritance){
            if(classAncestors.get(inheritsFrom.getLexeme()) == null){
                classAncestors.put(inheritsFrom.getLexeme(),inheritsFrom);
                SymbolTable.getSymbolTableInstance().getClass(inheritsFrom.getLexeme()).checkCircularInheritance(classAncestors);
            }else{
                throw new SemanticException(inheritsFrom, "se produce herencia circular");
            }
            isFreeFromCircularInheritance = true;
            this.ancestors = classAncestors;
        }
    }

    private void overrideInterfaceMethods() throws SemanticException {
        for(Interfaz interfaz: interfaces.values()){
            HashMap<String,Metodo> interfaceMethods = SymbolTable.getSymbolTableInstance().getClass(interfaz.getToken().getLexeme()).getMethods();
            for(Metodo interfaceMethod:interfaceMethods.values()){
                Metodo methodThisClass = methods.get(interfaceMethod.getToken().getLexeme());
                if(methodThisClass == null){
                    throw new SemanticException(interfaceMethod.getToken(),"no se sobreescribio el metodo " + interfaceMethod.getToken().getLexeme() + " de la interfaz " + interfaz.getToken().getLexeme() + " en la clase " + classToken.getLexeme());
                }else if(!methodThisClass.equalSignature(interfaceMethod)){
                    throw new SemanticException(methodThisClass.getToken(), "el metodo de la clase no sobreescribe de manera adecuada el metodo de la interfaz "+ interfaz.getToken().getLexeme());
                }
            }
        }
    }

    @Override
    public boolean isConcreteClass() {
        return true;
    }

    public void setInheritsFrom(Token inheritsFrom){
        this.inheritsFrom = inheritsFrom;
    }
    public Token getInheritsFrom(){
        return this.inheritsFrom;
    }

    public void insertAttribute(String attributeLexeme,Atributo attribute) throws SemanticException {
        if(attributes.get(attributeLexeme) == null){
            this.attributes.put(attributeLexeme,attribute);
        }else{
            throw new SemanticException(attribute.getToken(), "ya hay un atributo de nombre " + attributeLexeme + " en la clase " + this.classToken.getLexeme());
        }
    }

    public HashMap<String, Atributo> getAttributes() {
        return attributes;
    }

    public int getBiggestAvailableOffset(){
        return biggestAvailableOffset;
    }

    public void setBiggestAvailableOffset(int offset){
        this.biggestAvailableOffset = offset;
    }

    public Atributo getAttribute(String attributeName){return attributes.get(attributeName);}

    public void checkSentences() throws SemanticException{
        SymbolTable.actualClass = this;
        for(Metodo method: methods.values()){
            if(method.getClassCont().getLexeme().equals(this.classToken.getLexeme()))
                method.checkSentences();
        }

        for(Interfaz i : interfaces.values()){
            Clase implementedInt = SymbolTable.getSymbolTableInstance().getClass(i.getToken().getLexeme());
            for(Metodo m : implementedInt.getMethods().values()){
                if(m.getOffset() == null){
                    m.setOffset(this.methods.get(m.getToken().getLexeme()).getOffset());
                    implementedInt.methodsByOffset.put(this.methods.get(m.getToken().getLexeme()).getOffset(),m);
                }else{
                    if(!m.getOffset().equals(this.methods.get(m.getToken().getLexeme()).getOffset())){
                        m.changeOffset();
                    }
                }
            }
        }
    }

    public void generate(){
        SymbolTable.actualClass = this;

        //Creacion de las VTables
        SymbolTable.instructions.add(".DATA");
        if(notStaticMethods.size() == 0){
            SymbolTable.instructions.add("VT_" + classToken.getLexeme() + ": NOP");
        }else{
            String instruction = "VT_" + classToken.getLexeme() + ": DW ";

            for(int i = 0; i < biggestAvailableOffset; i++){
                if(methodsByOffset.get(i) != null){
                    instruction += methods.get(methodsByOffset.get(i).getToken().getLexeme()).getMethodLabel() + ",";
                }else{
                    instruction += "0,";
                }
            }
            instruction = instruction.substring(0,instruction.length()-1);
            SymbolTable.instructions.add(instruction);
        }

        //Metodos
        SymbolTable.instructions.add(".CODE");
        constructor.generate();
        for(Metodo method: methods.values()){
            if(method.getClassCont().getLexeme().equals(this.classToken.getLexeme())) //Metodo en esta clase
                method.generate();
        }
    }
}
