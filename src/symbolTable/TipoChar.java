package symbolTable;

public class TipoChar extends TipoPrimitivo{

    public String getTypeName() {
        return "char";
    }

    @Override
    public boolean isSubtype(TipoMetodo ancestorType) {
        return ancestorType.isSubtypeV(this);
    }

    public boolean isSubtypeV(TipoChar ancestorType){return true;}
}
