package symbolTable.AST.sentencia;

import lexicalAnalyzer.Token;
import symbolTable.AST.acceso.NodoAcceso;
import symbolTable.SemanticException;
import symbolTable.SymbolTable;
import symbolTable.TipoMetodo;

public class NodoLlamada extends NodoSentencia{
    private NodoAcceso access;
    private Token dotComaToken;
    private TipoMetodo accessType;

    public NodoLlamada(NodoAcceso access,Token dotComaToken){
        this.access = access;
        this.dotComaToken = dotComaToken;
    }

    @Override
    public void checkSentences() throws SemanticException {
        accessType = access.check();

        if(!access.isMethodOrConstructor()){
            throw new SemanticException(dotComaToken,"el acceso debe ser una llamada a un metodo/constructor o el ultimo elemento del encadenado del acceso debe ser una llamada");
        }
    }

    public void generate(){
        access.generate();
        if(!accessType.getTypeName().equals("void")){
            SymbolTable.instructions.add("POP ; como el acceso retorna algo diferente de void y no se usa, se desapila");
        }
    }
}
