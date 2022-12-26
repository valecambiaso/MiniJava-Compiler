package symbolTable.AST.sentencia;

import symbolTable.SemanticException;

public class NodoSentenciaVacia extends NodoSentencia{

    @Override
    public void checkSentences() throws SemanticException {
        //Las sentencias vacias son inherentemente correctas.
    }

    @Override
    public void generate() {
        //No hay nada que generar
    }
}
