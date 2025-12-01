        package eirb.pg203;

        import java.io.BufferedReader; // Pour lire le texte de manière optimisée
        import java.io.FileReader;     // Pour lire un fichier caractère par caractère
        import java.io.IOException;  // L'exception que la lecture de fichier peut lever
        import java.util.ArrayList;
        import java.util.List;


abstract class Parser<T extends ICS>{
    protected String path;
    protected List<T> ICSs;
    protected int currentCursor = 0;

    public Parser(String path){
        this.path=path;
        this.ICSs= new ArrayList<>();
    }

    protected List<T> getICSs(){
        return ICSs;
    }

    protected abstract void lineProcess(String line);
    public void parse() {
        
        this.currentCursor = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            String line;
            String currentLine = null;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                if (line.startsWith((" ")) || line.startsWith("\t")){
                    if (currentLine != null) currentLine += line.substring(1);
                    continue;
                }

                if (currentLine != null){
                    lineProcess(currentLine);
                }               
                currentLine = line;
            }
            if (currentLine != null){
                lineProcess(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void icssSort();
    public abstract void icsFilter(String opt);
}