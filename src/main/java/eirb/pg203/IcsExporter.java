package eirb.pg203;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class IcsExporter implements Exporter {
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

    private void writeEvent(Event event, Writer output) throws IOException{
        output.write("BEGIN:VEVENT\r\n");
        
        output.write("DTSTAMP:" + event.getDtstamp() + "\r\n");
        output.write("DTSTART:" + event.getDtstart() + "\r\n");
        output.write("DTEND:" + event.getDtend() + "\r\n");
        output.write("SUMMARY:" + event.getSummary() + "\r\n");
        output.write("LOCATION:" + event.getLocation() + "\r\n");
        output.write("DESCRIPTION:" + event.getDescription() + "\r\n");
        output.write("UID:" + event.getUID() +"\r\n");    

        
        output.write("END:VEVENT\r\n");
    }

    private void writeTodo(Todos todo, Writer output) throws IOException {
        output.write("BEGIN:VTODO\r\n");
        
        output.write("UID:" + todo.getUID() +"\r\n");
        output.write("SUMMARY:" + todo.getSummary() + "\r\n");
        output.write("LOCATION:" + todo.getLocation() + "\r\n");
        output.write("PERCENT-COMPLETE:" + todo.getProgress() + "\r\n");
        output.write("DUE;VALUE=DATE:" + todo.getDueDate() + "\r\n");
        output.write("STATUS:" + todo.getStatus() + "\r\n");
        output.write("DTSTAMP:" + todo.getDtstamp() + "\r\n");
        
        output.write("END:VTODO\r\n");
    }
}
