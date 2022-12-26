package symbolTable;

public abstract class TipoMetodo {

    public abstract String getTypeName();

    public void checkIfTypeExists() throws SemanticException {}

    public abstract boolean isSubtype(TipoMetodo ancestorType) throws SemanticException;
    public boolean isSubtypeV(TipoNull ancestorType){return false;}
    public boolean isSubtypeV(TipoClase ancestorType) throws SemanticException {return false;}
    public boolean isSubtypeV(TipoVoid ancestorType){return false;}
    public boolean isSubtypeV(TipoChar ancestorType){return false;}
    public boolean isSubtypeV(TipoInt ancestorType){return false;}
    public boolean isSubtypeV(TipoBoolean ancestorType){return false;}
}
