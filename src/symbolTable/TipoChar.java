package symbolTable;

public class TipoChar extends TipoPrimitivo{

    public String getTypeName() {
        return "char";
    }

    public boolean isSubtype(TipoChar ancestorType){return true;}
}
