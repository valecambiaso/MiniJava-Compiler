package syntacticAnalyzer;

import lexicalAnalyzer.Token;

public class SyntacticException extends Exception{

    public SyntacticException(Token actualToken, String expectedTokenName){
        super(makeErrorMessage(actualToken, expectedTokenName));
    }

    private static String makeErrorMessage(Token actualToken, String expectedTokenName){
        String errorMessage = "Error Sintactico en linea " + actualToken.getLineNumber() + ": se esperaba " + expectedTokenName + " pero se encontro " + actualToken.getLexeme() + "\n";
        errorMessage += "[Error:" + actualToken.getLexeme() + "|" + actualToken.getLineNumber() + "]\n";
        return errorMessage;
    }
}
