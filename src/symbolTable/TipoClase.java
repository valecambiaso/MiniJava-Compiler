package symbolTable;

import lexicalAnalyzer.Token;

import java.util.HashMap;

public class TipoClase extends Tipo{
    protected Token idClaseToken;

    public TipoClase(Token idClaseToken){
       this.idClaseToken = idClaseToken;
    }

    public String getTypeName() {
        return idClaseToken.getLexeme();
    }

    public void checkIfTypeExists() throws SemanticException {
        if(SymbolTable.getSymbolTableInstance().getClass(idClaseToken.getLexeme()) == null){
            throw new SemanticException(idClaseToken, "no pueden haber tipos de clase " + idClaseToken.getLexeme() + " ya que no fue declarada");
        }
    }

    public boolean isSubtype(TipoClase ancestorType) throws SemanticException {
        Clase classAncestor = SymbolTable.getSymbolTableInstance().getClass(ancestorType.getTypeName());
        if(classAncestor.isConcreteClass()){
            return ancestorType.isSubtypeConcreteClass(this);
        }else{
            return ancestorType.isSubtypeInterface(this);
        }
    }

    public boolean isSubtypeConcreteClass(TipoClase subtype) throws SemanticException {
        if(this.idClaseToken.getLexeme().equals(subtype.getTypeName())){
            return true;
        }else{
            Clase classSubtype = SymbolTable.getSymbolTableInstance().getClass(subtype.getTypeName());
            if(classSubtype.getInheritsFrom() == null){
                return false;
            }else{
                return isSubtypeConcreteClass(new TipoClase(classSubtype.getInheritsFrom()));
            }
        }
    }

    public boolean isSubtypeInterface(TipoClase subtype) throws SemanticException{
        if(this.idClaseToken.getLexeme().equals(subtype.getTypeName())){
            return true;
        }else{
            Clase classSubtype = SymbolTable.getSymbolTableInstance().getClass(subtype.getTypeName());
            HashMap<String,Interfaz> interfaces = classSubtype.getInterfaces();
            for(Interfaz interfaz:interfaces.values()){
                if(isSubtypeInterface(new TipoClase(interfaz.getToken())))
                    return true;
            }
            Token fatherClassToken = classSubtype.getInheritsFrom();
            if (fatherClassToken != null){
                return isSubtypeInterface(new TipoClase(fatherClassToken));
            }else{
                return false;
            }
        }
    }
}
