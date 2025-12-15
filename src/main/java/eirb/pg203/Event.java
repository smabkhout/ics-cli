package eirb.pg203;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event extends Entry{

    protected String dtstart;
    protected String dtend;
    protected String description;

    Event() {
        super();
        this.dtstart = "";
        this.dtend = "";
        this.description = "";
    }

    Event(String summary, String location, String dtstart, String dtend, String description) {
        super(summary, location);
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.description = description;
    }

    public String getDtstart() {
        return dtstart;
    }
    public String getDtend() {
        return dtend;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDtstart(String dtstart) {
        this.dtstart = dtstart;
    }
    public void setDtend(String dtend) {
        this.dtend = dtend;
    } 
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String Green = "\033[32m";
        String Reset = "\033[0m";

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = dateFormat.parse(dtstart);
            endTime = dateFormat.parse(dtend);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String locaString = location;
        if (locaString.isEmpty()) {
            locaString = "No specified location!";          
        }

        description = description.replace("\\n", "\n");

        String chaine = "Event [\n" +
               Green + "  Summary      : " + Reset + summary + "\n" +
               Green + "  Start date  : " + Reset + startTime + "\n" +
               Green + "  End date    : " + Reset + endTime + "\n" +
               Green + "  Location  : " + Reset + locaString + "\n" +
               Green + "  Description  : " + Reset + description + "\n" +
               "]\n";
        return chaine;
    }
    
}