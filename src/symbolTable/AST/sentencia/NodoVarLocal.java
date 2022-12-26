package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.TipoMetodo;

public class NodoVarLocal extends NodoSentencia  {
    private Token localVarToken;
    private Token equalsToken;
    private NodoExpresion expression;
    private TipoMetodo varType;
    private Integer localVarOffset;

    public NodoVarLocal(Token localVar, Token equals, NodoExpresion expression){
        this.localVarToken = localVar;
        this.equalsToken = equals;
        this.expression = expression;
    }

    public void addType(TipoMetodo type){
        this.varType = type;
    }

    public TipoMetodo getVarType(){return this.varType;}

    public Token getLocalVarToken(){return this.localVarToken;}

    @Override
    public void checkSentences() throws SemanticException {
        if(SymbolTable.actualMethod.isFormalParameter(localVarToken.getLexeme())){
            throw new SemanticException(localVarToken, "la variable " + localVarToken.getLexeme() + " ya fue declarada como un parametro formal");
        }

        for(NodoBloque block : SymbolTable.actualBlocks){
            if(block.getLocalVar(localVarToken.getLexeme()) != null){
                throw new SemanticException(localVarToken, "la variable " + localVarToken.getLexeme() + " ya fue declarada en este bloque o en un bloque que lo contiene");
            }
        }

        TipoMetodo expressionType = expression.check();

        SymbolTable.actualBlocks.get(0).addLocalVar(this);

        if(expressionType.getTypeName().equals("null")){
            throw new SemanticException(equalsToken, "el tipo de la expresion no puede ser nulo");
        }

        if(expressionType.getTypeName().equals("void")){
            throw new SemanticException(equalsToken, "el tipo de la expresion no puede ser void");
        }

        addType(expressionType);
    }

    public void setLocalVarOffset(int offset){
        this.localVarOffset = offset;
    }

    public int getLocalVarOffset(){
        return this.localVarOffset;
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("RMEM 1 ; Se reserva lugar para la variable local");
        expression.generate();
        SymbolTable.instructions.add("STORE " + getLocalVarOffset() + " ; Se almacena el valor de la expresion en la variable");
    }
}
