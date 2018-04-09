import java.io.BufferedReader;
import java.io.IOException;

class LexicalAnalysis {
    SymbolTable symbolTable = new SymbolTable();
    private boolean dev = false;
    private String lex = "";
    private char c;
    public boolean eof = false;
    public static int line = 1;

    public LexicalAnalysis() {
    }

    public Symbol tokenization(BufferedReader archive) throws IOException {
        Symbol symbol = null;
        int actualState = 0;
        int finalState = 17;
        lex = "";
        final int id = 1, value = 2;
        int tokenType = 0, valueType = 0;
        int flagHexa = 0;

        while (actualState != finalState) {
            switch (actualState) {
                case 0:
                    this.lex = "";
                    if (dev == false) {
                        c = (char) archive.read();
                    }
                    eof = false;
                    dev = false;

                    if (c == '\n' || c == 11) {
                        line++;
                        actualState = 0;
                    } else if (c == 65535) {
                        actualState = finalState;
                        lex += c;
                        eof = true;
                        dev = false;
                        archive.close();
                    } else if (c == 32 || c == 11 || c == 8 || c == 13 || c == 9) {
                        actualState = 0;
                    } else if (c == '(' || c == ')' || c == ',' || c == '+' || c == '-' || c == '*' || c == ';' || c == '%'
                            || c == '[' || c == ']' || c == '=') {
                        lex += c;
                        actualState = finalState;
                    } else if (c == '_') {
                        lex += c;
                        actualState = 1;
                    } else if (isLetter(c)) {
                        lex += c;
                        actualState = 3;
                    } else if (isDigit(c)) {
                        lex += c;
                        if (c == '0') {
                            actualState = 4;
                        } else {
                            actualState = 5;
                        }
                    } else if (c == '>') {
                        lex += c;
                        actualState = 8;
                    } else if (c == '<') {
                        lex += c;
                        actualState = 9;
                    } else if (c == 39) {
                        lex += c;
                        actualState = 10;
                    } else if (c == 34) {
                        lex += c;
                        actualState = 12;
                    } else if (c == '/') {
                        lex += c;
                        actualState = 13;
                    }
                    break;
                case 1:
                    c = (char) archive.read();
                    tokenType = id;
                    if (c == '_') {
                        lex += c;
                        actualState = 1;
                    } else if (isDigit(c) || isLetter(c)) {
                        lex += c;
                        actualState = 2;
                    } else {
                        actualState = finalState;
                        eof = true;
                        System.out.println(line + ":lexema nao identificado" + "[" + lex + "]");
                    }
                    break;
                case 2:
                    c = (char) archive.read();
                    tokenType = id;
                    if (isLetter(c) || isDigit(c) || c == '_') {
                        lex += c;
                        actualState = 2;
                    } else {
                        actualState = finalState;
                        dev = true;
                    }
                    break;
                case 3:
                    c = (char) archive.read();
                    tokenType = id;
                    if (isLetter(c) || isDigit(c) || c == '_') {
                        lex += c;
                        actualState = 3;
                    } else {
                        actualState = finalState;
                        dev = true;
                    }
                    break;
                case 4:
                    c = (char) archive.read();
                    if (isDigit(c) || isHexa(c)) {
                        if (isDigit(c)) {
                            flagHexa++;
                        }
                        lex += c;
                        actualState = 6;
                    }else{
                        actualState = 5;
                    }
                    break;
                case 5:
                    c = (char) archive.read();
                    tokenType = value;
                    if (isDigit(c)) {
                        lex += c;
                        actualState = 5;
                    } else {
                        actualState = finalState;
                        dev = true;
                    }
                    break;
                case 6:
                    c = (char) archive.read();
                    if (isDigit(c) || isHexa(c)) {
                        if (isDigit(c) && flagHexa == 1) {
                            flagHexa++;
                        }
                        lex += c;
                        actualState = 7;
                    } else if (flagHexa == 1) {
                        lex += c;
                        actualState = 5;
                    }
                    break;
                case 7:
                    c = (char) archive.read();
                    if (c == 'h') {
                        lex += c;
                        actualState = finalState;
                    } else if (flagHexa == 2) {
                        lex += c;
                        actualState = 5;
                    } else {
                        actualState = finalState;
                        System.exit(0);
                    }
                    break;
                case 8:
                    c = (char) archive.read();
                    if (c == '=') {
                        lex += c;
                        actualState = finalState;
                    } else {
                        actualState = finalState;
                        dev = true;
                    }
                    break;
                case 9:
                    c = (char) archive.read();
                    if (c == '>' || c == '=' || c == '-') {
                        lex += c;
                    } else {
                        actualState = finalState;
                        dev = true;
                    }
                    break;
                case 10:
                    c = (char) archive.read();
                    tokenType = value;
                    if (isDigit(c) || isLetter(c)) {
                        lex += c;
                        actualState = 11;
                    }
                    break;
                case 11:
                    c = (char) archive.read();
                    if (c == 39) {
                        lex += c;
                        tokenType = value;
                        actualState = finalState;
                    } else {
                        actualState = finalState;
                        System.out.println(line + ":lexema nao identificado" + "[" + lex + "]");
                        System.exit(0);
                    }
                    break;
                case 12:
                    c = (char) archive.read();
                    tokenType = value;
                    if (isDigit(c) || isLetter(c) || !isAlpha(c)) {
                        lex += c;
                        actualState = 12;
                    } else if (c == 34) {
                        lex+=c;
                        actualState = finalState;
                    } else {
                        actualState = finalState;
                        System.out.println(line + ":lexema nao identificado" + "[" + lex + "]");
                        System.exit(0);
                    }
                    break;
                case 13:
                    c = (char) archive.read();
                    if (c == '*') {
                        lex += c;
                        actualState = 14;
                    } else {
                        actualState = finalState;
                        dev = true;
                    }
                    break;

                case 14:
                    c = (char) archive.read();
                    if (c == '*') {
                        actualState = 15;
                    } else if (c == 13) {
                        actualState = 14;
                        line++;
                    } else if (c == -1 || c == 65535) {
                        eof = true;
                        System.err.println(line + ":Fim de arquivo nao esperado");
                        System.exit(0);
                    } else {
                        actualState = 14;
                    }
                    break;
                case 15:
                    c = (char) archive.read();
                    if (c == '/') {
                        actualState = 0;
                        lex = "";
                    } else if (c == '*') {
                        actualState = 15;
                    } else if (c == -1 || c == 65535) {
                        eof = true;
                        System.err.println(line + ":Fim de arquivo não esperado");
                        System.exit(0);
                    } else {
                        actualState = 14;
                    }
                    break;
            }
        }

        if (eof == false) {
            if (symbolTable.search(lex) != null) {
                symbol = symbolTable.getSimb(lex);
            } else {
                if (tokenType == id) {
                    lex = lex.toLowerCase();
                    symbolTable.insert(lex, symbolTable.ID);
                    symbol = symbolTable.getSimb(lex);
                } else if (tokenType == value) {
                    lex = lex.toLowerCase();
                    symbolTable.insert(lex, symbolTable.VALOR);
                    symbol = symbolTable.getSimb(lex);
                    System.out.println("Inseriu o valor"+symbol);
                }
            }
        }
        return symbol;
    }

    public static boolean isDigit(char letter) {
        boolean isDigit = false;
        if (letter >= '0' && letter <= '9') {
            isDigit = true;
        }
        return isDigit;
    }

    public static boolean isLetter(char letter) {
        boolean isLetter = false;
        if (letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z') {
            isLetter = true;
        }
        return isLetter;
    }

    public static boolean isHexa(char letter) {
        boolean isHexa = false;
        if (letter >= 'a' && letter <= 'f' || letter >= 'A' && letter <= 'F') {
            isHexa = true;
        }
        return isHexa;
    }

    public static boolean isAlpha(char letter){
        boolean isAlpha = false;
        if(letter == 36 || letter == 34 || letter == 13 || letter == 10 || letter == 11 || letter == 8){
            isAlpha = true;
        }
        return isAlpha;
    }
}
