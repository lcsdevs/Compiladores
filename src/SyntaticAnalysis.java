/*
Luciano Junior
Pedro Rangel
 */

import java.io.BufferedReader;
import java.io.IOException;

public class SyntaticAnalysis {
    /*-Principal Inicio-*/
    LexicalAnalysis lexicalAnalysis;
    SymbolTable symbolTable;
    Symbol actualSymbol;
    BufferedReader in;
    /*-Principal Fim-*/

    /*-Semântico Inicio-*/
    private final String VAR = "classe-var", CONST = "classe-const";
    private final String typeInteger = "inteiro", typeCharacter = "caractere", typeLogic = "logico", typeHexa = "hexadecimal",
            typeString = "string";

    private String TIPO_type = "", D1_class = "", D_class = "", F_type = "", F1_type = "", Exp_type = "",
            T_type = "", T1_type = "", ExpS_type = "", ExpS1_type = "";

    private Symbol tempToken = new Symbol();
    private Symbol tokenConst = new Symbol();

    private boolean minusFlag = false;
    private boolean vectorFlagInteger = false, vectorFlagCharacter = false;
    private int Exp_op = 0, ExpS_op = 0, T_op = 0;

    /*-Semântico Fim-*/

    public SyntaticAnalysis() {
        lexicalAnalysis = new LexicalAnalysis();
        symbolTable = new SymbolTable();
    }

    public void startParsing(BufferedReader archive) throws Exception {
        actualSymbol = lexicalAnalysis.tokenization(archive);
        this.in = archive;
        if (actualSymbol == null) {
            actualSymbol = lexicalAnalysis.tokenization(archive);
        }
        S();
    }


    //casaToken
    public void casaToken(byte tokEsperado) throws IOException {
        try {
            if (actualSymbol != null) {
                //System.out.println("Token atual ct:"+actualSymbol.toString());
                if (actualSymbol.getSymbol() == tokEsperado) {
                    actualSymbol = lexicalAnalysis.tokenization(in);
                } else {
                    if (lexicalAnalysis.eof) {
                        errorEOF();
                    } else {
                        errorUT(actualSymbol.getLexema());
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
            actualSymbol.setTipo(TIPO_type);
            D1();
            while (actualSymbol.getSymbol() == symbolTable.ID) {
                D1();
            }
            casaToken(symbolTable.DOTCOMMA);

        } else if (actualSymbol.getSymbol() == symbolTable.FINAL) {
            casaToken(symbolTable.FINAL);
            if (!actualSymbol.getClasse().equals("")) {
                errorID(actualSymbol.getLexema());
            }
            actualSymbol.setClasse(CONST);
            tokenConst = actualSymbol;
            casaToken(symbolTable.ID);
            casaToken(symbolTable.EQUAL);
            if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                minusFlag = true;
                casaToken(symbolTable.MINUS);
            }

            if (minusFlag == true && !tempToken.getTipo().equals(typeInteger)) {
                errorIT();
            }
            actualSymbol.setClasse(CONST);
            D_class = actualSymbol.getClasse();
            tokenConst.setTipo(actualSymbol.getTipo());
            casaToken(symbolTable.VALOR);
            casaToken(symbolTable.DOTCOMMA);
        }
    }

    //proc TIPO
    public void TIPO() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.INT) {
            casaToken(symbolTable.INT);
            TIPO_type = typeInteger;
        } else if (actualSymbol.getSymbol() == symbolTable.CHAR) {
            casaToken(symbolTable.CHAR);
            TIPO_type = typeCharacter;
        }
    }

    //proc D1
    public void D1() throws Exception {
        int temp_value;
        if (!actualSymbol.getClasse().equals("")) {
            errorID(actualSymbol.getLexema());
        } else if (actualSymbol.getLexema().equals("")) {
            System.out.println("Aqui");
        }
        actualSymbol.setTipo(TIPO_type);
        actualSymbol.setClasse(VAR);
        tempToken = actualSymbol;
        casaToken(symbolTable.ID);
        if (actualSymbol.getSymbol() == symbolTable.ATRIB) {
            casaToken(symbolTable.ATRIB);
            if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                minusFlag = true;
                casaToken(symbolTable.MINUS);
            }
            if (tempToken.getTipo().equals(typeInteger) && !actualSymbol.getTipo().equals(typeInteger)) {
                errorIT();
            } else if (minusFlag == true && !tempToken.getTipo().equals(typeInteger)) {
                errorIT();
            } else {
                casaToken(symbolTable.VALOR);
            }
        } else if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
            casaToken(symbolTable.ACOLCHETES);
            if (!actualSymbol.getTipo().equals(typeInteger)) {
                errorIT();
            }

            if (TIPO_type.equals(typeInteger)) {
                vectorFlagInteger = true;
            } else if (TIPO_type.equals(typeCharacter)) {
                vectorFlagCharacter = true;
            }
            temp_value = Integer.parseInt(actualSymbol.getLexema());
            if (temp_value < 1) {
                errorMT();
            } else if (temp_value > 32767) {
                errorEV();
            }
            lexicalAnalysis.updateVector(tempToken.getLexema(),actualSymbol.getLexema());
            casaToken(symbolTable.VALOR);
            casaToken(symbolTable.FCOLCHETES);
        }

        while (actualSymbol.getSymbol() == symbolTable.COMMA) {
            casaToken(symbolTable.COMMA);
            if (!actualSymbol.getClasse().equals("")) {
                errorID(actualSymbol.getLexema());
            }
            actualSymbol.setTipo(TIPO_type);
            actualSymbol.setClasse(VAR);
            tempToken = actualSymbol;
            casaToken(symbolTable.ID);

            if (actualSymbol.getSymbol() == symbolTable.ATRIB) {
                casaToken(symbolTable.ATRIB);
                if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                    minusFlag = true;
                    casaToken(symbolTable.MINUS);
                }

                if (tempToken.getTipo().equals(typeInteger) && !actualSymbol.getTipo().equals(typeInteger)) {
                    errorIT();
                } else if (minusFlag == true && !tempToken.getTipo().equals(typeInteger)) {
                    errorIT();
                } else {
                    casaToken(symbolTable.VALOR);
                }
            } else if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
                casaToken(symbolTable.ACOLCHETES);
                if (!actualSymbol.getTipo().equals(typeInteger)) {
                    errorIT();
                }

                if (TIPO_type.equals(typeInteger)) {
                    vectorFlagInteger = true;
                } else if (TIPO_type.equals(typeCharacter)) {
                    vectorFlagCharacter = true;
                }
                temp_value = Integer.parseInt(actualSymbol.getLexema());
                if (temp_value < 1) {
                    errorMT();
                } else if (temp_value > 32767) {
                    errorEV();
                }
                lexicalAnalysis.updateVector(tempToken.getLexema(),actualSymbol.getLexema());
                casaToken(symbolTable.VALOR);
                casaToken(symbolTable.FCOLCHETES);
            }
        }
        D1_class = VAR;
    }

    //proc C
    public void C() throws Exception {
        String id_temp = "";
        while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR
                || actualSymbol.getSymbol() == symbolTable.IF || actualSymbol.getSymbol() == symbolTable.DOTCOMMA ||
                actualSymbol.getSymbol() == symbolTable.READLN || actualSymbol.getSymbol() == symbolTable.WRITE ||
                actualSymbol.getSymbol() == symbolTable.WRITELN) {

            if (actualSymbol.getSymbol() == symbolTable.FOR) {
                casaToken(symbolTable.FOR);
                if (actualSymbol.getClasse().equals("")) {
                    errorUnT(actualSymbol.getLexema());
                } else if (!actualSymbol.getTipo().equals(typeInteger)) {
                    errorIT();
                }
                id_temp = actualSymbol.getTipo();
                casaToken(symbolTable.ID);
                casaToken(symbolTable.ATRIB);
                Exp();
                if (!Exp_type.equals(typeInteger)) {
                    errorIT();
                }
                casaToken(symbolTable.TO);

                Exp();

                if (!Exp_type.equals(typeInteger)) {
                    errorIT();
                }
                if (actualSymbol.getSymbol() == symbolTable.STEP) {
                    casaToken(symbolTable.STEP);
                    if (!actualSymbol.getTipo().equals(typeInteger)) {
                        errorIT();
                    }
                    casaToken(symbolTable.VALOR);
                }
                casaToken(symbolTable.DO);
                C1();

            } else if (actualSymbol.getSymbol() == symbolTable.IF) {
                casaToken(symbolTable.IF);
                Exp();
                if (!Exp_type.equals(typeLogic)) {
                    errorIT();
                }
                casaToken(symbolTable.THEN);
                C1();
                if (actualSymbol.getSymbol() == symbolTable.ELSE) {
                    casaToken(symbolTable.ELSE);
                    C1();
                }

            } else if (actualSymbol.getSymbol() == symbolTable.READLN) {
                casaToken(symbolTable.READLN);
                casaToken(symbolTable.APARENTESES);
                Exp();
                if (vectorFlagInteger || Exp_type.equals(typeHexa)) {
                    errorIT();
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.WRITE) {
                casaToken(symbolTable.WRITE);
                casaToken(symbolTable.APARENTESES);
                Exp();

                if (Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                    errorIT();
                }
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    casaToken(symbolTable.COMMA);
                    Exp();
                    if (!Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                        errorIT();
                    }
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.WRITELN) {
                casaToken(symbolTable.WRITELN);
                casaToken(symbolTable.APARENTESES);
                Exp();
                if (!Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                    errorIT();
                }
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    casaToken(symbolTable.COMMA);
                    Exp();
                    if (!Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                        errorIT();
                    }
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.ID) {
                if (actualSymbol.getClasse().equals("")) {
                    errorUnT(actualSymbol.getLexema());
                }
                tempToken = actualSymbol;
                casaToken(symbolTable.ID);
                casaToken(symbolTable.ATRIB);
                Exp();

                if (!Exp_type.equals(tempToken.getTipo())) {
                    errorIT();
                }
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.DOTCOMMA) {
                casaToken(symbolTable.DOTCOMMA);
            }
        }
    }

    //proc C1
    public void C1() throws Exception {
        if (actualSymbol.getSymbol() == symbolTable.BEGIN) {
            casaToken(symbolTable.BEGIN);
            C();
            while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR
                    || actualSymbol.getSymbol() == symbolTable.IF || actualSymbol.getSymbol() == symbolTable.DOTCOMMA
                    || actualSymbol.getSymbol() == symbolTable.READLN || actualSymbol.getSymbol() == symbolTable.WRITE
                    || actualSymbol.getSymbol() == symbolTable.WRITELN) {
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
        Exp_type = ExpS_type;
        if (actualSymbol.getSymbol() == symbolTable.LESS || actualSymbol.getSymbol() == symbolTable.GREAT
                || actualSymbol.getSymbol() == symbolTable.LESSOREQUAL || actualSymbol.getSymbol() == symbolTable.GREATOREQUAL
                || actualSymbol.getSymbol() == symbolTable.EQUAL || actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
            if (actualSymbol.getSymbol() == symbolTable.LESS) {
                Exp_op = 1;
                casaToken(symbolTable.LESS);
            } else if (actualSymbol.getSymbol() == symbolTable.GREAT) {
                Exp_op = 2;
                casaToken(symbolTable.GREAT);
            } else if (actualSymbol.getSymbol() == symbolTable.LESSOREQUAL) {
                Exp_op = 3;
                casaToken(symbolTable.LESSOREQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.GREATOREQUAL) {
                Exp_op = 4;
                casaToken(symbolTable.GREATOREQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.EQUAL) {
                Exp_op = 4;
                casaToken(symbolTable.EQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
                Exp_op = 5;
                casaToken(symbolTable.DIFFERENT);
            }

            ExpS();
            ExpS1_type = ExpS_type;
            switch (Exp_op) {
                case 1:
                    if (!Exp_type.equals(typeInteger) || !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    break;
                case 2:
                    if (!Exp_type.equals(typeInteger) || !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    break;
                case 3:
                    if (!Exp_type.equals(typeInteger) || !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    break;
                case 4:
                    if (Exp_type.equals(typeInteger) && !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    } else if (Exp_type.equals(typeString) && !ExpS1_type.equals(typeString)) {
                        errorIT();
                    } else if (!vectorFlagInteger || !vectorFlagCharacter) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    break;
                case 5:
                    break;
            }
        }
    }

    //proc ExpS
    public void ExpS() throws Exception {
        boolean minusFlag_ExpS = false;
        if (actualSymbol.getSymbol() == symbolTable.PLUS) {
            minusFlag_ExpS = false;
            casaToken(symbolTable.PLUS);
        } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
            minusFlag_ExpS = true;
            casaToken(symbolTable.MINUS);
        }
        T();
        ExpS_type = T_type;
        while (actualSymbol.getSymbol() == symbolTable.PLUS || actualSymbol.getSymbol() == symbolTable.MINUS
                || actualSymbol.getSymbol() == symbolTable.OR) {
            if (actualSymbol.getSymbol() == symbolTable.PLUS) {
                ExpS_op = 1;
                casaToken(symbolTable.PLUS);
            } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                ExpS_op = 2;
                casaToken(symbolTable.MINUS);
            } else {
                Exp_op = 3;
                casaToken(symbolTable.OR);
            }

            T();
            T1_type = T_type;
            switch (ExpS_op) {
                case 1:
                    if (!ExpS_type.equals(typeInteger) || !T1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    break;
                case 2:
                    if (!ExpS_type.equals(typeInteger) || !T1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    break;
                case 3:
                    if (!ExpS_type.equals(typeLogic) || !T1_type.equals(typeLogic)) {
                        errorIT();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //Proc T
    public void T() throws Exception {
        F();
        T_type = F_type;

        while (actualSymbol.getSymbol() == symbolTable.STAR || actualSymbol.getSymbol() == symbolTable.SLASH
                || actualSymbol.getSymbol() == symbolTable.PERCENT || actualSymbol.getSymbol() == symbolTable.AND) {
            if (actualSymbol.getSymbol() == symbolTable.STAR) {
                T_op = 1;
                casaToken(symbolTable.STAR);
            } else if (actualSymbol.getSymbol() == symbolTable.SLASH) {
                T_op = 2;
                casaToken(symbolTable.SLASH);
            } else if (actualSymbol.getSymbol() == symbolTable.PERCENT) {
                T_op = 3;
                casaToken(symbolTable.PERCENT);
            } else {
                T_op = 4;
                casaToken(symbolTable.AND);
            }

            F();
            F1_type = F_type;

            switch (T_op) {
                case 1:
                    if (!T_type.equals(typeInteger) || !F1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    break;
                case 2:
                    if (!T_type.equals(typeInteger) || !F1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    break;
                case 3:
                    if (!T_type.equals(typeInteger) || !F1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    break;

                case 4:
                    if (!T_type.equals(typeLogic) && !F1_type.equals(typeLogic)) {
                        errorIT();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    //Proc F
    public void F() throws Exception {
       boolean pos = false;
        if (actualSymbol.getSymbol() == symbolTable.APARENTESES) {
            casaToken(symbolTable.APARENTESES);
            Exp();
            F_type = Exp_type;
            casaToken(symbolTable.FPARENTESES);
        } else if (actualSymbol.getSymbol() == symbolTable.NOT) {
            casaToken(symbolTable.NOT);
            F();
            if (F_type.equals(typeLogic)) {
                errorIT();
            }
            F_type = typeLogic;
        } else if (actualSymbol.getSymbol() == symbolTable.VALOR) {
            F_type = actualSymbol.getTipo();
            casaToken(symbolTable.VALOR);
        } else {
            if (actualSymbol.getClasse().equals("")) {
                if (lexicalAnalysis.symbolTable.getSimb(actualSymbol.getLexema()) != null) {
                    actualSymbol.setClasse(CONST);
                    actualSymbol.setTipo(tokenConst.getTipo());
                }
            }
            if (actualSymbol.getLexema().equals(tokenConst.getLexema())) {
                actualSymbol.setClasse(tokenConst.getClasse());
                actualSymbol.setTipo(tokenConst.getTipo());
            }

            if (actualSymbol.getClasse().equals("")) {
                errorUnT(actualSymbol.getLexema());
            }
            F_type = actualSymbol.getTipo();
            tempToken = actualSymbol;
            System.out.println(tempToken);
            casaToken(symbolTable.ID);

            if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
                casaToken(symbolTable.ACOLCHETES);
                Exp();
                casaToken(symbolTable.FCOLCHETES);
            }
        }
    }

    private void errorID(String lex) {
        System.out.println(lexicalAnalysis.line + ":identificador ja declarado" + "[" + lex + "].");
        System.exit(0);
    }

    private void errorIT() {
        System.out.println(lexicalAnalysis.line + ":tipos incompativeis.");
        System.exit(0);
    }

    private void errorEOF() {
        System.err.println(lexicalAnalysis.line + ":Fim de arquivo nao esperado.");
        System.exit(0);
    }

    private void errorUT(String lex) {
        System.err.println(lexicalAnalysis.line + ":Token nao esperado: " + lex + ".");
        System.exit(1);
    }

    private void errorUnT(String lex) {
        System.out.println(lexicalAnalysis.line + ":identificador nao declarado" + "[" + lex + "].");
        System.exit(0);
    }

    private void errorIC(String lex) {
        System.out.println(lexicalAnalysis.line + ":classe de identificador incompativel" + "[" + lex + "].");
        System.exit(0);
    }

    private void errorEV() {
        System.err.println(lexicalAnalysis.line + ":tamanho do vetor excede o maximo permitido.");
        System.exit(0);
    }

    private void errorMT() {
        System.err.println(lexicalAnalysis.line + ":tamanho do vetor menor que o minimo permitido.");
        System.exit(0);
    }
}


