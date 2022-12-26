package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;

public class NodoWhile extends NodoSentencia{
    private Token whileToken;
    private NodoExpresion condition;
    private NodoSentencia sentence;

    public NodoWhile(Token whileToken, NodoExpresion condition, NodoSentencia sentence){
        this.whileToken = whileToken;
        this.condition = condition;
        this.sentence = sentence;
    }

    @Override
    public void checkSentences() throws SemanticException {
        if(condition.check().getTypeName().equals("boolean")){
            NodoBloque whileBlock = new NodoBloque();
            whileBlock.addSentence(sentence);
            whileBlock.checkSentences();
        }else{
            throw new SemanticException(whileToken, "la condicion del while debe ser de tipo boolean");
        }
    }

    @Override
    public void generate() {
        int startWhile = SymbolTable.getIndex();
        int endWhile = SymbolTable.getIndex();
        SymbolTable.instructions.add("l"+startWhile+": NOP ; Comienzo del while");
        condition.generate();
        SymbolTable.instructions.add("BF l"+endWhile+ "; Si la condicion del while es falsa, se salta para evitar el bloque");
        sentence.generate();
        SymbolTable.instructions.add("JUMP l"+startWhile+ "; Una vez que termina la ejecucion del bloque while, se salta al comienzo");
        SymbolTable.instructions.add("l"+endWhile+": NOP ; Final del while");
    }
}
