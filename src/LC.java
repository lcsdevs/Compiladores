/*
Luciano Junior
Pedro Rangel
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LC {
    static BufferedReader archive;

    public static void main(String[] args) {
         SyntaticAnalysis syntaticAnalysis = new SyntaticAnalysis(args[1]);

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
