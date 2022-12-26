package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;

public class NodoIf extends NodoSentencia{
    private Token ifToken;
    private NodoExpresion condition;
    private NodoSentencia sentence;
    private NodoSentencia else_;

    public NodoIf(Token ifToken, NodoExpresion condition, NodoSentencia sentence){
        this.ifToken = ifToken;
        this.condition = condition;
        this.sentence = sentence;
    }

    public void addElse(NodoSentencia else_){
        this.else_ = else_;
    }

    @Override
    public void checkSentences() throws SemanticException {
        if(condition.check().getTypeName().equals("boolean")){
            NodoBloque ifBlock = new NodoBloque();
            ifBlock.addSentence(sentence);
            ifBlock.checkSentences();

            if(else_ != null){
                NodoBloque elseBlock = new NodoBloque();
                elseBlock.addSentence(else_);
                elseBlock.checkSentences();
            }

        }else{
            throw new SemanticException(ifToken, "la condicion del if debe ser de tipo boolean");
        }
    }

    @Override
    public void generate() {
        condition.generate();
        if(else_ != null){
            int labelElse = SymbolTable.getIndex();
            int labelEnd = SymbolTable.getIndex();
            SymbolTable.instructions.add("BF l"+labelElse+" ; Salta al bloque del else si la condicion es falsa");
            sentence.generate();
            SymbolTable.instructions.add("JUMP l"+labelEnd+" ; Salta para evitar ejecutar el bloque else");
            SymbolTable.instructions.add("l"+labelElse+": NOP ; Label que marca el inicio del bloque else");
            else_.generate();
            SymbolTable.instructions.add("l"+labelEnd+": NOP ; Label que marca el final del bloque else");
        }else{
            int l = SymbolTable.getIndex();
            SymbolTable.instructions.add("BF l"+l+" ; Salta para evitar ejecutar el bloque if");
            sentence.generate();
            SymbolTable.instructions.add("l"+l+": NOP ; Label que marca el final del if");
        }
    }

}
