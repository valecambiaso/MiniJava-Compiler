package symbolTable;

public class TipoVoid extends TipoMetodo{

    public String getTypeName() {
        return "void";
    }

    public boolean isSubtype(TipoVoid ancestorType){return true;}
}
