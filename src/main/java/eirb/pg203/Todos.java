package eirb.pg203;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;


class Todos extends ICS{
    
    protected String progress;
    protected String dueDate;
    protected String status;    
    protected String priority;
    
    Todos() {
        super();
        this.dueDate = "";
        this.progress = "";
        this.status = "";
        this.priority = "";
    }
    Todos (String summary, String location, String duedate, String progress, String status, String priority) {
        super(summary, location);
        this.dueDate = duedate;
        this.progress = progress;
        this.status = status;
        this.priority = priority;
    }

    public String getdueDate() {
        return dueDate;
    }
    public String getprogress() {
        return progress;
    }
    public String getStatus() {
        return status;
    }
    public String getPriority() {
        return priority;
    }

    
    
    public void setDueDate(String dDate) {
        this.dueDate = dDate;
    }
    public void setStatus(String status) {
        this.status = status;
    }  
    public void setprogress(String progress) {
        this.progress = progress;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        String Green = "\033[32m";
        String Reset = "\033[0m";

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date dueTime = null;
        try {
            dueTime = dateFormat.parse(dueDate);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "Todos [\n" + Green + "Summary" + Reset + ": " + summary + Green + "\nDue date: " + Reset + dueTime + Green + "\nStatus: " + Reset + status + Green + "\nLocation: " + Reset + location
                + "\nPriority" + Reset + ": " + priority + "\nProgress: " + Reset + progress + "\n]";
    }
    
}

