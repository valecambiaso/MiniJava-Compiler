package symbolTable;

import lexicalAnalyzer.Token;
import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.AST.sentencia.nodoBloquePredefinido.*;

import java.util.*;

public class SymbolTable {
    private static SymbolTable symbolTable;
    public static HashMap<String,Clase> classes;
    public static Clase actualClass;
    public static Metodo actualMethod;
    public static List<NodoBloque> actualBlocks;
    public static List<String> instructions;
    private Metodo mainMethod;
    private static int index;

    private SymbolTable() throws SemanticException {
        classes = new HashMap<>();
        actualBlocks = new LinkedList<>();
        instructions = new ArrayList<>();

        addObjectClass();
        addStringClass();
        addSystemClass();

        index = 0;
    }

    private void addObjectClass() throws SemanticException {
        Token objectClassToken = new Token("idClase","Object",0);
        ClaseConcreta objectClass = new ClaseConcreta(objectClassToken);
        objectClass.setInheritsFrom(null);

        Token debugPrintMethodToken = new Token("idMetVar","debugPrint",0);
        Metodo debugPrintMethod = new Metodo(true,new TipoVoid(),debugPrintMethodToken,objectClassToken);
        debugPrintMethod.addBlock(new DebugPrint());
        Token iFormalParameterToken = new Token("idMetVar","i",0);
        ParametroFormal iFormalParameter = new ParametroFormal(new TipoInt(),iFormalParameterToken);
        debugPrintMethod.insertFormalParameter("i",iFormalParameter);
        objectClass.insertMethod(debugPrintMethodToken.getLexeme(), debugPrintMethod);

        insertClass("Object",objectClass);
    }

    private void addStringClass() throws SemanticException {
        Token stringClassToken = new Token("idClase","String",0);
        ClaseConcreta stringClass = new ClaseConcreta(stringClassToken);
        stringClass.setInheritsFrom(new Token("idClase","Object",0));

        insertClass("String",stringClass);
    }

    private void addSystemClass() throws SemanticException {
        Token systemClassToken = new Token("idClase","System",0);
        ClaseConcreta systemClass = new ClaseConcreta(systemClassToken);
        systemClass.setInheritsFrom(new Token("idClase","Object",0));

        Token readMethodToken = new Token("idMetVar","read",0);
        Metodo readMethod = new Metodo(true,new TipoInt(),readMethodToken,systemClassToken);
        readMethod.addBlock(new Read());
        systemClass.insertMethod(readMethodToken.getLexeme(), readMethod);

        Token printBMethodToken = new Token("idMetVar","printB",0);
        Metodo printBMethod = new Metodo(true,new TipoVoid(),printBMethodToken,systemClassToken);
        printBMethod.addBlock(new PrintB());
        printBMethod.insertFormalParameter("b",new ParametroFormal(new TipoBoolean(),new Token("idMetVar","b",0)));
        systemClass.insertMethod(printBMethodToken.getLexeme(),printBMethod);

        Token printCMethodToken = new Token("idMetVar","printC",0);
        Metodo printCMethod = new Metodo(true,new TipoVoid(),printCMethodToken,systemClassToken);
        printCMethod.addBlock(new PrintC());
        printCMethod.insertFormalParameter("c",new ParametroFormal(new TipoChar(),new Token("idMetVar","c",0)));
        systemClass.insertMethod(printCMethodToken.getLexeme(),printCMethod);

        Token printIMethodToken = new Token("idMetVar","printI",0);
        Metodo printIMethod = new Metodo(true,new TipoVoid(),printIMethodToken,systemClassToken);
        printIMethod.addBlock(new PrintI());
        printIMethod.insertFormalParameter("i",new ParametroFormal(new TipoInt(),new Token("idMetVar","i",0)));
        systemClass.insertMethod(printIMethodToken.getLexeme(),printIMethod);

        Token printSMethodToken = new Token("idMetVar","printS",0);
        Metodo printSMethod = new Metodo(true,new TipoVoid(),printSMethodToken,systemClassToken);
        printSMethod.addBlock(new PrintS());
        printSMethod.insertFormalParameter("s",new ParametroFormal(new TipoClase(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        systemClass.insertMethod(printSMethodToken.getLexeme(),printSMethod);

        Token printlnMethodToken = new Token("idMetVar","println",0);
        Metodo printlnMethod = new Metodo(true,new TipoVoid(),printlnMethodToken,systemClassToken);
        printlnMethod.addBlock(new Println());
        systemClass.insertMethod(printlnMethodToken.getLexeme(),printlnMethod);

        Token printBlnMethodToken = new Token("idMetVar","printBln",0);
        Metodo printBlnMethod = new Metodo(true,new TipoVoid(),printBlnMethodToken,systemClassToken);
        printBlnMethod.addBlock(new PrintBln());
        printBlnMethod.insertFormalParameter("b",new ParametroFormal(new TipoBoolean(),new Token("idMetVar","b",0)));
        systemClass.insertMethod(printBlnMethodToken.getLexeme(),printBlnMethod);

        Token printClnMethodToken = new Token("idMetVar","printCln",0);
        Metodo printClnMethod = new Metodo(true,new TipoVoid(),printClnMethodToken,systemClassToken);
        printClnMethod.addBlock(new PrintCln());
        printClnMethod.insertFormalParameter("c",new ParametroFormal(new TipoChar(),new Token("idMetVar","c",0)));
        systemClass.insertMethod(printClnMethodToken.getLexeme(),printClnMethod);

        Token printIlnMethodToken = new Token("idMetVar","printIln",0);
        Metodo printIlnMethod = new Metodo(true,new TipoVoid(),printIlnMethodToken,systemClassToken);
        printIlnMethod.addBlock(new PrintIln());
        printIlnMethod.insertFormalParameter("i",new ParametroFormal(new TipoInt(),new Token("idMetVar","i",0)));
        systemClass.insertMethod(printIlnMethodToken.getLexeme(),printIlnMethod);

        Token printSlnMethodToken = new Token("idMetVar","printSln",0);
        Metodo printSlnMethod = new Metodo(true,new TipoVoid(),printSlnMethodToken,systemClassToken);
        printSlnMethod.addBlock(new PrintSln());
        printSlnMethod.insertFormalParameter("s",new ParametroFormal(new TipoClase(new Token("idClase","String",0)),new Token("idMetVar","s",0)));
        systemClass.insertMethod(printSlnMethodToken.getLexeme(),printSlnMethod);

        insertClass("System",systemClass);
    }

    public static SymbolTable getSymbolTableInstance() throws SemanticException {
        if(symbolTable == null){
            symbolTable = new SymbolTable();
        }
        return symbolTable;
    }

    public static void resetSymbolTable(){
        symbolTable = null;
        classes = null;
        actualClass = null;
        actualMethod = null;
        actualBlocks = null;
        index = 0;
    }

    public void insertClass(String className, Clase newClass) throws SemanticException {
        if(classes.get(className) == null){
            classes.put(className, newClass);
        }else {
            throw new SemanticException(newClass.getToken(),"ya hay una clase o interfaz de nombre " + className);
        }
    }

    public Clase getClass(String className){
        return classes.get(className);
    }

    public static int getIndex(){
        index++;
        return index;
    }

    public void checkStatements() throws SemanticException {
        for(Map.Entry<String,Clase> c:classes.entrySet()){
            c.getValue().checkStatements();
        }
        checkForMainMethod();
    }

    private void checkForMainMethod() throws SemanticException {
        boolean mainMethodFound = false;
        for(Map.Entry<String,Clase> c:classes.entrySet()){
            if(c.getValue().isConcreteClass()){
                HashMap<String,Metodo> methods = c.getValue().getMethods();
                for(Metodo m:methods.values()){
                    Metodo mainMethod = new Metodo(true,new TipoVoid(),new Token("idMetVar","main",0),c.getValue().getToken());
                    if(m.equalSignature(mainMethod)){
                        if(!mainMethodFound){
                            mainMethodFound = true;
                            this.mainMethod = m;
                        }else{
                            throw new SemanticException(m.getToken(), "hay mas de un metodo main declarado");
                        }
                    }
                }
            }
        }
        if(!mainMethodFound){
            throw new SemanticException(new Token("idMetVar","main",0), "no hay ningun metodo main declarado");
        }
    }

    public void consolidate() throws SemanticException {
        for(Map.Entry<String,Clase> c:classes.entrySet()){
            c.getValue().consolidate();
        }
    }

    public void checkSentences() throws SemanticException{
        for(Map.Entry<String,Clase> c:classes.entrySet()){
            if(c.getValue().isConcreteClass()){
                c.getValue().checkSentences();
            }
        }
    }

    public void generate(){
        instructions.add(".CODE \n");
        callMainMethod();
        malloc();

        for(Map.Entry<String,Clase> c:classes.entrySet()){
            if(c.getValue().isConcreteClass()){
                c.getValue().generate();
            }
        }
    }

    private void callMainMethod() {
        instructions.add("PUSH " + mainMethod.getMethodLabel() + " \n");
        instructions.add("CALL \n");
        instructions.add("HALT \n");
    }

    private void malloc() {
        instructions.add("simple_malloc: LOADFP ; Inicializacion unidad \n");
        instructions.add("LOADSP \n");
        instructions.add("STOREFP ; Finaliza inicializacion del RA \n");
        instructions.add("LOADHL ; hl \n");
        instructions.add("DUP ; hl \n");
        instructions.add("PUSH 1 ; 1 \n");
        instructions.add("ADD ; hl + 1 \n");
        instructions.add("STORE 4 ; Guarda resultado (puntero a base del bloque) \n");
        instructions.add("LOAD 3 ; Carga cantidad de celdas a alojar (parametro) \n");
        instructions.add("ADD \n");
        instructions.add("STOREHL ; Mueve el heap limit (hl) \n");
        instructions.add("STOREFP \n");
        instructions.add("RET 1 ; Retorna eliminando el parametro \n");
    }

}
