/*
Luciano Junior
Pedro Rangel
 */

import java.io.*;

public class LC {
    static BufferedReader archive;
    static SyntaticAnalysis syntaticAnalysis = new SyntaticAnalysis();

    public static void main(String[] args) {
        String nomArq;

        if(args.length < 1){
            System.out.println("Numero de argumentos invalido!");
            System.exit(5);
        }

        nomArq = args[0];

        if (nomArq.contains(".l")){
            try{
                File arq = new File(args[0]);
                archive = new BufferedReader ( new InputStreamReader(new FileInputStream(arq), "US-ASCII"));

            }catch (Exception e){
                System.out.println(e);
            }

        }

        while (!syntaticAnalysis.lexicalAnalysis.eof) {
            try {
               // readArchive(args[0]);
                syntaticAnalysis.startParsing(archive);
            } catch (Exception e) {
               // System.err.println("Erro " + e.getMessage());
            }
        }
    }
}
