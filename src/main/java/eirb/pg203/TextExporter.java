package eirb.pg203;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class TextExporter implements Exporter {
        @Override
        public void export(Calendar cal, Writer output) throws IOException{
                List<Entry> items = cal.getItems();
                for (Entry entry : items){
                    output.write(entry.toString());
                }
        }
}
