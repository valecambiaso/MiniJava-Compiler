package symbolTable;

public class TipoBoolean extends TipoPrimitivo{

    public String getTypeName() {
        return "boolean";
    }

    public boolean isSubtype(TipoBoolean ancestorType){return true;}
}
