/*
Luciano Junior
Pedro Rangel
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Symbol {
    String lexema;
    byte token;

    public int getEndereco() {
        return endereco;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    int endereco;
    private String classe="";
    private String tipo="";
    int tamanho;

    public Symbol(){}
    public Symbol(String lexema, byte token, int endereco) {
        this.lexema = lexema;
        this.token = token;
        this.endereco = endereco;
    }

    public Symbol(String lexema,byte token,int endereco,String tipo){
        this.lexema = lexema;
        this.token = token;
        this.endereco = endereco;
        this.tipo = tipo;
    }

    public Symbol(String lexema,byte token,String tipo,String classe,int tamanho){
        this.lexema = lexema;
        this.token = token;
        this.tamanho = tamanho;
        this.tipo = tipo;
        this.classe = classe;
    }

    public int getTamanho(){
        return this.tamanho;
    }

    public byte getSymbol(){
        return this.token;
    }

    public String getLexema(){
        return this.lexema;
    }
    public String toString() {
        return this.lexema;
    }

    public String getClasse(){
        return  this.classe;
    }

    public void setClasse(String classe){
        this.classe = classe;
    }

    public String getTipo(){
        return  this.tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }


}

public class SymbolTable {
    public HashMap<String, Symbol> table = new HashMap<>();

    public static int pos = -1;

    final byte OUT = -1;

    final byte ID = 0;
    final byte VALOR = 1;
    final byte CHAR = 2;
    final byte INT = 3;
    final byte FINAL = 4;
    final byte FOR = 5;
    final byte IF = 6;
    final byte EQUAL = 7;
    final byte ELSE = 8;
    final byte AND = 9;
    final byte OR = 10;
    final byte NOT = 11;
    final byte ATRIB = 12;
    final byte TO = 13;
    final byte APARENTESES = 14;
    final byte FPARENTESES = 15;
    final byte LESS = 16;
    final byte GREAT = 17;
    final byte DIFFERENT = 18;
    final byte GREATOREQUAL = 19;
    final byte LESSOREQUAL = 20;
    final byte COMMA = 21;
    final byte PLUS = 22;
    final byte MINUS = 23;
    final byte STAR = 24;
    final byte SLASH = 25;
    final byte DOTCOMMA = 26;
    final byte BEGIN = 27;
    final byte END = 28;
    final byte THEN = 29;
    final byte READLN = 30;
    final byte STEP = 31;
    final byte WRITE = 32;
    final byte WRITELN = 33;
    final byte PERCENT = 34;
    final byte ACOLCHETES = 35;
    final byte FCOLCHETES = 36;
    final byte DO = 37;
    final byte EOF = 39;

    public SymbolTable() {
        table.put("id", new Symbol("id", ID, ++pos));
        table.put("valor", new Symbol("valor", VALOR, ++pos));
        table.put("char", new Symbol("char", CHAR, ++pos));
        table.put("int", new Symbol("int", INT, ++pos));
        table.put("final", new Symbol("final", FINAL, ++pos));
        table.put("for", new Symbol("for", FOR, ++pos));
        table.put("if", new Symbol("if", IF, ++pos));
        table.put("=", new Symbol("=", EQUAL, ++pos));
        table.put("else", new Symbol("else", ELSE, ++pos));
        table.put("and", new Symbol("and", AND, ++pos));
        table.put("or", new Symbol("or", OR, ++pos));
        table.put("not", new Symbol("not", NOT, ++pos));
        table.put("<-", new Symbol("<-", ATRIB, ++pos));
        table.put("to", new Symbol("to", TO, ++pos));
        table.put("(", new Symbol("(", APARENTESES, ++pos));
        table.put(")", new Symbol(")", FPARENTESES, ++pos));
        table.put("<", new Symbol("<", LESS, ++pos));
        table.put(">", new Symbol(">", GREAT, ++pos));
        table.put("<>", new Symbol("<>", DIFFERENT, ++pos));
        table.put(">=", new Symbol(">=", GREATOREQUAL, ++pos));
        table.put("<=", new Symbol("<=", LESSOREQUAL, ++pos));
        table.put(",", new Symbol(",", COMMA, ++pos));
        table.put("+", new Symbol("+", PLUS, ++pos));
        table.put("-", new Symbol("-", MINUS, ++pos));
        table.put("*", new Symbol("*", STAR, ++pos));
        table.put("/", new Symbol("/", SLASH, ++pos));
        table.put(";", new Symbol(";", DOTCOMMA, ++pos));
        table.put("begin", new Symbol("begin", BEGIN, ++pos));
        table.put("end", new Symbol("end", END, ++pos));
        table.put("then", new Symbol("then", THEN, ++pos));
        table.put("readln", new Symbol("readln", READLN, ++pos));
        table.put("step", new Symbol("step", STEP, ++pos));
        table.put("write", new Symbol("write", WRITE, ++pos));
        table.put("writeln", new Symbol("writeln", WRITELN, ++pos));
        table.put("%", new Symbol("%", PERCENT, ++pos));
        table.put("[", new Symbol("[", ACOLCHETES, ++pos));
        table.put("]", new Symbol("]", FCOLCHETES, ++pos));
        table.put("do", new Symbol("do", DO, ++pos));
        table.put("eof", new Symbol ( "eof" , EOF , ++pos ));

    }

    public Integer search(String lex) {
        //lex = lex.toLowerCase();
        Symbol s = table.get(lex);
        if (s != null) {
            return s.endereco;
        } else {
            //System.out.println("Simbolo Inexistente");
            return null;
        }
    }


    public Symbol getSimb(String lex) {
        //System.out.println(lex);
       // lex = lex.toLowerCase();
        Symbol s = table.get(lex);
        if (s == null) {
            return (getSimb("k"));
        } else {
            return s;
        }
    }


    public int insert(String lex,byte tipo) {
       // lex = lex.toLowerCase();
        table.put(lex, new Symbol(lex,tipo, ++pos));
        return search(lex);
    }

    public int insert(String lex,byte tipo,String tipoSemantico) {
        // lex = lex.toLowerCase();
        table.put(lex, new Symbol(lex,tipo, ++pos,tipoSemantico));
        return search(lex);
    }

    public void printMap(){
        int count = 0;
        Iterator it = table.entrySet().iterator();
        while (it.hasNext()) {
            count++;
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = '" + pair.getValue().toString() + "'");
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("Numero de itens na tabela = " + count);
    }
}
