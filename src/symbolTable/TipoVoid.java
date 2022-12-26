package symbolTable;

public class TipoVoid extends TipoMetodo{

    public String getTypeName() {
        return "void";
    }

    @Override
    public boolean isSubtype(TipoMetodo ancestorType) {
        return ancestorType.isSubtypeV(this);
    }

    public boolean isSubtypeV(TipoVoid ancestorType){return true;}
}
