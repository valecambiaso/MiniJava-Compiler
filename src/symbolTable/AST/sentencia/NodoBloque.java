package symbolTable.AST.sentencia;

import symbolTable.SemanticException;
import symbolTable.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodoBloque extends NodoSentencia{
    private List<NodoSentencia> sentences;
    private HashMap<String,NodoVarLocal> localVariables;

    public NodoBloque(){
        sentences = new ArrayList<>();
        localVariables = new HashMap<>();
    }

    public void addSentence(NodoSentencia sentence) {
        sentences.add(sentence);
    }

    public void addLocalVar(NodoVarLocal localVar){
        /* TODO id no es el nombre de un par´ametro del m´etodo que contiene a la declaraci´on o una variable
            local definida anteriormente en el mismo bloque o anteriormente en un bloque que contiene a al
            bloque que contiene la declaraci´on.*/
    }

    public NodoVarLocal getLocalVar(String varName){
        return localVariables.get(varName);
    }

    public void checkSentences() throws SemanticException{
        SymbolTable.actualBlocks.add(this);
        for(NodoSentencia sentence : sentences){
            //sentence.checkSentences();
        }
        SymbolTable.actualBlocks.remove(this);
    }
}
