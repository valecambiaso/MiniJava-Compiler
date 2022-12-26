package symbolTable;

public class TipoNull extends Tipo{
    @Override
    public String getTypeName() {
        return "null";
    }

    @Override
    public boolean isSubtype(TipoMetodo ancestorType) {
        return ancestorType instanceof TipoClase;
    }

    public boolean isSubtypeV(TipoNull ancestorType){return true;}
    public boolean isSubtypeV(TipoClase ancestorType){return true;}
}
