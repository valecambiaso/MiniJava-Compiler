package symbolTable;

public class TipoInt extends TipoPrimitivo{

    public String getTypeName() {
        return "int";
    }

    public boolean isSubtype(TipoInt ancestorType){return true;}
}
