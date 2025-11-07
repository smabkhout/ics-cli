package eirb.pg203;

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
            while ((line = reader.readerLine()) != null) {
                if (line.startsWith(beginevent)) {
                    Event event = new Event();

                } else if (line.startsWith(endevent)) {
                    ++i; //on passe à l'evenement suivant
                } else if (line.startsWith(summaryprefix)) {
                    String summary = line.substring(summaryprefix.length());
                } else if (line.startsWith(dtstartprefix)) {
                    String dtstart = line.substring(dtstartprefix.length());
                } else if (line.startsWith(dtendprefix)) {
                    String dtend = line.substring(dtendprefix.length());
                } else if (line.startsWith(locationprefix)) {
                    String location = line.substring(locationprefix.length());
                } else if (line.startsWith(descriptionprefix)) {
                    String description = line.substring(descriptionprefix.length());
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}