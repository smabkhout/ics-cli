package eirb.pg203;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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

    @Override
    public void icssSort(){
        this.ICSs.sort(( a,  b) -> a.getDtstart().compareTo(b.getDtstart()));
    }

    @Override
    public void icsFilter(List<String> options){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        switch (options.get(0)) {
            case "tomorrow":
                String tomorrow = LocalDate.now().plusDays(1).format(formatter);
                this.ICSs.removeIf((Event E)-> (!(E.getDtstart().substring(0, 8).equals(tomorrow))));
                break;
                
            case "week":
                String firstDay = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(formatter);
                String lastDay = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(formatter);

                this.ICSs.removeIf((Event E)-> ((E.getDtstart().substring(0, 8).compareTo(firstDay))<0));
                this.ICSs.removeIf((Event E)-> ((E.getDtstart().substring(0, 8).compareTo(lastDay))>0));
                break;
            
            case "from":
                String from = options.get(1);
                this.ICSs.removeIf((Event E)-> ((E.getDtstart().substring(0, 8).compareTo(from))<0));
                if (options.contains("to")){
                    int i = options.indexOf("to");
                    String to1 = options.get(i+1);
                    this.ICSs.removeIf((Event E)-> ((E.getDtstart().substring(0, 8).compareTo(to1))>0));
                }
                break;
            
            case "to":
                String to = options.get(1);
                this.ICSs.removeIf((Event E)-> ((E.getDtstart().substring(0, 8).compareTo(to))>0));
                if (options.contains("from")){
                    int i = options.indexOf("from");
                    String from1 = options.get(i+1);
                    this.ICSs.removeIf((Event E)-> ((E.getDtstart().substring(0, 8).compareTo(from1))<0));
                }
                break;
            
            default: // par defaut on affiche les evenements d'aujourd'hui
                String today = LocalDate.now().format(formatter);
                this.ICSs.removeIf((Event E)-> (!(E.getDtstart().substring(0, 8).equals(today))));
                break;
        }
    }
}
