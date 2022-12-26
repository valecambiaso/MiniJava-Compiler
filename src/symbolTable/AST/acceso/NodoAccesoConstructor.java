package symbolTable.AST.acceso;

import lexicalAnalyzer.Token;
import symbolTable.*;

public class NodoAccesoConstructor extends NodoAcceso{
    private Token classToken;
    private Clase newClass;

    public NodoAccesoConstructor(Token classToken){
        this.classToken = classToken;
    }


    @Override
    public TipoMetodo check() throws SemanticException {
        newClass = SymbolTable.getSymbolTableInstance().getClass(classToken.getLexeme());
        if(newClass == null){
            throw new SemanticException(classToken, "no se puede crear una instancia de la clase "+ classToken.getLexeme() + " ya que no existe dicha clase");
        }else if(!newClass.isConcreteClass()){
            throw new SemanticException(classToken, "no se puede crear una instancia de la interfaz " + classToken.getLexeme());
        }

        if(this.encadenado != null){
            return this.encadenado.check(new TipoClase(newClass.getToken()));
        }else{
            return new TipoClase(newClass.getToken());
        }
    }

    @Override
    public void generate() {
        SymbolTable.instructions.add("RMEM 1 ; Reserva memoria para el CIR");
        SymbolTable.instructions.add("PUSH " + (newClass.getAttributes().size() + 1) +" ; Se apila el tamaño del CIR");
        SymbolTable.instructions.add("PUSH simple_malloc ; Se apila la etiqueta que indica el inicio de la rutina");
        SymbolTable.instructions.add("CALL ; Se llama a la rutina");
        SymbolTable.instructions.add("DUP");
        SymbolTable.instructions.add("PUSH VT_" + newClass.getToken().getLexeme() + " ; Se apila la etiqueta que indica el inicio de la VT");
        SymbolTable.instructions.add("STOREREF 0 ; Se guarda referencia a la VT");
        SymbolTable.instructions.add("DUP");
        //No hay parametros
        SymbolTable.instructions.add("PUSH constructor_" + classToken.getLexeme() + " ; Se apila la etiqueta del constructor");
        SymbolTable.instructions.add("CALL ; Se llama al constructor");

        if(this.encadenado != null){
            this.encadenado.setLeftSide(this.isLeftSide);
            this.encadenado.generate();
        }
    }

    @Override
    public boolean isVariable() {
        if(this.encadenado != null){
            return this.encadenado.isVariable();
        }else{
            return false;
        }
    }

    @Override
    public boolean isMethodOrConstructor() {
        if(this.encadenado != null){
            return this.encadenado.isMethodOrConstructor();
        }else{
            return true;
        }
    }
}
