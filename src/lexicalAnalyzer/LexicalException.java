package lexicalAnalyzer;

public class LexicalException extends Exception{

    public LexicalException(String lexeme, int lineNumber, String line, int colNumber, String errorDetail){
        super(makeErrorMessage(lexeme, lineNumber, line, colNumber, errorDetail));
    }

    private static String makeErrorMessage(String lexeme, int lineNumber, String line, int colNumber, String errorDetail) {
        String errorMessage = "Error lexico en linea " + lineNumber + ": " + errorDetail + ".\n";
        errorMessage += makeFancyErrorReport(line, colNumber);
        errorMessage += "[Error:" + lexeme + "|" + lineNumber + "]\n";
        errorMessage += "Columna donde se produjo el error: " + colNumber + ".\n";
        return errorMessage;
    }

    private static String makeFancyErrorReport(String line, int colNumber) {
        String detail = "Detalle: ";
        StringBuilder cursor = new StringBuilder();
        String lineDetail = detail + line;
        for(int i = 0; i < detail.length() + colNumber; i++){
            if(lineDetail.charAt(i) == '\t'){
                cursor.append('\t');
            }else{
                cursor.append(' ');
            }
        }
        lineDetail += "\n";
        cursor.append('^');
        return lineDetail + cursor + "\n";
    }
}
