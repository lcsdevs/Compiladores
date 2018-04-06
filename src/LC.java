import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class LC {
    public static Symbol simboloCorrente;
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

    public static void main(String[] args) throws Exception {
        Symbol[] bufferDeSimbolos = new Symbol[10000];
        Symbol[] simbolos = null;
        int nSimbolos = 0;

        readArchive();
        simboloCorrente = lexicalAnalysis.tokenization(archive);

        while(!lexicalAnalysis.eof){
            bufferDeSimbolos[nSimbolos] = simboloCorrente;
            nSimbolos++;
            simboloCorrente = lexicalAnalysis.tokenization(archive);
        }
        //adicionando o eof no aray de simbolos
        bufferDeSimbolos[nSimbolos] = simboloCorrente;
        nSimbolos++;
        simbolos = new Symbol[nSimbolos];
        for (int i=0; i<nSimbolos; i++){
            simbolos[i] = bufferDeSimbolos[i];
        }
        bufferDeSimbolos = null;
        SyntaticAnalysis syntaticAnalysis = new SyntaticAnalysis(simbolos, archive);
        syntaticAnalysis.startAnalise();
        System.out.println("Compilado com sucesso");
    }
}
