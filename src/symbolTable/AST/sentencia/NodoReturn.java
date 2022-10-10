package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.expresion.NodoExpresion;

public class NodoReturn extends NodoSentencia{
    private Token returnToken;
    private NodoExpresion returnExp;

    public NodoReturn(Token returnToken){
        this.returnToken = returnToken;
    }

    public void addExp(NodoExpresion returnExp){
        this.returnExp = returnExp;
    }


}
