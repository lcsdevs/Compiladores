import java.io.BufferedReader;
import java.io.IOException;

public class SyntaticAnalysis {
    LexicalAnalysis lexicalAnalysis;
    SymbolTable symbolTable;
    Symbol actualSymbol;
    BufferedReader in;
   public SyntaticAnalysis(){
        lexicalAnalysis = new LexicalAnalysis();
        symbolTable = new SymbolTable();
    }

    public void startParsing(BufferedReader archive) throws Exception{
        actualSymbol = lexicalAnalysis.tokenization(archive);
        this.in = archive;
        if(actualSymbol == null){
            actualSymbol = lexicalAnalysis.tokenization(archive);
        }
        S();
    }


    //casaToken
    public void casaToken(byte tokEsperado) throws IOException {
        try {
           if(actualSymbol != null){
               System.out.println("Token atual ct:"+actualSymbol.toString());
               if(actualSymbol.getSymbol() == tokEsperado){
                   actualSymbol = lexicalAnalysis.tokenization(in);
               }else{
                   if(lexicalAnalysis.eof){
                       System.err.println(lexicalAnalysis.line + ":Fim de arquivo nao esperado.");
                       System.exit(0);
                   }else{
                       System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + actualSymbol.getLexema());
                       System.exit(0);
                   }
               }
           }
        } catch (NullPointerException e) {
            System.err.println("Casa Token: " + e.toString());
            System.exit(1);
        }
    }


    //proc S
    public void S() throws Exception {
        while (actualSymbol.getSymbol() == symbolTable.INT || actualSymbol.getSymbol() == symbolTable.CHAR
                || actualSymbol.getSymbol() == symbolTable.FINAL) {
            D();
        }

        while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR || actualSymbol.getSymbol() == symbolTable.IF
                || actualSymbol.getSymbol() == symbolTable.DOTCOMMA || actualSymbol.getSymbol() == symbolTable.READLN ||
                actualSymbol.getSymbol() == symbolTable.WRITE || actualSymbol.getSymbol() == symbolTable.WRITELN
                ) {
            C();
        }
    }

    //proc D
    public void D() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.INT || actualSymbol.getSymbol() == symbolTable.CHAR) {
            TIPO();
            D1();
            while (actualSymbol.getSymbol() == symbolTable.ID) {
                D1();
            }
            casaToken(symbolTable.DOTCOMMA);
        } else if (actualSymbol.getSymbol() == symbolTable.FINAL) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.FINAL);
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.ID);
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.ATRIB);
            System.out.println("Token atual:"+actualSymbol.toString());
            if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.MINUS);
            }
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.VALOR);
            System.out.println("Token atual1:"+actualSymbol.toString());
            casaToken(symbolTable.DOTCOMMA);
            System.out.println("Token atual:"+actualSymbol.toString());
        }
    }

    //proc TIPO
    public void TIPO() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.INT) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.INT);
            System.out.println("Token atual:"+actualSymbol.toString());
        } else if (actualSymbol.getSymbol() == symbolTable.CHAR) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.CHAR);
            System.out.println("Token atual:"+actualSymbol.toString());
        }
    }

    //proc D1
    public void D1() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        casaToken(symbolTable.ID);
        System.out.println("Token atual:"+actualSymbol.toString());
        if (actualSymbol.getSymbol() == symbolTable.ATRIB) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.ATRIB);
            System.out.println("Token atual:"+actualSymbol.toString());
            if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.MINUS);
                System.out.println("Token atual:"+actualSymbol.toString());
            }
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.VALOR);
            System.out.println("Token atual:"+actualSymbol.toString());
        } else if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.ACOLCHETES);
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.VALOR);
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.FCOLCHETES);
            System.out.println("Token atual:"+actualSymbol.toString());
        }
        while (actualSymbol.getSymbol() == symbolTable.COMMA) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.COMMA);
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.ID);
            System.out.println("Token atual:"+actualSymbol.toString());
            if (actualSymbol.getSymbol() == symbolTable.ATRIB) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ATRIB);
                System.out.println("Token atual:"+actualSymbol.toString());
                if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                    System.out.println("Token atual:"+actualSymbol.toString());
                    casaToken(symbolTable.MINUS);
                    System.out.println("Token atual:"+actualSymbol.toString());
                }
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.VALOR);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ACOLCHETES);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.VALOR);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.FCOLCHETES);
                System.out.println("Token atual:"+actualSymbol.toString());
            }
        }
    }

    //proc C
    public void C() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR
                || actualSymbol.getSymbol() == symbolTable.IF || actualSymbol.getSymbol() == symbolTable.DOTCOMMA ||
                actualSymbol.getSymbol() == symbolTable.READLN || actualSymbol.getSymbol() == symbolTable.WRITE ||
                actualSymbol.getSymbol() == symbolTable.WRITELN) {
            if (actualSymbol.getSymbol() == symbolTable.FOR) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.FOR);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ID);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ATRIB);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.TO);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                if (actualSymbol.getSymbol() == symbolTable.STEP) {
                    System.out.println("Token atual:"+actualSymbol.toString());
                    casaToken(symbolTable.STEP);
                    System.out.println("Token atual:"+actualSymbol.toString());
                    casaToken(symbolTable.VALOR);
                    System.out.println("Token atual:"+actualSymbol.toString());
                }
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.DO);
                System.out.println("Token atual:"+actualSymbol.toString());
                C1();
            } else if (actualSymbol.getSymbol() == symbolTable.IF) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.IF);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.THEN);
                System.out.println("Token atual:"+actualSymbol.toString());
                C1();
                System.out.println("Token atual:"+actualSymbol.toString());
                if (actualSymbol.getSymbol() == symbolTable.ELSE) {
                    System.out.println("Token atual:"+actualSymbol.toString());
                    casaToken(symbolTable.ELSE);
                    System.out.println("Token atual:"+actualSymbol.toString());
                    C1();
                    System.out.println("Token atual:"+actualSymbol.toString());
                }
            } else if (actualSymbol.getSymbol() == symbolTable.READLN) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.READLN);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.APARENTESES);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ID);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.FPARENTESES);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.DOTCOMMA);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.WRITE) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.WRITE);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.APARENTESES);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    System.out.println("Token atual:"+actualSymbol.toString());
                    casaToken(symbolTable.COMMA);
                    System.out.println("Token atual:"+actualSymbol.toString());
                    Exp();
                    System.out.println("Token atual:"+actualSymbol.toString());
                }
                casaToken(symbolTable.FPARENTESES);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.DOTCOMMA);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.WRITELN) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.WRITELN);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.APARENTESES);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    System.out.println("Token atual:"+actualSymbol.toString());
                    casaToken(symbolTable.COMMA);
                    System.out.println("Token atual:"+actualSymbol.toString());
                    Exp();
                    System.out.println("Token atual:"+actualSymbol.toString());
                }
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.FPARENTESES);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.DOTCOMMA);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.ID) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ID);
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ATRIB);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.DOTCOMMA);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.DOTCOMMA) {
                System.out.println("Token atual 1:"+actualSymbol.toString());
                casaToken(symbolTable.DOTCOMMA);
                System.out.println("Token atual 2:"+actualSymbol.toString());
            }
        }
    }

    //proc C1
    public void C1() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        if (actualSymbol.getSymbol() == symbolTable.BEGIN) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.BEGIN);
            System.out.println("Token atual:"+actualSymbol.toString());
            C();
            System.out.println("Token atual:"+actualSymbol.toString());
            while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR
                    ||actualSymbol.getSymbol() == symbolTable.IF || actualSymbol.getSymbol() == symbolTable.DOTCOMMA
                    || actualSymbol.getSymbol() == symbolTable.READLN || actualSymbol.getSymbol() == symbolTable.WRITE
                    || actualSymbol.getSymbol() == symbolTable.WRITELN) {
                System.out.println("Token atual:"+actualSymbol.toString());
                C();
                System.out.println("Token atual:"+actualSymbol.toString());
            }
            casaToken(symbolTable.END);
            System.out.println("Token atual:"+actualSymbol.toString());
        } else {
            System.out.println("Token atual:"+actualSymbol.toString());
            C();
            System.out.println("Token atual:"+actualSymbol.toString());
        }
    }

    //Proc Exp
    public void Exp() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        ExpS();
        System.out.println("Token atual:"+actualSymbol.toString());
        if (actualSymbol.getSymbol() == symbolTable.LESS || actualSymbol.getSymbol() == symbolTable.GREAT
                || actualSymbol.getSymbol() == symbolTable.LESSOREQUAL || actualSymbol.getSymbol() == symbolTable.GREATOREQUAL
                || actualSymbol.getSymbol() == symbolTable.EQUAL || actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
            if (actualSymbol.getSymbol() == symbolTable.LESS) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.LESS);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.GREAT) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.GREAT);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.LESSOREQUAL) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.LESSOREQUAL);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.GREATOREQUAL) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.GREATOREQUAL);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.EQUAL) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.EQUAL);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.DIFFERENT);
                System.out.println("Token atual:"+actualSymbol.toString());
            }
            ExpS();
        }
    }

    //proc ExpS
    public void ExpS() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        if (actualSymbol.getSymbol() == symbolTable.PLUS) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.PLUS);
            System.out.println("Token atual:"+actualSymbol.toString());
        } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.MINUS);
            System.out.println("Token atual:"+actualSymbol.toString());
        }
        T();
        while (actualSymbol.getSymbol() == symbolTable.PLUS || actualSymbol.getSymbol() == symbolTable.MINUS
                || actualSymbol.getSymbol() == symbolTable.OR) {
            if (actualSymbol.getSymbol() == symbolTable.PLUS) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.PLUS);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.MINUS);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.OR);
                System.out.println("Token atual:"+actualSymbol.toString());
            }
            T();
        }
    }

    //Proc T
    public void T() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        F();
        System.out.println("Token atual:"+actualSymbol.toString());
        while (actualSymbol.getSymbol() == symbolTable.STAR || actualSymbol.getSymbol() == symbolTable.SLASH
                || actualSymbol.getSymbol() == symbolTable.PERCENT || actualSymbol.getSymbol() == symbolTable.AND) {
            if (actualSymbol.getSymbol() == symbolTable.STAR) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.STAR);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.SLASH) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.SLASH);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else if (actualSymbol.getSymbol() == symbolTable.PERCENT) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.PERCENT);
                System.out.println("Token atual:"+actualSymbol.toString());
            } else {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.AND);
                System.out.println("Token atual:"+actualSymbol.toString());
            }
            F();
        }
    }

    //Proc F
    public void F() throws Exception {
        System.out.println("Token atual:"+actualSymbol.toString());
        if (actualSymbol.getSymbol() == symbolTable.APARENTESES) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.APARENTESES);
            System.out.println("Token atual:"+actualSymbol.toString());
            Exp();
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.FPARENTESES);
            System.out.println("Token atual:"+actualSymbol.toString());
        } else if (actualSymbol.getSymbol() == symbolTable.NOT) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.NOT);
            System.out.println("Token atual:"+actualSymbol.toString());
            F();
            System.out.println("Token atual:"+actualSymbol.toString());
        } else if (actualSymbol.getSymbol() == symbolTable.VALOR) {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.VALOR);
            System.out.println("Token atual:"+actualSymbol.toString());
        } else {
            System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.ID);
            System.out.println("Token atual:"+actualSymbol.toString());
            if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.ACOLCHETES);
                System.out.println("Token atual:"+actualSymbol.toString());
                Exp();
                System.out.println("Token atual:"+actualSymbol.toString());
                casaToken(symbolTable.FCOLCHETES);
                System.out.println("Token atual:"+actualSymbol.toString());
            }
        }
    }
}


