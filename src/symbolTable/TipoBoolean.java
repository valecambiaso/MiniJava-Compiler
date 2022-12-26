package symbolTable;

public class TipoBoolean extends TipoPrimitivo{

    public String getTypeName() {
        return "boolean";
    }

    @Override
    public boolean isSubtype(TipoMetodo ancestorType) {
        return ancestorType.isSubtypeV(this);
    }

    public boolean isSubtypeV(TipoBoolean ancestorType){return true;}
}
