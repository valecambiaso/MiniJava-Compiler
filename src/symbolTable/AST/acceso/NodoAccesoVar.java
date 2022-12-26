package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.*;
import symbolTable.AST.sentencia.NodoBloque;
import symbolTable.AST.sentencia.NodoVarLocal;

public class NodoAccesoVar extends NodoAcceso{
    private Token varToken;

    public NodoAccesoVar(Token varToken){
        this.varToken = varToken;
    }


    @Override
    public TipoMetodo check() throws SemanticException {
        TipoMetodo varType;
        Atributo atr = SymbolTable.actualClass.getAttribute(varToken.getLexeme());
        NodoVarLocal localVar = null;
        for(NodoBloque block : SymbolTable.actualBlocks){
            if(block.getLocalVar(varToken.getLexeme()) != null){
                localVar = block.getLocalVar(varToken.getLexeme());
                break;
            }
        }

        if(SymbolTable.actualMethod.isFormalParameter(varToken.getLexeme())){
            varType = SymbolTable.actualMethod.getFormalParameter(varToken.getLexeme()).getFormalParameterType();
        }else if(localVar != null){
            varType = localVar.getVarType();
        }else if(atr != null && (atr.getClassCont().getLexeme().equals(SymbolTable.actualClass.getToken().getLexeme()) || atr.getAttributeVisibility().equals("kw_public"))){
            if(!SymbolTable.actualMethod.isStatic())
                varType = SymbolTable.actualClass.getAttribute(varToken.getLexeme()).getAttributeType();
            else
                throw new SemanticException(varToken, "no se puede acceder a un atributo desde un metodo estatico");
        }else{
            throw new SemanticException(varToken, varToken.getLexeme() + " no es un parametro formal, variable local o atributo visible");
        }

        if(this.encadenado != null){
            return this.encadenado.check(varType);
        }else{
            return varType;
        }
    }

    @Override
    public void generate() {
        Atributo classAttribute = SymbolTable.actualClass.getAttribute(varToken.getLexeme());
        ParametroFormal formalParameter = SymbolTable.actualMethod.getFormalParameter(varToken.getLexeme());
        NodoVarLocal localVar = null;
        for (NodoBloque b : SymbolTable.actualBlocks){
            if(b.getLocalVar(varToken.getLexeme()) != null){
                localVar = b.getLocalVar(varToken.getLexeme());
                break;
            }
        }
        if(formalParameter != null){
            if(!isLeftSide || encadenado != null){
                SymbolTable.instructions.add("LOAD " + formalParameter.getParameterOffset() + " ; Se busca el parametro " + formalParameter.getToken().getLexeme());
            }else{
                SymbolTable.instructions.add("STORE " + formalParameter.getParameterOffset() + " ; Se almacena en el parametro " + formalParameter.getToken().getLexeme());
            }
        }else if(localVar != null){
            if(!isLeftSide || encadenado != null){
                SymbolTable.instructions.add("LOAD " + localVar.getLocalVarOffset() + " ; Se busca la variable local " + localVar.getLocalVarToken().getLexeme());
            }else{
                SymbolTable.instructions.add("STORE " + localVar.getLocalVarOffset() + " ; Se almacena la variable local " + localVar.getLocalVarToken().getLexeme());
            }
        }else if(classAttribute != null){
            SymbolTable.instructions.add("LOAD 3 ; Se apila el this en la pila");
            if(!isLeftSide || encadenado != null){
                SymbolTable.instructions.add("LOADREF " + classAttribute.getOffset() + " ; Se busca el atributo " + classAttribute.getToken().getLexeme());
            }else{
                SymbolTable.instructions.add("SWAP");
                SymbolTable.instructions.add("STOREREF " + classAttribute.getOffset() + " ; Se almacena en el atributo " + classAttribute.getToken().getLexeme());
            }
        }

        if(this.encadenado != null){
            this.encadenado.setLeftSide(this.isLeftSide);
            this.encadenado.generate();
        }
    }

    @Override
    public boolean isVariable() {
        if(this.encadenado != null){
            return this.encadenado.isVariable();
        }else{
            return true;
        }
    }

    @Override
    public boolean isMethodOrConstructor() {
        if(this.encadenado != null){
            return this.encadenado.isMethodOrConstructor();
        }else{
            return false;
        }
    }
}
