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

    /*MASM - Inicio*/
    boolean test = false;
    Memory memory;
    Label label;
    WriterASM writerASM;
    int address = memory.count;
    private static int F_end = 0, F1_end = 0, T_end = 0, T1_end = 0,ExpS_end = 0,ExpS1_end = 0,Exp_end = 0,
            D_end = 0;
    /*MASM - Fim*/

    public SyntaticAnalysis() {
        lexicalAnalysis = new LexicalAnalysis();
        symbolTable = new SymbolTable();
        memory = new Memory();
        label = new Label();
        try {
            writerASM = new WriterASM();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        writerASM.writer.add("sseg SEGMENT STACK ;inicio seg. pilha");
        writerASM.writer.add("byte 4000h DUP(?) ;dimensiona pilha");
        writerASM.writer.add("sseg ENDS ;fim seg. pilha");
        writerASM.writer.add("dseg SEGMENT PUBLIC ;inicio seg. dados");
        writerASM.writer.add("byte 4000h DUP(?) ;temporarios");
        address = memory.toAllocateTemp();
        while (actualSymbol.getSymbol() == symbolTable.INT || actualSymbol.getSymbol() == symbolTable.CHAR
                || actualSymbol.getSymbol() == symbolTable.FINAL) {
            D();
        }
        writerASM.writer.add("dseg ENDS ;fim seg. dados");
        writerASM.writer.add("cseg SEGMENT PUBLIC ;inicio seg. codigo");
        writerASM.writer.add("ASSUME CS:cseg, DS:dseg");
        writerASM.writer.add("strt:");
        writerASM.writer.add("mov ax, dseg");
        writerASM.writer.add("mov ds, ax");
        while (actualSymbol.getSymbol() == symbolTable.ID || actualSymbol.getSymbol() == symbolTable.FOR || actualSymbol.getSymbol() == symbolTable.IF
                || actualSymbol.getSymbol() == symbolTable.DOTCOMMA || actualSymbol.getSymbol() == symbolTable.READLN ||
                actualSymbol.getSymbol() == symbolTable.WRITE || actualSymbol.getSymbol() == symbolTable.WRITELN
                ) {
            C();
        }

    }

    //proc D
    public void D() throws Exception {
        Symbol temp = actualSymbol;
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
            temp = actualSymbol;
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
            temp.setTipo(actualSymbol.getTipo());
            String lexTemp = actualSymbol.getLexema();
            D_class = actualSymbol.getClasse();
            tokenConst.setTipo(actualSymbol.getTipo());
            if(minusFlag){
                address = memory.toAllocateInteger();
                writerASM.writer.add("sword -"+lexTemp);
                temp.setTipo(typeInteger);
            }else{
                switch (actualSymbol.getTipo()){
                    case typeInteger:
                        address = memory.toAllocateInteger();
                        writerASM.writer.add("sword "+lexTemp);
                        break;
                    case typeCharacter:
                        address = memory.toAllocateCharacter();
                        writerASM.writer.add("byte "+lexTemp);
                        break;
                    case typeString:
                        address = memory.toAllocateString(lexTemp.length() - 1);
                        writerASM.writer.add("byte "+actualSymbol.getLexema().substring(0,actualSymbol.getLexema().length() - 1)+"$"+actualSymbol.getLexema().charAt(actualSymbol.getLexema().length() - 1));
                        break;
                    case typeHexa:
                        address = memory.toAllocateInteger();
                        writerASM.writer.add("sword "+lexTemp);
                        break;
                }
            }
            temp.setEndereco(address);
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
        Symbol temp = actualSymbol;
        if (!actualSymbol.getClasse().equals("")) {
            errorID(actualSymbol.getLexema());
        }
        actualSymbol.setTipo(TIPO_type);
        actualSymbol.setClasse(VAR);
        tempToken = actualSymbol;
        temp = actualSymbol;
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
                String lexTemp = actualSymbol.getLexema();
                if(minusFlag){
                    address = memory.toAllocateInteger();
                    writerASM.writer.add("sword -"+lexTemp);
                    temp.setTipo(typeInteger);
                }else{
                    switch (actualSymbol.getTipo()){
                        case typeInteger:
                            address = memory.toAllocateInteger();
                            writerASM.writer.add("sword "+lexTemp);
                            break;
                        case typeCharacter:
                            address = memory.toAllocateCharacter();
                            writerASM.writer.add("byte "+lexTemp);
                            break;
                    }
                }
                temp.setEndereco(address);
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
            String lexTemp = actualSymbol.getLexema();
            switch (actualSymbol.getTipo()){
                case typeInteger:
                    address = memory.toAllocateVectorInt(Integer.parseInt(lexTemp));
                    int x = Integer.parseInt(lexTemp);
                    x = x * 2;
                    writerASM.writer.add("byte "+x+" DUP(?)");
                    break;
                case typeCharacter:
                    address = memory.toAllocateVectorChar(Integer.parseInt(actualSymbol.getLexema()));
                    writerASM.writer.add("byte "+lexTemp+" DUP(?)");
                    break;
            }
            casaToken(symbolTable.VALOR);
            casaToken(symbolTable.FCOLCHETES);
        }else{
            switch (temp.getTipo()){
                case typeInteger:
                    address = memory.toAllocateInteger();
                    writerASM.writer.add("sword ?; inteiro = " + temp.getLexema());
                    break;
                case typeCharacter:
                    address = memory.toAllocateCharacter();
                    writerASM.writer.add("byte ?; char = " + temp.getLexema());
                    break;
            }
            temp.setEndereco(address);
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
                    String lexTemp = actualSymbol.getLexema();
                    if(minusFlag){
                        address = memory.toAllocateInteger();
                        writerASM.writer.add("sword -"+lexTemp);
                        temp.setTipo(typeInteger);
                    }else{
                        switch (actualSymbol.getTipo()){
                            case typeInteger:
                                address = memory.toAllocateInteger();
                                writerASM.writer.add("sword "+lexTemp);
                                break;
                            case typeCharacter:
                                address = memory.toAllocateCharacter();
                                writerASM.writer.add("byte "+lexTemp);
                                break;
                        }
                    }
                    temp.setEndereco(address);
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
                String lexTemp = actualSymbol.getLexema();
                switch (actualSymbol.getTipo()){
                    case typeInteger:
                        address = memory.toAllocateVectorInt(Integer.parseInt(lexTemp));
                        int x = Integer.parseInt(lexTemp);
                        x = x * 2;
                        writerASM.writer.add("byte "+x+" DUP(?)");
                        break;
                    case typeCharacter:
                        address = memory.toAllocateVectorChar(Integer.parseInt(actualSymbol.getLexema()));
                        writerASM.writer.add("byte "+lexTemp+" DUP(?)");
                        break;
                }
                casaToken(symbolTable.VALOR);
                casaToken(symbolTable.FCOLCHETES);
            }else{
                switch (temp.getTipo()){
                    case typeInteger:
                        address = memory.toAllocateInteger();
                        writerASM.writer.add("sword ?; inteiro = " + temp.getLexema());
                        break;
                    case typeCharacter:
                        address = memory.toAllocateCharacter();
                        writerASM.writer.add("byte ?; char = " + temp.getLexema());
                        break;
                }
                temp.setEndereco(address);
            }
        }
        D1_class = VAR;
    }

    //proc C
    public void C() throws Exception {
        String id_temp = "";
        String value = "";
        Symbol tmp;
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
                String beginLabel = label.newLabel();
                String endLabel = label.newLabel();
              //  writerASM.writer.add(beginLabel+":");

                casaToken(symbolTable.ID);
                casaToken(symbolTable.ATRIB);
                Exp();
             //   writerASM.writer.add("mov ax, DS:["+actualSymbol.getEndereco()+"]");
                if (!Exp_type.equals(typeInteger)) {
                    errorIT();
                }
                casaToken(symbolTable.TO);

                Exp();
           //     writerASM.writer.add("mov bx, DS:["+Exp_end+"]");
                if (!Exp_type.equals(typeInteger)) {
                    errorIT();
                }
                if (actualSymbol.getSymbol() == symbolTable.STEP) {
                    casaToken(symbolTable.STEP);
                    tempToken = actualSymbol;
          //          writerASM.writer.add("sub cx, 1");
          //          writerASM.writer.add("jmp "+beginLabel);
                    casaToken(symbolTable.VALOR);
                    if (!tempToken.getTipo().equals(typeInteger)) {
                        errorIT();
                    }
                }
                casaToken(symbolTable.DO);
                C1();
         //       writerASM.writer.add("cmp ax, bx");
         //       writerASM.writer.add("jg "+endLabel);
         //       writerASM.writer.add("add ax, 1");
         //       writerASM.writer.add("jmp "+beginLabel);
         //       writerASM.writer.add(endLabel+":");

            } else if (actualSymbol.getSymbol() == symbolTable.IF) {
                casaToken(symbolTable.IF);
                String falseLabel = label.newLabel();
                String endLabel = label.newLabel();
                Exp();
                if (!Exp_type.equals(typeLogic)) {
                    errorIT();
                }
                writerASM.writer.add("mov ax, DS:["+Exp_end+"]");
                writerASM.writer.add("cmp ax, 0");
                writerASM.writer.add("je "+falseLabel);
                casaToken(symbolTable.THEN);
                C1();
                if (actualSymbol.getSymbol() == symbolTable.ELSE) {
                    casaToken(symbolTable.ELSE);
                    writerASM.writer.add("jmp "+endLabel);
                    writerASM.writer.add(falseLabel+":");
                    C1();
                    writerASM.writer.add(endLabel+":");
                }

            } else if (actualSymbol.getSymbol() == symbolTable.READLN) {
                casaToken(symbolTable.READLN);
                casaToken(symbolTable.APARENTESES);
                Exp();
                if (Exp_type.equals(typeHexa)) {
                    errorIT();
                }
                tmp = actualSymbol;
                int readerAdd = memory.toAllocateTempString();
                memory.countTemp+=3;
                writerASM.writer.add("mov dx, "+readerAdd);
                writerASM.writer.add("mov al, 0FFh");
                writerASM.writer.add("mov ds:["+readerAdd+"], al");
                writerASM.writer.add("mov ah, 0Ah");
                writerASM.writer.add("int 21h");
                writerASM.writer.add("mov ah, 02h");
                writerASM.writer.add("mov dl, 0Dh");
                writerASM.writer.add("int 21h");
                writerASM.writer.add("mov DL, 0Ah");
                writerASM.writer.add("int 21h");
                writerASM.writer.add("mov di, "+ readerAdd+2);

                if(!tmp.getTipo().equals(typeString)){
                    writerASM.writer.add("mov ax, 0");
                    writerASM.writer.add("mov cx, 10");
                    writerASM.writer.add("mov dx, 1");
                    writerASM.writer.add("mov bh, 0");
                    writerASM.writer.add("mov bl, ds:[di]");
                    writerASM.writer.add("cmp bx, 2Dh");
                    String lab = label.newLabel();
                    writerASM.writer.add("jne " + lab);
                    writerASM.writer.add("mov dx, -1");
                    writerASM.writer.add("add di, 1");
                    writerASM.writer.add("mov bl, ds:[di]");
                    writerASM.writer.add(lab + ":");
                    writerASM.writer.add("push dx");
                    writerASM.writer.add("mov dx, 0");
                    String lab1 = label.newLabel();
                    writerASM.writer.add(lab1 + ":");
                    writerASM.writer.add("cmp bx, 0Dh");
                    String lab2 = label.newLabel();
                    writerASM.writer.add("je " + lab2);
                    writerASM.writer.add("imul cx");
                    writerASM.writer.add("add bx, -48");
                    writerASM.writer.add("add ax, bx");
                    writerASM.writer.add("add di, 1");
                    writerASM.writer.add("mov bh, 0");
                    writerASM.writer.add("mov bl, ds:[di]");
                    writerASM.writer.add("jmp " + lab1);
                    writerASM.writer.add(lab2 + ":");
                    writerASM.writer.add("pop cx");
                    writerASM.writer.add("imul cx");
                    writerASM.writer.add("mov DS:[" + tmp.getEndereco() + "], ax");
                }else{
                    writerASM.writer.add("mov si, " + tmp.getEndereco());
                    String labString = label.newLabel();
                    writerASM.writer.add(labString + ":");
                    writerASM.writer.add("mov al, ds:[di]");
                    writerASM.writer.add("cmp al, 0dh ;verifica fim string");
                    String lab = label.newLabel();
                    writerASM.writer.add("je " + lab + " ;salta se fim string");
                    writerASM.writer.add("mov ds:[si], al ;pr�ximo caractere");
                    writerASM.writer.add("add di, 1 ;incrementa base");
                    writerASM.writer.add("add si, 1");
                    writerASM.writer.add("jmp " + labString + " ;loop");
                    writerASM.writer.add(lab + ":");
                    writerASM.writer.add("mov al, 024h ;fim de string");
                    writerASM.writer.add("mov ds:[si], al ;grava '$'");
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.WRITE) {
                int StringAdd = memory.newTemp();
                casaToken(symbolTable.WRITE);
                casaToken(symbolTable.APARENTESES);
                Exp();
                if (Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                    errorIT();
                }

                if(Exp_type.equals(typeString)){
                    writerASM.writer.add("mov dx, "+Exp_end);
                    writerASM.writer.add("mov ah, 09h");
                    writerASM.writer.add("int 21h");
                }else{
                    writerASM.writer.add("mov ax, DS:[" + Exp_end + "]");
                    writerASM.writer.add("mov di, " + StringAdd);
                    writerASM.writer.add("mov cx, 0 ;contador");
                    writerASM.writer.add("cmp ax,0 ;verifica sinal");
                    String lab = label.newLabel();
                    writerASM.writer.add("jge " + lab);
                    writerASM.writer.add("mov bl, 2Dh");
                    writerASM.writer.add("mov ds:[di], bl");
                    writerASM.writer.add("add di, 1");
                    writerASM.writer.add("neg ax");
                    writerASM.writer.add(lab + ":");
                    writerASM.writer.add("mov bx, 10 ");
                    String lab1 = label.newLabel();
                    writerASM.writer.add(lab1 + ":");
                    writerASM.writer.add("add cx, 1 ");
                    writerASM.writer.add("mov dx, 0 ");
                    writerASM.writer.add("idiv bx ");
                    writerASM.writer.add("push dx ");
                    writerASM.writer.add("cmp ax, 0 ");
                    writerASM.writer.add("jne " + lab1);
                    String lab2 = label.newLabel();
                    writerASM.writer.add(lab2 + ":");
                    writerASM.writer.add("pop dx");
                    writerASM.writer.add("add dx, 30h");
                    writerASM.writer.add("mov ds:[di],dl ");
                    writerASM.writer.add("add di, 1 ");
                    writerASM.writer.add("add cx, -1 ");
                    writerASM.writer.add("cmp cx, 0 ");
                    writerASM.writer.add("jne " + lab2);
                    writerASM.writer.add("mov dl, 024h");
                    writerASM.writer.add("mov ds:[di], dl");
                    writerASM.writer.add("mov dx, " + StringAdd);
                    writerASM.writer.add("mov ah, 09h");
                    writerASM.writer.add("int 21h");
                }

                while (actualSymbol.getSymbol() == symbolTable.COMMA) {

                    casaToken(symbolTable.COMMA);
                    Exp();
                    if (!Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                        errorIT();
                    }

                    if(Exp_type.equals(typeString)){
                        writerASM.writer.add("mov dx, "+Exp_end);
                        writerASM.writer.add("mov ah, 09h");
                        writerASM.writer.add("int 21h");
                    }else{
                        writerASM.writer.add("mov ax, DS:[" + Exp_end + "]");
                        writerASM.writer.add("mov di, " + StringAdd);
                        writerASM.writer.add("mov cx, 0 ;contador");
                        writerASM.writer.add("cmp ax,0 ;verifica sinal");
                        String lab = label.newLabel();
                        writerASM.writer.add("jge " + lab);
                        writerASM.writer.add("mov bl, 2Dh");
                        writerASM.writer.add("mov ds:[di], bl");
                        writerASM.writer.add("add di, 1");
                        writerASM.writer.add("neg ax");
                        writerASM.writer.add(lab + ":");
                        writerASM.writer.add("mov bx, 10 ");
                        String lab1 = label.newLabel();
                        writerASM.writer.add(lab1 + ":");
                        writerASM.writer.add("add cx, 1 ");
                        writerASM.writer.add("mov dx, 0 ");
                        writerASM.writer.add("idiv bx ");
                        writerASM.writer.add("push dx ");
                        writerASM.writer.add("cmp ax, 0 ");
                        writerASM.writer.add("jne " + lab1);
                        String lab2 = label.newLabel();
                        writerASM.writer.add(lab2 + ":");
                        writerASM.writer.add("pop dx");
                        writerASM.writer.add("add dx, 30h");
                        writerASM.writer.add("mov ds:[di],dl ");
                        writerASM.writer.add("add di, 1 ");
                        writerASM.writer.add("add cx, -1 ");
                        writerASM.writer.add("cmp cx, 0 ");
                        writerASM.writer.add("jne " + lab2);
                        writerASM.writer.add("mov dl, 024h");
                        writerASM.writer.add("mov ds:[di], dl");
                        writerASM.writer.add("mov dx, " + StringAdd);
                        writerASM.writer.add("mov ah, 09h");
                        writerASM.writer.add("int 21h");
                    }
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.WRITELN) {
                int StringAdd = memory.newTemp();
                casaToken(symbolTable.WRITELN);
                casaToken(symbolTable.APARENTESES);
                Exp();
                if (!Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                    errorIT();
                }
                if(Exp_type.equals(typeString)){
                    writerASM.writer.add("mov dx, "+Exp_end);
                    writerASM.writer.add("mov ah, 09h");
                    writerASM.writer.add("int 21h");
                }else{
                    writerASM.writer.add("mov ax, DS:[" + Exp_end + "]");
                    writerASM.writer.add("mov di, " + StringAdd);
                    writerASM.writer.add("mov cx, 0 ;contador");
                    writerASM.writer.add("cmp ax,0 ;verifica sinal");
                    String lab = label.newLabel();
                    writerASM.writer.add("jge " + lab);
                    writerASM.writer.add("mov bl, 2Dh");
                    writerASM.writer.add("mov ds:[di], bl");
                    writerASM.writer.add("add di, 1");
                    writerASM.writer.add("neg ax");
                    writerASM.writer.add(lab + ":");
                    writerASM.writer.add("mov bx, 10 ");
                    String lab1 = label.newLabel();
                    writerASM.writer.add(lab1 + ":");
                    writerASM.writer.add("add cx, 1 ");
                    writerASM.writer.add("mov dx, 0 ");
                    writerASM.writer.add("idiv bx ");
                    writerASM.writer.add("push dx ");
                    writerASM.writer.add("cmp ax, 0 ");
                    writerASM.writer.add("jne " + lab1);
                    String lab2 = label.newLabel();
                    writerASM.writer.add(lab2 + ":");
                    writerASM.writer.add("pop dx");
                    writerASM.writer.add("add dx, 30h");
                    writerASM.writer.add("mov ds:[di],dl ");
                    writerASM.writer.add("add di, 1 ");
                    writerASM.writer.add("add cx, -1 ");
                    writerASM.writer.add("cmp cx, 0 ");
                    writerASM.writer.add("jne " + lab2);
                    writerASM.writer.add("mov dl, 024h");
                    writerASM.writer.add("mov ds:[di], dl");
                    writerASM.writer.add("mov dx, " + StringAdd);
                    writerASM.writer.add("mov ah, 09h");
                    writerASM.writer.add("int 21h");
                    writerASM.writer.add("mov ah, 02h");
                    writerASM.writer.add("mov dl, 0Dh");
                    writerASM.writer.add("int 21h");
                    writerASM.writer.add("mov DL, 0Ah");
                    writerASM.writer.add("int 21h");
                }
                while (actualSymbol.getSymbol() == symbolTable.COMMA) {
                    casaToken(symbolTable.COMMA);
                    Exp();
                    if (!Exp_type.equals(typeCharacter) && !Exp_type.equals(typeInteger) && !Exp_type.equals(typeString)) {
                        errorIT();
                    }
                    if(Exp_type.equals(typeString)){
                        writerASM.writer.add("mov dx, "+Exp_end);
                        writerASM.writer.add("mov ah, 09h");
                        writerASM.writer.add("int 21h");
                    }else{
                        writerASM.writer.add("mov ax, DS:[" + Exp_end + "]");
                        writerASM.writer.add("mov di, " + StringAdd);
                        writerASM.writer.add("mov cx, 0 ;contador");
                        writerASM.writer.add("cmp ax,0 ;verifica sinal");
                        String lab = label.newLabel();
                        writerASM.writer.add("jge " + lab);
                        writerASM.writer.add("mov bl, 2Dh");
                        writerASM.writer.add("mov ds:[di], bl");
                        writerASM.writer.add("add di, 1");
                        writerASM.writer.add("neg ax");
                        writerASM.writer.add(lab + ":");
                        writerASM.writer.add("mov bx, 10 ");
                        String lab1 = label.newLabel();
                        writerASM.writer.add(lab1 + ":");
                        writerASM.writer.add("add cx, 1 ");
                        writerASM.writer.add("mov dx, 0 ");
                        writerASM.writer.add("idiv bx ");
                        writerASM.writer.add("push dx ");
                        writerASM.writer.add("cmp ax, 0 ");
                        writerASM.writer.add("jne " + lab1);
                        String lab2 = label.newLabel();
                        writerASM.writer.add(lab2 + ":");
                        writerASM.writer.add("pop dx");
                        writerASM.writer.add("add dx, 30h");
                        writerASM.writer.add("mov ds:[di],dl ");
                        writerASM.writer.add("add di, 1 ");
                        writerASM.writer.add("add cx, -1 ");
                        writerASM.writer.add("cmp cx, 0 ");
                        writerASM.writer.add("jne " + lab2);
                        writerASM.writer.add("mov dl, 024h");
                        writerASM.writer.add("mov ds:[di], dl");
                        writerASM.writer.add("mov dx, " + StringAdd);
                        writerASM.writer.add("mov ah, 09h");
                        writerASM.writer.add("int 21h");
                        writerASM.writer.add("mov ah, 02h");
                        writerASM.writer.add("mov dl, 0Dh");
                        writerASM.writer.add("int 21h");
                        writerASM.writer.add("mov DL, 0Ah");
                        writerASM.writer.add("int 21h");
                    }
                }
                casaToken(symbolTable.FPARENTESES);
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.ID) {
                if (actualSymbol.getClasse().equals("")) {
                    errorUnT(actualSymbol.getLexema());
                }
                tempToken = actualSymbol;
                tmp = actualSymbol;
                casaToken(symbolTable.ID);
                if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
                    casaToken(symbolTable.ACOLCHETES);
                    Exp();
                    casaToken(symbolTable.FCOLCHETES);
                    if(tempToken.getTipo().equals(typeCharacter)){
                        vectorFlagCharacter = true;
                    }else if(tempToken.getTipo().equals(typeInteger)){
                        vectorFlagInteger = true;
                    }
                }
                casaToken(symbolTable.ATRIB);
                if(actualSymbol.getTipo().equals(typeString)) {
                    value = actualSymbol.getLexema().substring(1, actualSymbol.getLexema().length() - 1);
                    value += "$";
                }
                Exp();
                if(vectorFlagCharacter && tempToken.getTipo().equals(typeCharacter)){
                    if(Exp_type.equals(typeString)){
                        int tam = lexicalAnalysis.search(tempToken.getLexema()).tamanho;
                        if(value.length() > tam){
                            errorEV();
                        }
                    }
                }else if (!Exp_type.equals(tempToken.getTipo())) {
                    errorIT();
                }else if(tempToken.getTipo().equals(typeCharacter) && (!Exp_type.equals(typeCharacter) || !Exp_type.equals(typeInteger))){
                    errorIT();
                }else if(tempToken.getTipo().equals(typeInteger) && !Exp_type.equals(typeInteger)){
                    errorIT();
                }else if(vectorFlagInteger){
                    //errorIT();
                }
                writerASM.writer.add("mov al, DS:["+Exp_end+"]");
                if(Exp_type.equals(typeCharacter)){
                    writerASM.writer.add("mov ah, 0");
                }
                writerASM.writer.add("mov DS:["+tmp.getEndereco()+"], ax");
                casaToken(symbolTable.DOTCOMMA);

            } else if (actualSymbol.getSymbol() == symbolTable.DOTCOMMA) {
                casaToken(symbolTable.DOTCOMMA);
            }
        }
        memory.resetTemp();
        writerASM.writer.add("mov ah, 4Ch");
        writerASM.writer.add("int 21h");
        writerASM.writer.add("cseg ENDS ;fim seg. codigo");
        writerASM.writer.add("END strt ;fim programa");
        writerASM.createASM();

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
        Exp_end = ExpS_end;
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
                Exp_op = 5;
                casaToken(symbolTable.GREATOREQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.EQUAL) {
                Exp_op = 4;
                casaToken(symbolTable.EQUAL);
            } else if (actualSymbol.getSymbol() == symbolTable.DIFFERENT) {
                Exp_op = 6;
                casaToken(symbolTable.DIFFERENT);
            }

            ExpS();
            ExpS1_type = ExpS_type;
            writerASM.writer.add("mov ax, DS:["+Exp_end+"]");
            writerASM.writer.add("mov bx, DS:["+ExpS_end+"]");
            writerASM.writer.add("cmp ax, bx");
            String trueLabel = label.newLabel();

            switch (Exp_op) {
                case 1:
                    if (!Exp_type.equals(typeInteger) || !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    writerASM.writer.add("jl "+trueLabel);
                    break;
                case 2:
                    if (!Exp_type.equals(typeInteger) || !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    writerASM.writer.add("jg "+trueLabel);
                    break;
                case 3:
                    if (!Exp_type.equals(typeInteger) || !ExpS1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    Exp_type = typeLogic;
                    writerASM.writer.add("jle "+trueLabel);
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
                    writerASM.writer.add("je "+trueLabel);
                    break;
                case 5:
                    if(Exp_type.equals(typeInteger) && ExpS1_type.equals(typeInteger)){
                        errorIT();
                    }
                    writerASM.writer.add("jge "+trueLabel);
                    break;
                case 6:
                    if(Exp_type.equals(typeInteger) && !ExpS1_type.equals(typeInteger)){
                        errorIT();
                    }
                    writerASM.writer.add("jne "+trueLabel);
                    break;
            }
            writerASM.writer.add("mov AL, 0");
            String falseLabel = label.newLabel();
            writerASM.writer.add("jmp "+falseLabel);
            writerASM.writer.add(trueLabel+":");
            writerASM.writer.add("mov AL,0FFh");
            writerASM.writer.add(falseLabel+":");
            Exp_end = memory.newTemp();
            writerASM.writer.add("mov DS:["+Exp_end+"], AL");
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
        if(minusFlag_ExpS){
            ExpS_end = memory.newTemp();
            writerASM.writer.add("mov al, DS:["+T_end+"]");
            writerASM.writer.add("not al");
            writerASM.writer.add("mov DS:["+T_end+"], al");
        }
        ExpS_end = T_end;

        while (actualSymbol.getSymbol() == symbolTable.PLUS || actualSymbol.getSymbol() == symbolTable.MINUS
                || actualSymbol.getSymbol() == symbolTable.OR) {
            if (actualSymbol.getSymbol() == symbolTable.PLUS) {
                ExpS_op = 1;
                casaToken(symbolTable.PLUS);
            } else if (actualSymbol.getSymbol() == symbolTable.MINUS) {
                ExpS_op = 2;
                casaToken(symbolTable.MINUS);
                minusFlag_ExpS = true;
            } else {
                Exp_op = 3;
                casaToken(symbolTable.OR);
            }
            int T_endTemp = T_end;
            T();
            T1_type = T_type;
            writerASM.writer.add("mov ax, DS:["+ExpS_end+"]");
            writerASM.writer.add("mov bx, DS:["+T_end+"]");
            switch (ExpS_op) {
                case 1:
                    if (!ExpS_type.equals(typeInteger) || !T1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    writerASM.writer.add("add ax, bx");
                    break;
                case 2:
                    if (!ExpS_type.equals(typeInteger) || !T1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    writerASM.writer.add("sub ax, bx");
                    break;
                case 3:
                    if (!ExpS_type.equals(typeLogic) || !T1_type.equals(typeLogic)) {
                        errorIT();
                    }
                    writerASM.writer.add("or ax, bx");
                    break;
                default:
                    break;
            }
            ExpS_end = memory.newTemp();
            writerASM.writer.add("mov DS:["+ExpS_end+"], ax");
        }
    }

    //Proc T
    public void T() throws Exception {
        F();
        T_type = F_type;
        T_end = F_end;
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
            writerASM.writer.add("mov al, DS:["+T_end+"]");
            writerASM.writer.add("mov bx, DS:["+F_end+"]");

            if(T_op == 2){
                writerASM.writer.add("cwd");
                writerASM.writer.add("mov cx, ax");
                writerASM.writer.add("cwd");
                writerASM.writer.add("mov bx, ax");
                writerASM.writer.add("mov ax, cx");
            }


            switch (T_op) {
                case 1:
                    if (!T_type.equals(typeInteger) || !F1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    writerASM.writer.add("imul bx");
                    break;
                case 2:
                    if (!T_type.equals(typeInteger) || !F1_type.equals(typeInteger)) {
                        errorIT();
                    }
                    writerASM.writer.add("idiv bx");
                    writerASM.writer.add("sub ax, 256");
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
                    writerASM.writer.add("and ax, bx");
                    break;

                default:
                    break;
            }
            T_end = memory.newTemp();
            writerASM.writer.add("mov DS:["+T_end+"], ax");
        }
    }

    //Proc F
    public void F() throws Exception {
       boolean pos = false;
        if (actualSymbol.getSymbol() == symbolTable.APARENTESES) {
            casaToken(symbolTable.APARENTESES);
            Exp();
            F_type = Exp_type;
            F_end = Exp_end;
            casaToken(symbolTable.FPARENTESES);
        } else if (actualSymbol.getSymbol() == symbolTable.NOT) {
            casaToken(symbolTable.NOT);
            int F_endTemp = F_end;
            F();
            if (!F_type.equals(typeLogic)) {
                errorIT();
            }
            F_type = typeLogic;
            F_endTemp = memory.newTemp();
            writerASM.writer.add("mov al, DS:["+F_end+"]");
            writerASM.writer.add("not al");
            writerASM.writer.add("mov DS:["+F_endTemp+"], al");
            F_end = F_endTemp;
        } else if (actualSymbol.getSymbol() == symbolTable.VALOR) {
            F_type = actualSymbol.getTipo();
            if(F_type.equals(typeString)){
                writerASM.writer.add("dseg SEGMENT PUBLIC");
                writerASM.writer.add("byte "+actualSymbol.getLexema().substring(0,actualSymbol.getLexema().length()-1)+"$"+actualSymbol.getLexema().charAt(actualSymbol.getLexema().length()- 1));
                writerASM.writer.add("dseg ENDS");
                F_end = memory.count;
                memory.toAllocateString(actualSymbol.getLexema().length()-1);
            }else{
                String lexTemp = actualSymbol.getLexema();
                F_end = memory.newTemp();
                writerASM.writer.add("mov ax, "+lexTemp);
                writerASM.writer.add("mov DS:["+F_end+"], al");
                if(actualSymbol.getTipo().equals(typeInteger)){
                    memory.toAllocateTempInteger();
                }else if(actualSymbol.getTipo().equals(typeCharacter)){
                    memory.toAllocateTempCharacter();
                }else if(actualSymbol.getTipo().equals(typeHexa)){
                    memory.toAllocateTempInteger();
                }
            }
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
            F_end = actualSymbol.getEndereco();
            casaToken(symbolTable.ID);
            if (actualSymbol.getSymbol() == symbolTable.ACOLCHETES) {
                casaToken(symbolTable.ACOLCHETES);
                Exp();
                F_end = Exp_end;
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


