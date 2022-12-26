package symbolTable;

import lexicalAnalyzer.Token;

public class Constructor {
    private Token classToken;

    public Constructor(Token classToken){
        this.classToken = classToken;
    }

    public Token getClassToken(){return this.classToken;}

    public void generate(){
        SymbolTable.instructions.add("constructor_" + classToken.getLexeme() + ": LOADFP ; Apila el enlace dinamico del RA del llamador");
        SymbolTable.instructions.add("LOADSP ; Apila el lugar donde comienza el RA de la unidad llamada");
        SymbolTable.instructions.add("STOREFP ; Actualiza el FP con el valor del tope de la pila");
        SymbolTable.instructions.add("STOREFP ; Actualiza el FP con el valor del tope de la pila");
        SymbolTable.instructions.add("RET " + 1 + " ; Retorna de la unidad liberando n lugares de la pila");
    }

}
