package eirb.pg203;

public class EventParser extends Parser<Event> {

    public EventParser(String path) {
        super(path);
    }

    @Override
    protected void lineProcess(String line) {
        String beginevent = "BEGIN:VEVENT";
        String endevent = "END:VEVENT";
        String summaryprefix = "SUMMARY:";
        String dtstartprefix = "DTSTART:";
        String dtendprefix = "DTEND:";
        String locationprefix = "LOCATION:";
        String descriptionprefix = "DESCRIPTION:";
        if (line.equals(beginevent)) {
            Event event = new Event();
            this.ICSs.add(event);
        } else if (!this.ICSs.isEmpty() && this.currentCursor >= 0) { // on s'assure qu'on a commencé au moins un event
            if (line.equals(endevent)) {
                this.currentCursor++;
            } else if (line.startsWith(summaryprefix)) {
                String summary = line.substring(summaryprefix.length()); // on lit la valeur de summary
                this.ICSs.get(currentCursor).setSummary(summary); // on la mets sur notre event
            } else if (line.startsWith(dtstartprefix)) {
                String dtstart = line.substring(dtstartprefix.length());
                this.ICSs.get(currentCursor).setDtstart(dtstart);
            } else if (line.startsWith(dtendprefix)) {
                String dtend = line.substring(dtendprefix.length());
                this.ICSs.get(currentCursor).setDtend(dtend);
            } else if (line.startsWith(locationprefix)) {
                String location = line.substring(locationprefix.length());
                this.ICSs.get(currentCursor).setLocation(location);
            } else if (line.startsWith(descriptionprefix)) {
                String description = line.substring(descriptionprefix.length());
                this.ICSs.get(currentCursor).setDescription(description);
            }
        }
    }

}
