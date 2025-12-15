package eirb.pg203;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExporterFactory {
    
    private final Map<String, Exporter> strategies;

    public ExporterFactory() {
        strategies = new HashMap<>();
        
        strategies.put("html", new HtmlExporter());
        strategies.put("text", new TextExporter());
        strategies.put("ics", new IcsExporter());
    }

    private Exporter getExporter(String opt) {
        if (strategies.containsKey(opt)) {
            return strategies.get(opt);
        } 
        return null;
    }

    public void outputHandler(List<String> options, Calendar cal) throws IOException{
        Exporter selectedExporter = new TextExporter(); 
        Writer output = new PrintWriter(System.out);

        if (options.contains("o")){
            int i = options.indexOf("o");
            if (i + 1 < options.size()) {
                output = new FileWriter(options.get(i+1));
            }
            else {
                System.out.println("Veuillez ajouter le nom du fichier cible\n");
                return;
            }
        }

        for (String opt: options){
            Exporter tmp = this.getExporter(opt);
            if (tmp!=null){
            selectedExporter = tmp;
        }
        }

        selectedExporter.export(cal, output);        
        output.close();
    }
}
