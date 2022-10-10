package symbolTable;

import lexicalAnalyzer.Token;

public class SemanticException extends Exception{

    public SemanticException(Token actualToken, String message){
        super(makeErrorMessage(actualToken, message));
    }

    private static String makeErrorMessage(Token actualToken, String message){
        String errorMessage = "Error Semantico en linea " + actualToken.getLineNumber() + ": " + message + ".\n";
        errorMessage += "[Error:" + actualToken.getLexeme() + "|" + actualToken.getLineNumber() + "]\n";
        return errorMessage;
    }
}
