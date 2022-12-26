package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;
import symbolTable.*;
import symbolTable.AST.acceso.*;
import symbolTable.AST.encadenado.NodoEncadenado;
import symbolTable.AST.encadenado.NodoMetodoEncadenado;
import symbolTable.AST.encadenado.NodoVarEncadenada;
import symbolTable.AST.expresion.*;
import symbolTable.AST.literal.*;
import symbolTable.AST.sentencia.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyntacticAnalyzer {
    private final LexicalAnalyzer lexicalAnalyzer;
    private Token actualToken;

    //Primeros
    private final List<String> primerosListaClases = Arrays.asList("kw_class", "kw_interface");
    private final List<String> primerosClaseConcreta = Arrays.asList("kw_class");
    private final List<String> primerosInterface = Arrays.asList("kw_interface");
    private final List<String> primerosMiembro = Arrays.asList("kw_public", "kw_private", "kw_static", "kw_boolean", "kw_char", "kw_int", "idClase", "kw_void");
    private final List<String> primerosEncabezadoMetodo = Arrays.asList("kw_static", "kw_boolean", "kw_char", "kw_int", "idClase", "kw_void");
    private final List<String> primerosAtributo = Arrays.asList("kw_public", "kw_private");
    private final List<String> primerosMetodo = Arrays.asList("kw_static", "kw_boolean", "kw_char", "kw_int", "idClase", "kw_void");
    private final List<String> primerosTipoPrimitivo = Arrays.asList("kw_boolean", "kw_char", "kw_int");
    private final List<String> primerosTipo = Arrays.asList("idClase","kw_boolean", "kw_char", "kw_int");
    private final List<String> primerosListaArgsFormales = Arrays.asList("idClase","kw_boolean", "kw_char", "kw_int");
    private final List<String> primerosSentencia = Arrays.asList("puntoComaPunt", "kw_this", "idMetVar", "kw_new", "idClase", "parentesisAbrirPunt", "kw_var", "kw_return", "kw_if", "kw_while", "llaveAbrirPunt");
    private final List<String> primerosAcceso = Arrays.asList("kw_this", "idMetVar", "kw_new", "idClase", "parentesisAbrirPunt");
    private final List<String> primerosVarLocal = Arrays.asList("kw_var");
    private final List<String> primerosReturn = Arrays.asList("kw_return");
    private final List<String> primerosIf = Arrays.asList("kw_if");
    private final List<String> primerosWhile = Arrays.asList("kw_while");
    private final List<String> primerosBloque = Arrays.asList("llaveAbrirPunt");
    private final List<String> primerosTipoDeAsignacion = Arrays.asList("igualAsignacion", "sumaAsignacion", "restaAsignacion");
    private final List<String> primerosExpresion = Arrays.asList("sumaOp","restaOp","notOp","kw_null","kw_true","kw_false","intLiteral","charLiteral","stringLiteral","kw_this","idMetVar","kw_new","idClase","parentesisAbrirPunt");
    private final List<String> primerosOperadorUnario = Arrays.asList("sumaOp","restaOp","notOp");
    private final List<String> primerosLiteral = Arrays.asList("kw_null","kw_true","kw_false","intLiteral","charLiteral","stringLiteral");
    private final List<String> primerosOperando = Arrays.asList("kw_null","kw_true","kw_false","intLiteral","charLiteral","stringLiteral","kw_this", "idMetVar", "kw_new", "idClase", "parentesisAbrirPunt");
    private final List<String> primerosOperadorBinario = Arrays.asList("orOp","andOp","igualOp","notIgualOp","mayorOp","menorOp","mayorIgualOp","menorIgualOp","sumaOp","restaOp","multOp","divOp","modOp");
    private final List<String> primerosAccesoThis = Arrays.asList("kw_this");
    private final List<String> primerosAccesoConstructor = Arrays.asList("kw_new");
    private final List<String> primerosAccesoMetodoEstatico = Arrays.asList("idClase");
    private final List<String> primerosExpresionParentizada = Arrays.asList("parentesisAbrirPunt");
    private final List<String> primerosArgsActuales = Arrays.asList("parentesisAbrirPunt");
    private final List<String> primerosListaExps = primerosExpresion;
    private final List<String> primerosEncadenadoOpt = Arrays.asList("puntoPunt");

    //Siguientes
    private final List<String> siguientesListaClases = Arrays.asList("eof");
    private final List<String> siguientesHeredaDe = Arrays.asList("kw_implements", "llaveAbrirPunt");
    private final List<String> siguientesImplementaA = Arrays.asList("llaveAbrirPunt");
    private final List<String> siguientesExtiendeA = siguientesImplementaA;
    private final List<String> siguientesListaTipoReferenciaF = siguientesImplementaA;
    private final List<String> siguientesListaMiembros = Arrays.asList("llaveCerrarPunt");
    private final List<String> siguientesListaEncabezados = siguientesListaMiembros;
    private final List<String> siguientesListaDecAtrsF = Arrays.asList("puntoComaPunt");
    private final List<String> siguientesEstaticoOpt = Arrays.asList("kw_boolean","kw_char","kw_int","idClase","kw_void");
    private final List<String> siguientesListaArgsFormalesOpt = Arrays.asList("parentesisCerrarPunt");
    private final List<String> siguientesListaArgsFormalesF = siguientesListaArgsFormalesOpt;
    private final List<String> siguientesListaSentencias = Arrays.asList("llaveCerrarPunt");
    private final List<String> siguientesExpresionOpt = Arrays.asList("puntoComaPunt");
    private final List<String> siguientesElse = Arrays.asList("puntoComaPunt","kw_this","idMetVar","kw_new","idClase","parentesisAbrirPunt","kw_var","kw_return","kw_if","kw_while","llaveAbrirPunt","llaveCerrarPunt","kw_else");
    private final List<String> siguientesExpresionR = Arrays.asList("puntoComaPunt","parentesisCerrarPunt","comaPunt");
    private final List<String> siguientesPrimarioF = Arrays.asList("puntoPunt","igualAsignacion","sumaAsignacion","restaAsignacion","orOp","andOp","igualOp","notIgualOp","mayorOp","menorOp","mayorIgualOp","menorIgualOp","sumaOp","restaOp","multOp","divOp","modOp","puntoComaPunt","parentesisCerrarPunt","comaPunt");
    private final List<String> siguientesListaExpsOpt = Arrays.asList("parentesisCerrarPunt");
    private final List<String> siguientesListaExpsF = Arrays.asList("parentesisCerrarPunt");
    private final List<String> siguientesEncadenadoOpt = Arrays.asList("igualAsignacion","sumaAsignacion","restaAsignacion","orOp","andOp","igualOp","notIgualOp","mayorOp","menorOp","mayorIgualOp","menorIgualOp","sumaOp","restaOp","multOp","divOp","modOp","puntoComaPunt","parentesisCerrarPunt","comaPunt");

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException, SyntacticException, SemanticException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        actualToken = lexicalAnalyzer.nextToken();
        inicial();
    }

    private void match(String tokenType, String expectedTokenName) throws LexicalException, IOException, SyntacticException {
        if(tokenType.equals(actualToken.getTokenType())){
            actualToken = lexicalAnalyzer.nextToken();
        } else {
            throw new SyntacticException(actualToken, expectedTokenName);
        }
    }

    private void inicial() throws LexicalException, SyntacticException, IOException, SemanticException {
        listaClases();
        match("eof", "eof");
    }

    private void listaClases() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(primerosListaClases.contains(actualToken.getTokenType())){
            clase();
            listaClases();
        }else if(siguientesListaClases.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, "clase, interfaz o fin del archivo");
        }
    }

    private void clase() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(primerosClaseConcreta.contains(actualToken.getTokenType())){
            claseConcreta();
        } else if(primerosInterface.contains(actualToken.getTokenType())){
            interface_();
        } else{
            throw new SyntacticException(actualToken, "interface o class");
        }
    }

    private void claseConcreta() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("kw_class", "class");
        Token idClaseToken = actualToken;
        match("idClase", "nombre de clase");
        ClaseConcreta newClass = new ClaseConcreta(idClaseToken);
        SymbolTable.actualClass = newClass;

        Token inheritsFrom = heredaDe();
        SymbolTable.actualClass.setInheritsFrom(inheritsFrom);

        implementaA();
        match("llaveAbrirPunt", "{");
        listaMiembros();
        match("llaveCerrarPunt", "}");

        SymbolTable.getSymbolTableInstance().insertClass(idClaseToken.getLexeme(), SymbolTable.actualClass);
    }

    private void interface_() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("kw_interface", "interface");
        Token idClaseToken = actualToken;
        match("idClase", "nombre de clase");
        Interfaz newInterface = new Interfaz(idClaseToken);
        SymbolTable.actualClass = newInterface;

        extiendeA();
        match("llaveAbrirPunt", "{");
        listaEncabezados();
        match("llaveCerrarPunt", "}");

        SymbolTable.getSymbolTableInstance().insertClass(idClaseToken.getLexeme(), SymbolTable.actualClass);
    }

    private Token heredaDe() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("kw_extends")){
            match("kw_extends", "extends");
            Token idClaseToken = actualToken;
            match("idClase", "nombre de clase");
            return idClaseToken;
        }else if(siguientesHeredaDe.contains(actualToken.getTokenType())){
            return new Token("idClase", "Object", 0);
        }else{
            throw new SyntacticException(actualToken, "extends, implements o {");
        }
    }

    private void implementaA() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getTokenType().equals("kw_implements")){
            match("kw_implements", "implements");
            listaTipoReferencia();
        }else if(siguientesImplementaA.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, "implements o {");
        }
    }

    private void extiendeA() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getTokenType().equals("kw_extends")){
            match("kw_extends", "extends");
            listaTipoReferencia();
        }else if(siguientesExtiendeA.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, "extends o {");
        }
    }

    private void listaTipoReferencia() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token idClaseToken = actualToken;
        match("idClase", "nombre de clase");
        Interfaz interfaz = new Interfaz(idClaseToken);
        SymbolTable.actualClass.addInterface(idClaseToken.getLexeme(),interfaz);
        listaTipoReferenciaF();
    }

    private void listaTipoReferenciaF() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getTokenType().equals("comaPunt")){
            match("comaPunt", ",");
            listaTipoReferencia();
        }else if(siguientesListaTipoReferenciaF.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, ", o {");
        }
    }

    private void listaMiembros() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(primerosMiembro.contains(actualToken.getTokenType())){
            miembro();
            listaMiembros();
        }else if(siguientesListaMiembros.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, "miembro o }");
        }
    }

    private void listaEncabezados() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(primerosEncabezadoMetodo.contains(actualToken.getTokenType())){
            encabezadoMetodo();
            match("puntoComaPunt", ";");
            listaEncabezados();
        }else if(siguientesListaEncabezados.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, "encabezado de metodo o }");
        }
    }

    private void miembro() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(primerosAtributo.contains(actualToken.getTokenType())){
            atributo();
        }else if(primerosMetodo.contains(actualToken.getTokenType())){
            metodo();
        }else{
            throw new SyntacticException(actualToken, "declaracion de atributo o metodo");
        }
    }

    private void atributo() throws LexicalException, SyntacticException, IOException, SemanticException {
        String visibility = visibilidad();
        Tipo type = tipo();
        listaDecAtrs(visibility, type);
        match("puntoComaPunt", ";");
    }

    private void metodo() throws LexicalException, SyntacticException, IOException, SemanticException {
        encabezadoMetodo();
        SymbolTable.actualMethod.addBlock(bloque());
    }

    private void encabezadoMetodo() throws LexicalException, SyntacticException, IOException, SemanticException {
        boolean isStatic = estaticoOpt();
        TipoMetodo methodType = tipoMetodo();
        Token idMetVarToken = actualToken;
        match("idMetVar", "nombre de metodo");
        Metodo newMethod = new Metodo(isStatic, methodType, idMetVarToken, SymbolTable.actualClass.getToken());
        SymbolTable.actualMethod = newMethod;
        argsFormales();
        SymbolTable.actualClass.insertMethod(idMetVarToken.getLexeme(),newMethod);
    }

    private String visibilidad() throws LexicalException, SyntacticException, IOException {
        String visibility;
        if(actualToken.getTokenType().equals("kw_public")){
            visibility = actualToken.getTokenType();
            match("kw_public", "public");
            return visibility;
        }else if(actualToken.getTokenType().equals("kw_private")){
            visibility = actualToken.getTokenType();
            match("kw_private", "private");
            return visibility;
        }else{
            throw new SyntacticException(actualToken, "public o private");
        }
    }

    private Tipo tipo() throws LexicalException, SyntacticException, IOException {
        if(primerosTipoPrimitivo.contains(actualToken.getTokenType())){
            return tipoPrimitivo();
        }else if(actualToken.getTokenType().equals("idClase")){
            Token idClaseToken = actualToken;
            match("idClase", "nombre de clase");
            return new TipoClase(idClaseToken);
        }else{
            throw new SyntacticException(actualToken, "tipo");
        }
    }

    private Tipo tipoPrimitivo() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("kw_boolean")){
            match("kw_boolean", "boolean");
            return new TipoBoolean();
        }else if(actualToken.getTokenType().equals("kw_char")){
            match("kw_char", "char");
            return new TipoChar();
        }else if(actualToken.getTokenType().equals("kw_int")){
            match("kw_int", "int");
            return new TipoInt();
        }else{
            throw new SyntacticException(actualToken, "tipo primitivo");
        }
    }

    private void listaDecAtrs(String visibility, Tipo type) throws LexicalException, SyntacticException, IOException, SemanticException {
        Token idMetVarToken = actualToken;
        match("idMetVar", "nombre de atributo");
        Atributo newAttribute = new Atributo(visibility,type,idMetVarToken,SymbolTable.actualClass.getToken());
        SymbolTable.actualClass.insertAttribute(idMetVarToken.getLexeme(),newAttribute);
        listaDecAtrsF(visibility, type);
    }

    private void listaDecAtrsF(String visibility, Tipo type) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getTokenType().equals("comaPunt")){
            match("comaPunt", ",");
            listaDecAtrs(visibility, type);
        }else if(siguientesListaDecAtrsF.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, ", o ;");
        }
    }

    private boolean estaticoOpt() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("kw_static")){
            match("kw_static", "static");
            return true;
        }else if(siguientesEstaticoOpt.contains(actualToken.getTokenType())){
            return false;
        }else{
            throw new SyntacticException(actualToken, "static o tipo de metodo");
        }
    }

    private TipoMetodo tipoMetodo() throws LexicalException, SyntacticException, IOException {
        if(primerosTipo.contains(actualToken.getTokenType())){
            Tipo type = tipo();
            return type;
        }else if(actualToken.getTokenType().equals("kw_void")){
            match("kw_void", "void");
            return new TipoVoid();
        }else{
            throw new SyntacticException(actualToken, "tipo de un metodo");
        }
    }

    private void argsFormales() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("parentesisAbrirPunt","(");
        listaArgsFormalesOpt();
        match("parentesisCerrarPunt",")");
    }

    private void listaArgsFormalesOpt() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(primerosListaArgsFormales.contains(actualToken.getTokenType())){
            listaArgsFormales();
        }else if(siguientesListaArgsFormalesOpt.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, ") o tipo de argumento formal");
        }
    }

    private void listaArgsFormales() throws LexicalException, SyntacticException, IOException, SemanticException {
        argFormal();
        listaArgsFormalesF();
    }

    private void listaArgsFormalesF() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getTokenType().equals("comaPunt")){
            match("comaPunt", ",");
            listaArgsFormales();
        }else if(siguientesListaArgsFormalesF.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, ", o )");
        }
    }

    private void argFormal() throws LexicalException, SyntacticException, IOException, SemanticException {
        Tipo type = tipo();
        Token idMetVarToken = actualToken;
        match("idMetVar", "nombre de variable");
        ParametroFormal newFormalParameter = new ParametroFormal(type,idMetVarToken);
        SymbolTable.actualMethod.insertFormalParameter(idMetVarToken.getLexeme(),newFormalParameter);
    }

    private NodoBloque bloque() throws LexicalException, SyntacticException, IOException {
        match("llaveAbrirPunt", "{");
        NodoBloque blockNode = new NodoBloque();
        listaSentencias(blockNode);
        match("llaveCerrarPunt", "}");
        return blockNode;
    }

    private void listaSentencias(NodoBloque blockNode) throws LexicalException, SyntacticException, IOException {
        if(primerosSentencia.contains(actualToken.getTokenType())){
            NodoSentencia sentence = sentencia();
            blockNode.addSentence(sentence);
            listaSentencias(blockNode);
        }else if(siguientesListaSentencias.contains(actualToken.getTokenType())){
            //epsilon
        }else{
            throw new SyntacticException(actualToken, "sentencia o }");
        }
    }

    private NodoSentencia sentencia() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("puntoComaPunt")){
            match("puntoComaPunt", ";");
            return new NodoSentenciaVacia();
        }else if(primerosAcceso.contains(actualToken.getTokenType())){
            NodoAcceso access = acceso();
            return sentenciaF(access);
        }else if(primerosVarLocal.contains(actualToken.getTokenType())){
            NodoSentencia sentence = varLocal();
            match("puntoComaPunt", ";");
            return sentence;
        }else if(primerosReturn.contains(actualToken.getTokenType())){
            NodoSentencia sentence = return_();
            match("puntoComaPunt", ";");
            return sentence;
        }else if(primerosIf.contains(actualToken.getTokenType())){
            return if_();
        }else if(primerosWhile.contains(actualToken.getTokenType())){
            return while_();
        }else if(primerosBloque.contains(actualToken.getTokenType())){
            return bloque();
        }else{
            throw new SyntacticException(actualToken, "sentencia");
        }
    }

    private NodoSentencia sentenciaF(NodoAcceso access) throws LexicalException, SyntacticException, IOException {
        if(primerosTipoDeAsignacion.contains(actualToken.getTokenType())){
            NodoAsignacion asig = tipoDeAsignacion();
            NodoExpresion exp = expresion();
            match("puntoComaPunt", ";");
            asig.addAccessAndExpression(access,exp);
            return asig;
        }else if(actualToken.getTokenType().equals("puntoComaPunt")){
            Token dotComaToken = actualToken;
            match("puntoComaPunt", ";");
            return new NodoLlamada(access,dotComaToken);
        }else{
            throw new SyntacticException(actualToken, "asignacion o llamada");
        }
    }

    private NodoAsignacion tipoDeAsignacion() throws LexicalException, SyntacticException, IOException {
        Token asigToken = actualToken;
        if(actualToken.getTokenType().equals("igualAsignacion")){
            match("igualAsignacion", "=");
            return new NodoAsignacion(asigToken);
        }else if(actualToken.getTokenType().equals("sumaAsignacion")){
            match("sumaAsignacion", "+=");
            return new NodoAsignacionCompuesta(asigToken);
        }else if(actualToken.getTokenType().equals("restaAsignacion")){
            match("restaAsignacion", "-=");
            return new NodoAsignacionCompuesta(asigToken);
        }else{
            throw new SyntacticException(actualToken, "asignacion");
        }
    }

    private NodoVarLocal varLocal() throws LexicalException, SyntacticException, IOException {
            match("kw_var", "var");
            Token localVarToken = actualToken;
            match("idMetVar", "nombre de variable");
            Token equalsToken = actualToken;
            match("igualAsignacion", "=");
            NodoExpresion expression = expresion();
            return new NodoVarLocal(localVarToken,equalsToken,expression);
    }

    private NodoReturn return_() throws LexicalException, SyntacticException, IOException {
        Token returnToken = actualToken;
        match("kw_return", "return");
        NodoReturn returnNode = new NodoReturn(returnToken);
        NodoExpresion returnExp = expresionOpt();
        returnNode.addExp(returnExp);
        return returnNode;
    }

    private NodoExpresion expresionOpt() throws LexicalException, SyntacticException, IOException {
        if(primerosExpresion.contains(actualToken.getTokenType())){
            return expresion();
        }else if(siguientesExpresionOpt.contains(actualToken.getTokenType())){
            return null;
        }else{
            throw new SyntacticException(actualToken, "expresion o ;");
        }
    }

    private NodoIf if_() throws LexicalException, SyntacticException, IOException {
        Token ifToken = actualToken;
        match("kw_if", "if");
        match("parentesisAbrirPunt", "(");
        NodoExpresion exp = expresion();
        match("parentesisCerrarPunt", ")");
        NodoSentencia sentence = sentencia();
        NodoIf ifNode = new NodoIf(ifToken, exp, sentence);
        ifNode.addElse(else_());
        return ifNode;
    }

    private NodoSentencia else_() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("kw_else")){
            match("kw_else", "else");
            return sentencia();
        }else if(siguientesElse.contains(actualToken.getTokenType())){
            return null;
        }else{
            throw new SyntacticException(actualToken, "else, } o sentencia");
        }
    }

    private NodoWhile while_() throws LexicalException, SyntacticException, IOException {
        Token whileToken = actualToken;
        match("kw_while", "while");
        match("parentesisAbrirPunt", "(");
        NodoExpresion exp = expresion();
        match("parentesisCerrarPunt", ")");
        NodoSentencia sentence = sentencia();
        return new NodoWhile(whileToken,exp,sentence);
    }

    private NodoExpresion expresion() throws LexicalException, SyntacticException, IOException {
        NodoExpresion expLeftSide = expresionUnaria();
        return expresionR(expLeftSide);
    }

    private NodoExpresion expresionR(NodoExpresion expLeftSide) throws LexicalException, SyntacticException, IOException {
        if(primerosOperadorBinario.contains(actualToken.getTokenType())){
            NodoExpBinaria binaryExp = operadorBinario();
            NodoExpresion expRightSide = expresionUnaria();
            binaryExp.addExp(expLeftSide,expRightSide);
            return expresionR(binaryExp);
        }else if(siguientesExpresionR.contains(actualToken.getTokenType())){
            return expLeftSide;
        }else{
            throw new SyntacticException(actualToken, "operador binario, ;, ) o ,");
        }
    }

    private NodoExpBinaria operadorBinario() throws LexicalException, SyntacticException, IOException {
        Token operator = actualToken;;
        if(actualToken.getTokenType().equals("orOp")){
            match("orOp", "||");
            return new NodoExpBinariaBooleana(operator);
        }else if(actualToken.getTokenType().equals("andOp")){
            match("andOp","&&");
            return new NodoExpBinariaBooleana(operator);
        }else if(actualToken.getTokenType().equals("igualOp")){
            match("igualOp", "==");
            return new NodoExpBinariaIgualdad(operator);
        }else if(actualToken.getTokenType().equals("notIgualOp")){
            match("notIgualOp","!=");
            return new NodoExpBinariaIgualdad(operator);
        }else if(actualToken.getTokenType().equals("menorOp")){
            match("menorOp", "<");
            return new NodoExpBinariaRelacional(operator);
        }else if(actualToken.getTokenType().equals("mayorOp")){
            match("mayorOp",">");
            return new NodoExpBinariaRelacional(operator);
        }else if(actualToken.getTokenType().equals("menorIgualOp")){
            match("menorIgualOp","<=");
            return new NodoExpBinariaRelacional(operator);
        }else if(actualToken.getTokenType().equals("mayorIgualOp")){
            match("mayorIgualOp",">=");
            return new NodoExpBinariaRelacional(operator);
        }else if(actualToken.getTokenType().equals("sumaOp")){
            match("sumaOp", "+");
            return new NodoExpBinariaMatematica(operator);
        }else if(actualToken.getTokenType().equals("restaOp")){
            match("restaOp", "-");
            return new NodoExpBinariaMatematica(operator);
        }else if(actualToken.getTokenType().equals("multOp")){
            match("multOp", "*");
            return new NodoExpBinariaMatematica(operator);
        }else if(actualToken.getTokenType().equals("divOp")){
            match("divOp","/");
            return new NodoExpBinariaMatematica(operator);
        }else if(actualToken.getTokenType().equals("modOp")){
            match("modOp","%");
            return new NodoExpBinariaMatematica(operator);
        }else{
            throw new SyntacticException(actualToken, "operador binario");
        }
    }

    private NodoExpresion expresionUnaria() throws LexicalException, SyntacticException, IOException {
        if(primerosOperadorUnario.contains(actualToken.getTokenType())){
            Token unaryOperator = operadorUnario();
            NodoOperando operand = operando();
            return new NodoExpUnaria(unaryOperator,operand);
        }else if(primerosOperando.contains(actualToken.getTokenType())){
            return operando();
        }else{
            throw new SyntacticException(actualToken, "expresion unaria");
        }
    }

    private Token operadorUnario() throws LexicalException, SyntacticException, IOException {
        Token operator = actualToken;
        if(actualToken.getTokenType().equals("sumaOp")){
            match("sumaOp", "+");
        }else if(actualToken.getTokenType().equals("restaOp")){
            match("restaOp", "-");
        }else if(actualToken.getTokenType().equals("notOp")){
            match("notOp", "!");
        }else{
            throw new SyntacticException(actualToken, "operador unario");
        }
        return operator;
    }

    private NodoOperando operando() throws LexicalException, SyntacticException, IOException {
        if(primerosLiteral.contains(actualToken.getTokenType())){
            return literal();
        }else if(primerosAcceso.contains(actualToken.getTokenType())){
            return acceso();
        }else{
            throw new SyntacticException(actualToken, "operando");
        }
    }

    private NodoOperando literal() throws LexicalException, SyntacticException, IOException {
        Token literalToken = actualToken;
        if(actualToken.getTokenType().equals("kw_null")){
            match("kw_null", "null");
            return new Null(literalToken);
        }else if(actualToken.getTokenType().equals("kw_true")){
            match("kw_true", "true");
            return new True(literalToken);
        }else if(actualToken.getTokenType().equals("kw_false")){
            match("kw_false", "false");
            return new False(literalToken);
        }else if(actualToken.getTokenType().equals("intLiteral")){
            match("intLiteral", "int");
            return new Entero(literalToken);
        }else if(actualToken.getTokenType().equals("charLiteral")){
            match("charLiteral", "char");
            return new Caracter(literalToken);
        }else if(actualToken.getTokenType().equals("stringLiteral")){
            match("stringLiteral", "string");
            return new Cadena(literalToken);
        }else{
            throw new SyntacticException(actualToken, "literal");
        }
    }

    private NodoAcceso acceso() throws LexicalException, SyntacticException, IOException {
        NodoAcceso primary = primario();
        NodoEncadenado enc = encadenadoOpt();
        primary.addEncadenado(enc);
        return primary;
    }

    private NodoAcceso primario() throws LexicalException, SyntacticException, IOException {
        if(primerosAccesoThis.contains(actualToken.getTokenType())){
            return accesoThis();
        }else if(primerosAccesoConstructor.contains(actualToken.getTokenType())){
            return accesoConstructor();
        }else if(primerosAccesoMetodoEstatico.contains(actualToken.getTokenType())){
            return accesoMetodoEstatico();
        }else if(primerosExpresionParentizada.contains(actualToken.getTokenType())){
            return expresionParentizada();
        }else if(actualToken.getTokenType().equals("idMetVar")){
            Token idMetVarToken = actualToken;
            match("idMetVar", "nombre de variable o metodo");
            return primarioF(idMetVarToken);
        }else{
            throw new SyntacticException(actualToken, "primario");
        }
    }

    private NodoAcceso primarioF(Token idMetVarToken) throws LexicalException, SyntacticException, IOException {
        if(primerosArgsActuales.contains(actualToken.getTokenType())){
            List<NodoExpresion> expressionList = argsActuales();
            return new NodoAccesoMetodo(idMetVarToken,expressionList);
        }else if(siguientesPrimarioF.contains(actualToken.getTokenType())){
            return new NodoAccesoVar(idMetVarToken);
        }else{
            throw new SyntacticException(actualToken, "asignacion, operador binario, ., ;, parentesis o ,");
        }
    }

    private NodoAccesoThis accesoThis() throws LexicalException, SyntacticException, IOException {
        Token thisToken = actualToken;
        match("kw_this","this");
        return new NodoAccesoThis(thisToken);
    }

    private NodoAccesoConstructor accesoConstructor() throws LexicalException, SyntacticException, IOException {
        match("kw_new","new");
        Token idClaseToken = actualToken;
        match("idClase","nombre de clase");
        match("parentesisAbrirPunt","(");
        match("parentesisCerrarPunt",")");
        return new NodoAccesoConstructor(idClaseToken);
    }

    private NodoExpresionParentizada expresionParentizada() throws LexicalException, SyntacticException, IOException {
        match("parentesisAbrirPunt","(");
        NodoExpresion expression = expresion();
        match("parentesisCerrarPunt",")");
        return new NodoExpresionParentizada(expression);
    }

    private NodoAccesoMetodoEstatico accesoMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        Token idClaseToken = actualToken;
        match("idClase","nombre de clase");
        match("puntoPunt",".");
        Token idMetVarToken = actualToken;
        match("idMetVar","nombre de metodo");
        List<NodoExpresion> expressionList = argsActuales();
        return new NodoAccesoMetodoEstatico(idClaseToken, idMetVarToken, expressionList);
    }

    private List<NodoExpresion> argsActuales() throws LexicalException, SyntacticException, IOException {
        match("parentesisAbrirPunt","(");
        List<NodoExpresion> argsActuales = listaExpsOpt();
        match("parentesisCerrarPunt",")");
        return argsActuales;
    }

    private List<NodoExpresion> listaExpsOpt() throws LexicalException, SyntacticException, IOException {
        if(primerosListaExps.contains(actualToken.getTokenType())){
            return listaExps();
        }else if(siguientesListaExpsOpt.contains(actualToken.getTokenType())){
            return new ArrayList<NodoExpresion>();
        }else{
            throw new SyntacticException(actualToken, "comienzo de expresion o )");
        }
    }

    private List<NodoExpresion> listaExps() throws LexicalException, SyntacticException, IOException {
        NodoExpresion expression = expresion();
        List<NodoExpresion> expressionList = listaExpsF();
        expressionList.add(0,expression);
        return expressionList;
    }

    private List<NodoExpresion> listaExpsF() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("comaPunt")){
            match("comaPunt", ",");
            return listaExps();
        }else if(siguientesListaExpsF.contains(actualToken.getTokenType())){
            return new ArrayList<NodoExpresion>();
        }else{
            throw new SyntacticException(actualToken, ", o )");
        }
    }

    private NodoEncadenado encadenadoOpt() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getTokenType().equals("puntoPunt")){
            match("puntoPunt", ".");
            Token idMetVar = actualToken;
            match("idMetVar", "nombre de variable o metodo");
            return encadenadoOptF(idMetVar);
        }else if(siguientesEncadenadoOpt.contains(actualToken.getTokenType())){
            return null;
        }else{
            throw new SyntacticException(actualToken, "asignacion, operador binario, ., ;, ) o ,");
        }
    }

    private NodoEncadenado encadenadoOptF(Token idMetVar) throws LexicalException, SyntacticException, IOException {
        if(primerosArgsActuales.contains(actualToken.getTokenType())){
            List<NodoExpresion> params = argsActuales();
            NodoMetodoEncadenado encadenadoMet = new NodoMetodoEncadenado(idMetVar,params);
            encadenadoMet.addEncadenado(encadenadoOpt());
            return encadenadoMet;
        }else{
            NodoVarEncadenada encadenadaVar = new NodoVarEncadenada(idMetVar);
            encadenadaVar.addEncadenado(encadenadoOpt());
            return encadenadaVar;
        }
    }

}