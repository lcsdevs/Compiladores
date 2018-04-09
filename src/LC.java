import java.io.*;

public class LC {
    static BufferedReader archive;
    static SyntaticAnalysis syntaticAnalysis = new SyntaticAnalysis();
    ;

    static void readArchive() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String file = "";

        try {
            do {
                System.out.print("Digite o nome do arquivo: ");
                file = in.readLine();
                if (file.length() > 0) {
                    if (file.charAt(file.length() - 2) != '.' && file.charAt(file.length() - 1) != 'l') {
                        System.err.println("Arquivo nao compativel");
                        System.out.print("Digite o nome do arquivo: ");
                        file = in.readLine();
                    }
                }
            } while (file.length() == 0);
            archive = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            System.err.println("Arquivo nao encontrado");
            readArchive();
        }
    }

    public static void main(String[] args) throws Exception {
        String nomArq;
      /*
        if(args.length < 1){
            System.out.println("Numero de argumentos invalido!");
            System.exit(0);
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
        */
        while (!syntaticAnalysis.lexicalAnalysis.eof) {
            try {
                readArchive();
                syntaticAnalysis.startParsing(archive);
            } catch (Exception e) {
                System.err.println("Erro " + e.getMessage());
            }
        }
    }
}
