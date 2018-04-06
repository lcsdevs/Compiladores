import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class MainClass {
    static BufferedReader archive;
    static LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();

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
    static SyntaticAnalysis syntaticAnalysis = new SyntaticAnalysis(archive);
    public static void main(String[] args) throws Exception {
        readArchive();
        while(!lexicalAnalysis.eof){
            syntaticAnalysis.startAnalise();
            //lexicalAnalysis.tokenization(archive);
        }
        System.out.println("Compilado com sucesso");
    }
}
