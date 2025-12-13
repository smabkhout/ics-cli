package eirb.pg203;

import java.io.BufferedReader; // Pour lire le texte de manière optimisée
import java.io.FileReader;     // Pour lire un fichier caractère par caractère
import java.io.IOException;  // L'exception que la lecture de fichier peut lever

public class Parser {

    protected String path;
    private Calendar calendar;
    protected Entry currentItem = null;

    public Parser(String path) {
        this.path = path;
        this.calendar = new Calendar();
    }

    protected Calendar getCalendar() {
        return calendar;
    }

    protected void lineProcess(String line) {
        //event prefixes
        String beginevent = "BEGIN:VEVENT";
        String endevent = "END:VEVENT";
        String dtstartprefix = "DTSTART:";
        String dtendprefix = "DTEND:";
        String descriptionprefix = "DESCRIPTION:";

        //todo prefixes
        String begintodo = "BEGIN:VTODO";
        String endtodo = "END:VTODO";
        String dueDateprefix = "DUE;VALUE=DATE:";
        String statusprefix = "STATUS:";
        String progressprefix = "PERCENT-COMPLETE:";

        //common prefixes
        String summaryprefix = "SUMMARY:";
        String locationprefix = "LOCATION:";

        if (line.equals(beginevent)) {
            currentItem = new Event();
            this.calendar.addItem(currentItem);
        } else if (line.equals(begintodo)) {
            currentItem = new Todos();
            this.calendar.addItem(currentItem);
        }

        if (currentItem == null) {
            return;
        }

        if (line.equals(endevent) || line.equals(endtodo)) {
            calendar.addItem(currentItem);
            currentItem = null;
            return;
        }

        if (line.startsWith(summaryprefix)) {
            String summary = line.substring(summaryprefix.length()); // on lit la valeur de summary
            currentItem.setSummary(summary); // on la mets sur notre event
        } else if (line.startsWith(locationprefix)) {
            String location = line.substring(locationprefix.length());
            currentItem.setLocation(location);
        }

        if (currentItem instanceof Event) {
            if (line.startsWith(dtstartprefix)) {
                String dtstart = line.substring(dtstartprefix.length());
                ((Event) currentItem).setDtstart(dtstart);
            } else if (line.startsWith(dtendprefix)) {
                String dtend = line.substring(dtendprefix.length());
                ((Event) currentItem).setDtend(dtend);
            } else if (line.startsWith(descriptionprefix)) {
                String description = line.substring(descriptionprefix.length());
                ((Event) currentItem).setDescription(description);
            }
        } else if (currentItem instanceof Todos) {
            if (line.startsWith(dueDateprefix)) {
                String dueDate = line.substring(dueDateprefix.length());
                ((Todos) currentItem).setDueDate(dueDate);
            } else if (line.startsWith(statusprefix)) {
                String status = line.substring(statusprefix.length());
                ((Todos) currentItem).setStatus(status);
            } else if (line.startsWith(progressprefix)) {
                String progress = line.substring(progressprefix.length());
                ((Todos) currentItem).setProgress(progress);
            }
        }
    }

    public void parse() {

        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            String line;
            String currentLine = null;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith((" ")) || line.startsWith("\t")) {
                    if (currentLine != null) {
                        currentLine += line.substring(1);
                    }
                    continue;
                }

                if (currentLine != null) {
                    lineProcess(currentLine);
                }
                currentLine = line;
            }
            if (currentLine != null) {
                lineProcess(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
