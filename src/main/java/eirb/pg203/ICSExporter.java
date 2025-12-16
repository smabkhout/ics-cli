package eirb.pg203;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ICSExporter implements Exporter {
    @Override
    public void export(Calendar cal, Writer output) throws IOException{
        List<Entry> items = cal.getItems();
        output.write("BEGIN:VCALENDAR\r\n" +
                        "METHOD:REQUEST\r\n" +
                        "PRODID:-//ADE/version 6.0\r\n" +
                        "VERSION:2.0\r\n" + 
                        "CALSCALE:GREGORIAN\r\n");

        for (Entry item : items){
            if (item instanceof Event) {
                writeEvent((Event) item, output);
            } else if (item instanceof Todos) { 
                writeTodo((Todos) item, output);
            }

        }

        output.write("END:VCALENDAR\r\n");
    }
    //ecrire les events
    private void writeEvent(Event event, Writer output) throws IOException{
        output.write("BEGIN:VEVENT\r\n");

        writeProperly(output, "DTSTAMP", event.getDtstamp());
        writeProperly(output, "DTSTART", event.getDtstart());
        writeProperly(output, "DTEND", event.getDtend());
        writeProperly(output, "SUMMARY", event.getSummary());
        writeProperly(output, "LOCATION", event.getLocation());
        writeProperly(output, "DESCRIPTION", event.getDescription());
        writeProperly(output, "UID", event.getUID());
           
        output.write("END:VEVENT\r\n");
    }

    //ecrire les todos
    private void writeTodo(Todos todo, Writer output) throws IOException {
        output.write("BEGIN:VTODO\r\n");
        
        writeProperly(output, "UID", todo.getUID());        
        writeProperly(output, "SUMMARY", todo.getSummary());
        writeProperly(output, "LOCATION", todo.getLocation());
        writeProperly(output, "PERCENT-COMPLETE", todo.getProgress());        
        writeProperly(output, "DUE;VALUE=DATE", todo.getDueDate());        
        writeProperly(output, "STATUS", todo.getStatus());        
        writeProperly(output, "DTSTAMP", todo.getDtstamp());
        
        output.write("END:VTODO\r\n");
    }

    //pour eviter des lignes de longueur sup à 75 (warning)
    private void writeProperly(Writer output, String key, String value) throws IOException {
        if (value == null || value.isEmpty()) return;

        String fullLine = key + ":" + value;

        if (fullLine.length() <= 75) {
            output.write(fullLine + "\r\n");
            return;
        }

        
        output.write(fullLine.substring(0, 75) + "\r\n");

        String remainder = fullLine.substring(75);

        while (remainder.length() > 74) {
            output.write(" " + remainder.substring(0, 74) + "\r\n");
            remainder = remainder.substring(74);
        }

        if (!remainder.isEmpty()) {
            output.write(" " + remainder + "\r\n");
        }
    }
}
