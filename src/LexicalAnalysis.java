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
        final int string = 1, integer = 2, character = 3, hexadecimal = 4;
        final int id = 1, value = 2, comment = 3;

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
                    } else if (c == 32 || c == 11 || c == 8 || c == 13 || c == 9) {
                        actualState = 0;
                    }else if(c == '(' || c == ')' || c == ',' || c == '+' || c == '-' || c == '*'|| c == ';' || c == '%'
                            || c == '[' || c == ']' || c == '='){
                        lex+=c;
                        actualState = finalState;
                        System.out.println("Token Lido:"+lex);
                }

            }
        }

        if(!eof){
            if(symbolTable.search(lex.toLowerCase()) != -1){
                symbol = symbolTable.getSimb(lex);
                System.out.println(""+symbolTable.search(lex));
            }
        }
        return symbol;
    }
}
