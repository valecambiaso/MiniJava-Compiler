package symbolTable;

public abstract class TipoMetodo {

    public abstract String getTypeName();

    public void checkIfTypeExists() throws SemanticException {}

    public boolean isSubtype(TipoNull ancestorType){return false;}
    public boolean isSubtype(TipoClase ancestorType) throws SemanticException {return false;}
    public boolean isSubtype(TipoVoid ancestorType){return false;}
    public boolean isSubtype(TipoChar ancestorType){return false;}
    public boolean isSubtype(TipoInt ancestorType){return false;}
    public boolean isSubtype(TipoBoolean ancestorType){return false;}
}
