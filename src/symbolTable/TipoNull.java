package symbolTable;

public class TipoNull extends Tipo{
    @Override
    public String getTypeName() {
        return "null";
    }

    public boolean isSubtype(TipoNull ancestorType){return true;}
    public boolean isSubtype(TipoClase ancestorType){return true;}
}
