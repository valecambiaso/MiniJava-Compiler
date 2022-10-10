package lexicalAnalyzer;

import fileManager.FileManager;

import java.io.IOException;

public class LexicalAnalyzer {
    private String lexeme;
    private char actualCharacter;
    private String firstLineOfMultilineComment;
    private int numberOfMultilineComment;
    private int colOfMultilineComment;

    private FileManager fileManager;
    private TokenKeyword tokenKeywordDictionary;

    public LexicalAnalyzer(FileManager fileManager) throws IOException {
        this.fileManager = fileManager;
        updateActualCharacter();
        tokenKeywordDictionary = new TokenKeyword();
    }

    public Token nextToken() throws IOException, LexicalException {
        lexeme = "";
        return e0();
    }

    private Token e0() throws IOException, LexicalException {
        if(Character.isWhitespace(actualCharacter)){
            updateActualCharacter();
            return e0();
        }else if(Character.isDigit(actualCharacter)){
            buildLexeme();
            return e1();
        }else if(Character.isUpperCase(actualCharacter)){
            buildLexeme();
            return e2();
        }else if(Character.isLowerCase(actualCharacter)){
            buildLexeme();
            return e3();
        }else if(actualCharacter == '\''){
            buildLexeme();
            return e4();
        }else if(actualCharacter == '"'){
            buildLexeme();
            return e5();
        }else if(actualCharacter == '('){
            buildLexeme();
            return e6();
        }else if(actualCharacter == ')'){
            buildLexeme();
            return e7();
        }else if(actualCharacter == '{'){
            buildLexeme();
            return e8();
        }else if(actualCharacter == '}'){
            buildLexeme();
            return e9();
        }else if(actualCharacter == ';'){
            buildLexeme();
            return e10();
        }else if(actualCharacter == ','){
            buildLexeme();
            return e11();
        }else if(actualCharacter == '.'){
            buildLexeme();
            return e12();
        }else if(actualCharacter == '>'){
            buildLexeme();
            return e13();
        }else if(actualCharacter == '<'){
            buildLexeme();
            return e14();
        }else if(actualCharacter == '!'){
            buildLexeme();
            return e15();
        }else if(actualCharacter == '='){
            buildLexeme();
            return e16();
        }else if(actualCharacter == '+'){
            buildLexeme();
            return e17();
        }else if(actualCharacter == '-'){
            buildLexeme();
            return e18();
        }else if(actualCharacter == '*'){
            buildLexeme();
            return e19();
        }else if(actualCharacter == '/'){
            buildLexeme();
            return e20();
        }else if(actualCharacter == '&'){
            buildLexeme();
            return e21();
        }else if(actualCharacter == '|'){
            buildLexeme();
            return e22();
        }else if(actualCharacter == '%'){
            buildLexeme();
            return e23();
        }else if(fileManager.isEOF(actualCharacter)){
            return e24();
        }else{
            updateLexeme();
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), lexeme + " no es un simbolo valido");
        }
    }

    private Token e1() throws IOException, LexicalException {
        if(Character.isDigit(actualCharacter)){
            buildLexeme();
            if(lexeme.length() == 9 && Character.isDigit(actualCharacter)){
                throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " los numeros no pueden tener mas de 9 digitos");
            }else{
                return e1();
            }
        }else if(lexeme.length() < 10){
            return new Token("intLiteral", lexeme, fileManager.getLineNumber());
        }else{
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " los numeros no pueden tener mas de 9 digitos");
        }
    }

    private Token e2() throws IOException{
        if(Character.isLetter(actualCharacter) || Character.isDigit(actualCharacter) || actualCharacter == '_'){
            buildLexeme();
            return e2();
        }else{
            String tokenType = tokenKeywordDictionary.getTokenKeywordType(lexeme);
            if(tokenType == null){
                return new Token("idClase", lexeme, fileManager.getLineNumber());
            }else{
                return new Token(tokenType, lexeme, fileManager.getLineNumber());
            }
        }
    }

    private Token e3() throws IOException{
        if(Character.isLetter(actualCharacter) || Character.isDigit(actualCharacter) || actualCharacter == '_'){
            buildLexeme();
            return e3();
        }else{
            String tokenType = tokenKeywordDictionary.getTokenKeywordType(lexeme);
            if(tokenType == null){
                return new Token("idMetVar", lexeme, fileManager.getLineNumber());
            }else{
                return new Token(tokenType, lexeme, fileManager.getLineNumber());
            }
        }
    }

    private Token e4() throws IOException, LexicalException {
        if(actualCharacter == '\n' || actualCharacter == '\'' || fileManager.isEOF(actualCharacter)){
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " no es un char valido");
        }else if(actualCharacter == '\\'){
            buildLexeme();
            return e4_op2();
        }else{
            buildLexeme();
            return e4_op3();
        }
    }

    private Token e4_op2() throws IOException, LexicalException {
        if(actualCharacter == '\n' || fileManager.isEOF(actualCharacter)) {
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " no es un char valido");
        }else if(actualCharacter == 'u'){
            buildLexeme();
            return e4_op4();
        }else{
            buildLexeme();
            return e4_op3();
        }
    }

    private Token e4_op3() throws IOException, LexicalException {
        if(actualCharacter == '\''){
            buildLexeme();
            return new Token("charLiteral", lexeme, fileManager.getLineNumber());
        }else{
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " no se cerro el char de forma apropiada");
        }
    }

    private Token e4_op4() throws IOException, LexicalException{
        int unicodeSize = 0;
        do{
            if(actualCharacter >= 'A' && actualCharacter <= 'F'){
                buildLexeme();
                unicodeSize++;
            }else if(actualCharacter >= 'a' && actualCharacter <= 'f'){
                buildLexeme();
                unicodeSize++;
            }else if(actualCharacter >= '0' && actualCharacter <= '9'){
                buildLexeme();
                unicodeSize++;
            }else if(actualCharacter == '\'' && unicodeSize == 0){
                buildLexeme();
                return new Token("charLiteral", lexeme, fileManager.getLineNumber());
            }else{
                throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " el unicode no es válido");
            }
        }while(unicodeSize < 4 && actualCharacter != '\'');

        if(unicodeSize == 4 && actualCharacter == '\''){
            buildLexeme();
            return new Token("charLiteral",lexeme,fileManager.getLineNumber());
        }else{
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " el unicode no es válido");
        }
    }

    private Token e5() throws IOException, LexicalException { //String
        if(actualCharacter == '\n'){
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " no se puede tener un salto de línea en el String");
        }else if(actualCharacter == '\"'){
            buildLexeme();
            return new Token("stringLiteral", lexeme, fileManager.getLineNumber());
        }else if(actualCharacter == '\\'){
            buildLexeme();
            return e5_op2();
        }else {
            buildLexeme();
            return e5();
        }
    }

    private Token e5_op2() throws IOException, LexicalException {
        if(actualCharacter == '\n'){
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), " no se puede tener un salto de línea en el String");
        }else {
            buildLexeme();
            return e5();
        }
    }

    private Token e6() {
        return new Token("parentesisAbrirPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e7() {
        return new Token("parentesisCerrarPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e8() {
        return new Token("llaveAbrirPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e9() {
        return new Token("llaveCerrarPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e10() {
        return new Token("puntoComaPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e11() {
        return new Token("comaPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e12() {
        return new Token("puntoPunt", lexeme, fileManager.getLineNumber());
    }

    private Token e13() throws IOException {
        if(actualCharacter == '='){
            buildLexeme();
            return e13_op2();
        }else{
            return new Token("mayorOp", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e13_op2(){
        return new Token("mayorIgualOp", lexeme, fileManager.getLineNumber());
    }

    private Token e14() throws IOException {
        if(actualCharacter == '='){
            buildLexeme();
            return e14_op2();
        }else{
            return new Token("menorOp", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e14_op2(){
        return new Token("menorIgualOp", lexeme, fileManager.getLineNumber());
    }

    private Token e15() throws IOException {
        if(actualCharacter == '='){
            buildLexeme();
            return e15_op2();
        }else{
            return new Token("notOp", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e15_op2(){
        return new Token("notIgualOp", lexeme, fileManager.getLineNumber());
    }

    private Token e16() throws IOException {
        if(actualCharacter == '='){
            buildLexeme();
            return e16_op2();
        }else{
            return new Token("igualAsignacion", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e16_op2(){
        return new Token("igualOp", lexeme, fileManager.getLineNumber());
    }

    private Token e17() throws IOException {
        if(actualCharacter == '='){
            buildLexeme();
            return e17_op2();
        }else{
            return new Token("sumaOp", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e17_op2(){
        return new Token("sumaAsignacion", lexeme, fileManager.getLineNumber());
    }

    private Token e18() throws IOException {
        if(actualCharacter == '='){
            buildLexeme();
            return e18_op2();
        }else{
            return new Token("restaOp", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e18_op2(){
        return new Token("restaAsignacion", lexeme, fileManager.getLineNumber());
    }

    private Token e19() {
        return new Token("multOp", lexeme, fileManager.getLineNumber());
    }

    private Token e20() throws IOException, LexicalException { //division o comentarios
        if(actualCharacter == '/'){
            updateActualCharacter();
            lexeme = "";
            return e20_singleLine();
        }else if(actualCharacter == '*'){
            firstLineOfMultilineComment = fileManager.getLine();
            numberOfMultilineComment = fileManager.getLineNumber();
            colOfMultilineComment = fileManager.getCharNumber();
            updateActualCharacter();
            lexeme = "";
            return e20_multiLine();
        }else{
            return new Token("divOp", lexeme, fileManager.getLineNumber());
        }
    }

    private Token e20_singleLine() throws LexicalException, IOException {
        if(actualCharacter == '\n' || fileManager.isEOF(actualCharacter)){
            return e0();
        }else{
            updateActualCharacter();
            return e20_singleLine();
        }
    }

    private Token e20_multiLine() throws IOException, LexicalException {
        if(actualCharacter == '*') {
            updateActualCharacter();
            return e20_multiLine_op2();
        }else if(fileManager.isEOF(actualCharacter)){
            throw new LexicalException(lexeme, numberOfMultilineComment, firstLineOfMultilineComment, colOfMultilineComment, " el codigo fuente finalizo antes de que se cierre el comentario");
        }else{
            updateActualCharacter();
            return e20_multiLine();
        }
    }

    private Token e20_multiLine_op2() throws LexicalException, IOException {
        if(actualCharacter == '/'){
            updateActualCharacter();
            return e0();
        }else if(actualCharacter == '*'){
            updateActualCharacter();
            return e20_multiLine_op2();
        }else if(fileManager.isEOF(actualCharacter)){
            throw new LexicalException(lexeme, numberOfMultilineComment, firstLineOfMultilineComment, colOfMultilineComment, " el codigo fuente finalizo antes de que se cierre el comentario");
        }else{
            updateActualCharacter();
            return e20_multiLine();
        }
    }

    private Token e21() throws IOException, LexicalException {
        if(actualCharacter == '&'){
            buildLexeme();
            return e21_op2();
        }else{
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), lexeme + " no es un simbolo valido");
        }
    }

    private Token e21_op2(){
        return new Token("andOp", lexeme, fileManager.getLineNumber());
    }

    private Token e22() throws IOException, LexicalException {
        if(actualCharacter == '|'){
            buildLexeme();
            return e22_op2();
        }else{
            throw new LexicalException(lexeme, fileManager.getLineNumber(), fileManager.getLine(), fileManager.getCharNumber(), lexeme + " no es un simbolo valido");
        }
    }

    private Token e22_op2(){
        return new Token("orOp", lexeme, fileManager.getLineNumber());
    }

    private Token e23() {
        return new Token("modOp", lexeme, fileManager.getLineNumber());
    }

    private Token e24() {
        return new Token("eof", lexeme, fileManager.getLineNumber());
    }

    private void buildLexeme() throws IOException {
        updateLexeme();
        updateActualCharacter();
    }

    private void updateLexeme(){
        lexeme = lexeme + actualCharacter;
    }

    public void updateActualCharacter() throws IOException {
        actualCharacter = fileManager.getNextChar();
    }
}
