package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.TipoMetodo;

public class NodoReturn extends NodoSentencia{
    private Token returnToken;
    private NodoExpresion returnExp;
    private TipoMetodo returnMethodType;
    private int variablesBeforeReturn;

    public NodoReturn(Token returnToken){
        this.returnToken = returnToken;
    }

    public void addExp(NodoExpresion returnExp){
        this.returnExp = returnExp;
    }

    @Override
    public void checkSentences() throws SemanticException {
        returnMethodType = SymbolTable.actualMethod.getReturnType();
        if(returnMethodType.getTypeName().equals("void")){
            if(returnExp != null){
                throw new SemanticException(returnToken, "el retorno del metodo es tipo void y no pueden retornarse valores");
            }
        }else{
            if(returnExp == null){
                throw new SemanticException(returnToken, "se deberia retornar algo de tipo " + returnMethodType.getTypeName());
            }else{
                TipoMetodo expressionType = returnExp.check();
                if(!expressionType.isSubtype(returnMethodType)){
                    throw new SemanticException(returnToken, "el tipo de la expresion retonada no conforma con el tipo de retorno del metodo");
                }
            }
        }
        variablesBeforeReturn = SymbolTable.actualBlocks.get(0).getLocalVarCant();
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("FMEM " + variablesBeforeReturn + " ; Se libera lugar de las variables declaradas");
        if(returnMethodType.getTypeName().equals("void")){ //El metodo no retorna nada
            SymbolTable.instructions.add("STOREFP ; El FP apunta al RA del metodo que lo llamo");
            SymbolTable.instructions.add("RET " + SymbolTable.actualMethod.getOffsetPlaceToReturn() + " ; Se retorna y se libera el lugar del RA llamado");
        }else{ //El metodo retorna algo
            returnExp.generate();
            SymbolTable.instructions.add("STORE " + SymbolTable.actualMethod.getOffsetThingToReturn() + " ; Se guarda el valor a retornar");
            SymbolTable.instructions.add("STOREFP ; El FP apunta al RA del metodo que lo llamo");
            SymbolTable.instructions.add("RET " + SymbolTable.actualMethod.getOffsetPlaceToReturn() + " ; Se retorna y se libera el lugar del RA llamado");
        }
    }
}
