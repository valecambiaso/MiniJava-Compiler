package symbolTable;

public class TipoInt extends TipoPrimitivo{

    public String getTypeName() {
        return "int";
    }

    @Override
    public boolean isSubtype(TipoMetodo ancestorType) {
        return ancestorType.isSubtypeV(this);
    }

    public boolean isSubtypeV(TipoInt ancestorType){return true;}
}
