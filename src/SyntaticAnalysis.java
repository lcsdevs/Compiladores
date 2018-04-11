/*
Luciano Junior
Pedro Rangel
 */

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
              // System.out.println("Token atual ct:"+actualSymbol.toString());
               if(actualSymbol.getSymbol() == tokEsperado){
                   actualSymbol = lexicalAnalysis.tokenization(in);
               }else{
                   if(lexicalAnalysis.eof){
                       System.err.println(lexicalAnalysis.line + ":Fim de arquivo nao esperado.");
                       System.exit(0);
                   }else{
                       System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + actualSymbol.getLexema());
                       System.exit(1);
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
            casaToken(symbolTable.FINAL);
            casaToken(symbolTable.ID);
            casaToken(symbolTable.ATRIB);
            if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                casaToken(symbolTable.MINUS);
            }
            casaToken(symbolTable.VALOR);
            casaToken(symbolTable.DOTCOMMA);
        }
    }

    //proc TIPO
    public void TIPO() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.INT) {
            casaToken(symbolTable.INT);
        } else if (actualSymbol.getSymbol() == symbolTable.CHAR) {
            casaToken(symbolTable.CHAR);
        }
    }

    //proc D1
    public void D1() throws Exception {
        casaToken(symbolTable.ID);
        if (actualSymbol.getSymbol() == symbolTable.ATRIB) {
            casaToken(symbolTable.ATRIB);
            if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                casaToken(symbolTable.MINUS);
            }
            casaToken(symbolTable.VALOR);
        } else if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
            casaToken(symbolTable.ACOLCHETES);
            casaToken(symbolTable.VALOR);
            casaToken(symbolTable.FCOLCHETES);
        }

        while (actualSymbol.getSymbol() == symbolTable.COMMA) {
            casaToken(symbolTable.COMMA);
            casaToken(symbolTable.ID);
            if (actualSymbol.getSymbol() == symbolTable.ATRIB) {
                casaToken(symbolTable.ATRIB);
                if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                    casaToken(symbolTable.MINUS);
                }
                casaToken(symbolTable.VALOR);
            } else if(actualSymbol.getSymbol() == symbolTable.ACOLCHETES){
                casaToken(symbolTable.ACOLCHETES);
                casaToken(symbolTable.VALOR);
                casaToken(symbolTable.FCOLCHETES);
            }
        }
    }

    //proc C
    public void C() throws Exception {
        while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR
                || actualSymbol.getSymbol() == symbolTable.IF || actualSymbol.getSymbol() == symbolTable.DOTCOMMA ||
                actualSymbol.getSymbol() == symbolTable.READLN || actualSymbol.getSymbol() == symbolTable.WRITE ||
                actualSymbol.getSymbol() == symbolTable.WRITELN) {
            if (actualSymbol.getSymbol() == symbolTable.FOR) {
                casaToken(symbolTable.FOR);
                casaToken(symbolTable.ID);
                casaToken(symbolTable.ATRIB);
                Exp();
                casaToken(symbolTable.TO);
                Exp();
                if (actualSymbol.getSymbol() == symbolTable.STEP) {
                    casaToken(symbolTable.STEP);
                    casaToken(symbolTable.VALOR);
                }
                casaToken(symbolTable.DO);
                if(actualSymbol.getSymbol() != symbolTable.DOTCOMMA){
                    C1();
                }else{
                    System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + actualSymbol.getLexema());
                    System.exit(1);
                }
            } else if (actualSymbol.getSymbol() == symbolTable.IF) {
                casaToken(symbolTable.IF);
                Exp();
                casaToken(symbolTable.THEN);
                if(actualSymbol.getSymbol() != symbolTable.DOTCOMMA){
                    C1();
                }else{
                    System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + actualSymbol.getLexema());
                    System.exit(1);
                }
                if (actualSymbol.getSymbol() == symbolTable.ELSE) {
                    casaToken(symbolTable.ELSE);
                    if(actualSymbol.getSymbol() != symbolTable.DOTCOMMA){
                        C1();
                    }else{
                        System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + actualSymbol.getLexema());
                        System.exit(1);
                    }
                }
            } else if (actualSymbol.getSymbol() == symbolTable.READLN) {
                casaToken(symbolTable.READLN);
                casaToken(symbolTable.APARENTESES);
                Exp();
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);
            } else if (actualSymbol.getSymbol() == symbolTable.WRITE) {
                casaToken(symbolTable.WRITE);
                casaToken(symbolTable.APARENTESES);
                Exp();
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    casaToken(symbolTable.COMMA);
                    Exp();
                }
               casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);
            } else if (actualSymbol.getSymbol() == symbolTable.WRITELN) {
                casaToken(symbolTable.WRITELN);
                casaToken(symbolTable.APARENTESES);
                Exp();
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    casaToken(symbolTable.COMMA);
                    Exp();
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);
            } else if (actualSymbol.getSymbol() == symbolTable.ID) {
                casaToken(symbolTable.ID);
                casaToken(symbolTable.ATRIB);
                Exp();
                casaToken(symbolTable.DOTCOMMA);
            } else if (actualSymbol.getSymbol() == symbolTable.DOTCOMMA) {
                casaToken(symbolTable.DOTCOMMA);
            }
        }
    }

    //proc C1
    public void C1() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.BEGIN) {
           // System.out.println("Token atual:"+actualSymbol.toString());
            casaToken(symbolTable.BEGIN);
            if(actualSymbol.getSymbol() != symbolTable.DOTCOMMA){
                C();
            }else{
                System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + actualSymbol.getLexema());
                System.exit(1);
            }
            while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR
                    ||actualSymbol.getSymbol() == symbolTable.IF || actualSymbol.getSymbol() == symbolTable.DOTCOMMA
                    || actualSymbol.getSymbol() == symbolTable.READLN || actualSymbol.getSymbol() == symbolTable.WRITE
                    || actualSymbol.getSymbol() == symbolTable.WRITELN) {
               // System.out.println("Token atual:"+actualSymbol.toString());
                C();
            }
            casaToken(symbolTable.END);
        } else {
            C();
        }
    }

    //Proc Exp
    public void Exp() throws Exception {
        ExpS();
        if (actualSymbol.getSymbol() == symbolTable.LESS || actualSymbol.getSymbol() == symbolTable.GREAT
                || actualSymbol.getSymbol() == symbolTable.LESSOREQUAL || actualSymbol.getSymbol() == symbolTable.GREATOREQUAL
                || actualSymbol.getSymbol() == symbolTable.EQUAL || actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
            if (actualSymbol.getSymbol() == symbolTable.LESS) {
                casaToken(symbolTable.LESS);
            } else if (actualSymbol.getSymbol() == symbolTable.GREAT) {
                casaToken(symbolTable.GREAT);
            } else if (actualSymbol.getSymbol() == symbolTable.LESSOREQUAL) {
                casaToken(symbolTable.LESSOREQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.GREATOREQUAL) {
                casaToken(symbolTable.GREATOREQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.EQUAL) {
                casaToken(symbolTable.EQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
                casaToken(symbolTable.DIFFERENT);
            }
            ExpS();
        }
    }

    //proc ExpS
    public void ExpS() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.PLUS) {
            casaToken(symbolTable.PLUS);
        } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
            casaToken(symbolTable.MINUS);
        }
        T();
        while (actualSymbol.getSymbol() == symbolTable.PLUS || actualSymbol.getSymbol() == symbolTable.MINUS
                || actualSymbol.getSymbol() == symbolTable.OR) {
            if (actualSymbol.getSymbol() == symbolTable.PLUS) {
                casaToken(symbolTable.PLUS);
            } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                casaToken(symbolTable.MINUS);
            } else {
                casaToken(symbolTable.OR);
            }
            T();
        }
    }

    //Proc T
    public void T() throws Exception {
        F();
        while (actualSymbol.getSymbol() == symbolTable.STAR || actualSymbol.getSymbol() == symbolTable.SLASH
                || actualSymbol.getSymbol() == symbolTable.PERCENT || actualSymbol.getSymbol() == symbolTable.AND) {
            if (actualSymbol.getSymbol() == symbolTable.STAR) {
                casaToken(symbolTable.STAR);
            } else if (actualSymbol.getSymbol() == symbolTable.SLASH) {
                casaToken(symbolTable.SLASH);
            } else if (actualSymbol.getSymbol() == symbolTable.PERCENT) {
                casaToken(symbolTable.PERCENT);
            } else {
                casaToken(symbolTable.AND);
            }
            F();
        }
    }

    //Proc F
    public void F() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.APARENTESES) {
            casaToken(symbolTable.APARENTESES);
            Exp();
            casaToken(symbolTable.FPARENTESES);
        } else if (actualSymbol.getSymbol() == symbolTable.NOT) {
            casaToken(symbolTable.NOT);
            F();
        } else if (actualSymbol.getSymbol() == symbolTable.VALOR) {
            casaToken(symbolTable.VALOR);
        } else {
            casaToken(symbolTable.ID);
            if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
                casaToken(symbolTable.ACOLCHETES);
                Exp();
                casaToken(symbolTable.FCOLCHETES);
            }
        }
    }
}


