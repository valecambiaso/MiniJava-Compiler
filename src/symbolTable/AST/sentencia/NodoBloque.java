package symbolTable.AST.sentencia;

import symbolTable.SemanticException;
import symbolTable.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodoBloque extends NodoSentencia{
    private List<NodoSentencia> sentences;
    private HashMap<String,NodoVarLocal> localVariables;
    private int nextLocalVarOffset;

    public NodoBloque(){
        sentences = new ArrayList<>();
        localVariables = new HashMap<>();
    }

    public int getNextLocalVarOffset(){return this.nextLocalVarOffset;}

    public void addSentence(NodoSentencia sentence) {
        sentences.add(sentence);
    }

    public void addLocalVar(NodoVarLocal localVar){
        localVariables.put(localVar.getLocalVarToken().getLexeme(),localVar);
        localVar.setLocalVarOffset(nextLocalVarOffset);
        nextLocalVarOffset--;
    }

    public NodoVarLocal getLocalVar(String varName){
        return localVariables.get(varName);
    }

    public int getLocalVarCant(){return this.localVariables.size();}

    public void checkSentences() throws SemanticException{
        if(SymbolTable.actualBlocks.isEmpty()){
            this.nextLocalVarOffset = 0;
        }else {
            this.nextLocalVarOffset = SymbolTable.actualBlocks.get(0).getNextLocalVarOffset();
        }
        SymbolTable.actualBlocks.add(0,this); //El bloque actual queda en la pos 0
        for(NodoSentencia sentence : sentences){
            sentence.checkSentences();
        }
        SymbolTable.actualBlocks.remove(this);
    }

    public void generate(){
        SymbolTable.actualBlocks.add(0,this); //El bloque actual queda en la pos 0
        for(NodoSentencia sentence : sentences){
            sentence.generate();
        }
        SymbolTable.actualBlocks.remove(this);

        SymbolTable.instructions.add("FMEM "+localVariables.size()+" ; Libero el lugar ocupado para las " + localVariables.size() + " variables del bloque");
    }
}
