package eirb.pg203;

import java.util.List;
import java.io.IOException;
import java.io.Writer;

public class TextExporter implements Exporter {
    
        public void export(Calendar cal, Writer output) throws IOException{
                List<Entry> items = cal.getItems();
                for (Entry entry : items){
                    output.write(entry.toString());
                }
        }
}
