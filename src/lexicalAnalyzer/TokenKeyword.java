package lexicalAnalyzer;

import java.util.Dictionary;
import java.util.Hashtable;

public class TokenKeyword {
    private final Dictionary<String, String> tokenKeywords = new Hashtable<>();

    public TokenKeyword(){
        tokenKeywords.put("class", "kw_class");
        tokenKeywords.put("public", "kw_public");
        tokenKeywords.put("void", "kw_void");
        tokenKeywords.put("if", "kw_if");
        tokenKeywords.put("this", "kw_this");
        tokenKeywords.put("interface", "kw_interface");
        tokenKeywords.put("private", "kw_private");
        tokenKeywords.put("boolean", "kw_boolean");
        tokenKeywords.put("else", "kw_else");
        tokenKeywords.put("new", "kw_new");
        tokenKeywords.put("extends", "kw_extends");
        tokenKeywords.put("static", "kw_static");
        tokenKeywords.put("char", "kw_char");
        tokenKeywords.put("while", "kw_while");
        tokenKeywords.put("null", "kw_null");
        tokenKeywords.put("implements", "kw_implements");
        tokenKeywords.put("int", "kw_int");
        tokenKeywords.put("return", "kw_return");
        tokenKeywords.put("true", "kw_true");
        tokenKeywords.put("var", "kw_var");
        tokenKeywords.put("false", "kw_false");
    }

    public String getTokenKeywordType(String tokenKeyword){
        return tokenKeywords.get(tokenKeyword);
    }
}
