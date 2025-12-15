package eirb.pg203;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class HtmlExporter implements Exporter {
    @Override
    public void export(Calendar cal, Writer output) throws IOException{
        List<Entry> items = cal.getItems();
        output.write("<html><body><ul>\n");
        for (Entry entry : items) {
            output.write("<li>" +
             entry.toString().replace("\033[32m", "").replace("\033[0m", "").replace("\n", "<br>") 
            + "</li>\n");
        }
        output.write("</ul></body></html>");
    }
    
}
