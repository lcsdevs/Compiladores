/*Alunos
Luciano Custodio
Pedro Nadu
 */

import java.io.BufferedReader;


public class SyntaticAnalysis {

        byte tok;
        //Symbol[] simbolos;
        //int pos;
        //String nomArq;
        BufferedReader archive;
        LexicalAnalysis analisadorLexico;

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
    //final byte EOF = 39;

    public SyntaticAnalysis(BufferedReader archive){
        try {
            this.archive = archive;
        }catch (Exception e){
            System.out.println("Erro!");
        }


    }

        //casaToken
        public void casaTok(byte tokEsperado)throws Exception {
            if(tokEsperado == tok){
                analisadorLexico.tokenization(archive);
            }
            else {
                System.out.println("token nao esperado [" + tok + "].");
                System.exit(0);
            }
        }

        public void startAnalise() throws Exception{
                S();
        }
        //proc S
        public void S () throws Exception{
            while(tok == INT || tok == CHAR || tok == FINAL){
                D();
            }
            while(tok == ID || tok == FOR || tok ==IF || tok == DOTCOMMA || tok == READLN || tok == WRITE || tok == WRITELN){
                C();
            }
        }

        //proc D
        public void D() throws Exception{
            if (tok == INT || tok == CHAR) {

                TIPO();
                D1();
                while (tok == ID) {
                    D1();
                }
            } else if (tok == FINAL) {
                casaTok(FINAL);
                casaTok(ID);
                casaTok(ATRIB);
                if (tok == MINUS) {
                    casaTok(MINUS);
                }
                casaTok(VALOR);
                casaTok(DOTCOMMA);
            }
        }

        //proc TIPO
        public void TIPO() throws Exception {
            if (tok == INT) {
                casaTok(INT);
            } else if (tok == CHAR) {
                casaTok(CHAR);
            }
        }

        //proc D1
        public void D1() throws Exception {
            casaTok(ID);
            if (tok == ATRIB) {
                casaTok(ATRIB);
                if (tok == MINUS) {
                   casaTok(MINUS);
                }
                    casaTok(VALOR);
                } else {
                    casaTok(ACOLCHETES);
                    casaTok(VALOR);
                    casaTok(FCOLCHETES);
                }
            while(tok == COMMA) {
                casaTok(COMMA);
                casaTok(ID);
                if (tok == ATRIB) {
                        casaTok(ATRIB);
                        if (tok == MINUS) {
                            casaTok(MINUS);
                        }
                        casaTok(VALOR);
                    } else {
                        casaTok(ACOLCHETES);
                        casaTok(VALOR);
                        casaTok(FCOLCHETES);
                    }
                }
            }

        //proc C
        public void C() throws Exception {
            while(tok == ID || tok == FOR || tok == IF || tok == DOTCOMMA || tok == READLN || tok == WRITE || tok == WRITELN){
                 if(tok == FOR) {
                     casaTok(FOR);
                     casaTok(ID);
                     casaTok(ATRIB);
                     Exp();
                     casaTok(TO);
                     Exp();
                     if (tok == STEP) {
                         casaTok(STEP);
                         casaTok(VALOR);
                     }
                     casaTok(DO);
                     C1();
                 }
                 else if( tok == IF) {
                     casaTok(IF);
                     Exp();
                     casaTok(THEN);
                     C1();
                     if (tok == ELSE) {
                         casaTok(ELSE);
                         C1();
                     }
                 }
                 else if(tok == READLN){
                     casaTok(READLN);
                     casaTok(APARENTESES);
                     casaTok(ID);
                     casaTok(FPARENTESES);
                     casaTok(DOTCOMMA);
                 }

                 else if( tok == WRITE){
                     casaTok(WRITE);
                     casaTok(APARENTESES);
                     Exp();
                     while(tok == COMMA){
                         casaTok(COMMA);
                         Exp();
                     }
                     casaTok(FPARENTESES);
                     casaTok(DOTCOMMA);
                 }
                 else if( tok == WRITELN){
                     casaTok(WRITELN);
                     casaTok(APARENTESES);
                     Exp();
                     while(tok == COMMA){
                         casaTok(COMMA);
                         Exp();
                     }
                     casaTok(FPARENTESES);
                     casaTok(DOTCOMMA);
                 }
                else if(tok == ID){
                    casaTok(ID);
                    casaTok(ATRIB);
                    Exp();
                    casaTok(DOTCOMMA);
                }
                else if(tok == DOTCOMMA){
                    casaTok(DOTCOMMA);
                }
            }
        }

        //proc C1
        public void C1() throws Exception {
            if (tok == BEGIN) {
                casaTok(BEGIN);
                C();
                while (tok == ID || tok == FOR || tok == IF || tok == DOTCOMMA || tok == READLN || tok == WRITE || tok == WRITELN) {
                    C();
                }
                casaTok(END);

            } else {
                C();
            }
        }

        //Proc Exp
        public void Exp()throws Exception{
            ExpS();
            if(tok == LESS || tok == GREAT || tok == LESSOREQUAL ||  tok == GREATOREQUAL || tok == EQUAL || tok == DIFFERENT){
                if (tok == LESS) {
                    casaTok(LESS);
                }
                else if (tok == GREAT) {
                    casaTok(GREAT);
                }
                else if (tok == LESSOREQUAL) {
                    casaTok(LESSOREQUAL);
                }
                else if(tok == GREATOREQUAL){
                    casaTok(GREATOREQUAL);
                }
                else if(tok == EQUAL){
                    casaTok(EQUAL);
                }
                else if (tok == DIFFERENT) {
                    casaTok(DIFFERENT);
                }
                ExpS();
            }
        }

        //proc ExpS
        public void ExpS() throws Exception {
            if(tok == PLUS){
                casaTok(PLUS);
            }
            else if (tok == MINUS){
                casaTok(MINUS);
            }
            T();
            while(tok == PLUS || tok == MINUS || tok == OR){
                if(tok == PLUS){
                    casaTok(PLUS);
                }
                else if(tok == MINUS){
                    casaTok(MINUS);
                }
                else{
                    casaTok(OR);
                }
                T();
            }
        }

        //Proc T
        public void T() throws Exception {
            F();
            while(tok == STAR || tok == SLASH || tok == PERCENT || tok == AND ){
                if(tok == STAR){
                    casaTok(STAR);
                }
                else if(tok == SLASH){
                    casaTok(SLASH);
                }
                else if(tok == PERCENT){
                    casaTok(PERCENT);
                }
                else {
                    casaTok(AND);
                }
                F();
            }
        }

        //Proc F
        public void F() throws Exception {
            if(tok == APARENTESES) {
                casaTok(APARENTESES);
                Exp();
                casaTok(FPARENTESES);
            }
            else if(tok == NOT){
                casaTok(NOT);
                F();
            }
            else if(tok == VALOR){
                casaTok(VALOR);
            }
            else{
                casaTok(ID);
                if(tok == ACOLCHETES){
                  casaTok(ACOLCHETES);
                  Exp();
                  casaTok(FCOLCHETES);
                }
            }
        }

    }


