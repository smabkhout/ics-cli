        package eirb.pg203;

        import java.io.BufferedReader; // Pour lire le texte de manière optimisée
        import java.io.FileReader;     // Pour lire un fichier caractère par caractère
        import java.io.IOException;  // L'exception que la lecture de fichier peut lever
        import java.util.List;
        import java.util.ArrayList;

public class ICSparser {
    String path;
    List<Event> events;

    public ICSparser(String path) {
        this.path = path;
        this.events = new ArrayList<>();
    }

    public List<Event> getEvents() {
        return events;
    }

    public void parse() {
        String beginevent = "BEGIN:VEVENT";
        String endevent = "END:VEVENT";
        String summaryprefix = "SUMMARY:";
        String dtstartprefix = "DTSTART:";
        String dtendprefix = "DTEND:";
        String locationprefix = "LOCATION:";
        String descriptionprefix = "DESCRIPTION:";
        int i = 0; //indice curseur (à incrémenter après chaque event)

        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(beginevent)) {
                    Event event = new Event();
                    this.events.add(event);

                } else if (line.equals(endevent)) {
                    ++i; //on passe à l'evenement suivant

                } else if (line.startsWith(summaryprefix)) {
                    String summary = line.substring(summaryprefix.length()); // on lit la valeur de summary
                    this.events.get(i).setSummary(summary); // on la mets sur notre event
                } else if (line.startsWith(dtstartprefix)) {
                    String dtstart = line.substring(dtstartprefix.length());
                    this.events.get(i).setDtstart(dtstart);
                } else if (line.startsWith(dtendprefix)) {
                    String dtend = line.substring(dtendprefix.length());
                    this.events.get(i).setDtend(dtend);
                } else if (line.startsWith(locationprefix)) {
                    String location = line.substring(locationprefix.length());
                    this.events.get(i).setLocation(location);
                } else if (line.startsWith(descriptionprefix)) {
                    String description = line.substring(descriptionprefix.length());
                    this.events.get(i).setDescription(description);
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}