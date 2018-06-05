import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriterASM {
    public static List<String> writer;
    public static List<String> realWriter = new ArrayList<>();
    public BufferedWriter archive;
    public String archiveName = "";

    public WriterASM(String name) throws Exception{
        writer = new ArrayList<>();
        archive = new BufferedWriter(new FileWriter("d:/8086/"+name+""));
    }

    public void createASM() throws IOException{
        for(String buffer : writer){
            archive.write(buffer);
            archive.newLine();
        }
        archive.close();
    }
}