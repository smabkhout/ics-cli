package eirb.pg203;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Event {
    private String summary;
    private String dtstart;
    private String dtend;
    private String location;
    private String description;
    Event() {
        this.summary = "";
        this.dtstart = "";
        this.dtend = "";
        this.location = "";
        this.description = "";
    }

    Event (String summary, String dtstart, String dtend, String location, String description) {
        this.summary = summary;
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.location = location;
        this.description = description;
    }
    public String getSummary() {
        return summary;
    }
    public String getDtstart() {
        return dtstart;
    }
    public String getDtend() {
        return dtend;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setDtstart(String dtstart) {
        this.dtstart = dtstart;
    }
    public void setDtend(String dtend) {
        this.dtend = dtend;
    }
    public void setLocation(String location) {
        this.location = location;
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


        description = description.replace("\\n", "\n");


        return "Event [\n" + Green + "Summary" + Reset + ": " + summary + Green + "\nStart date: " + Reset + startTime + Green + "\nEnd date: " + Reset + endTime + Green + "\nLocation: " + Reset + location
                + Green + "\nDescription: " + Reset + description + "\n]";
    }
    
}