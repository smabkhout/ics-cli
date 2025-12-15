package eirb.pg203;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Todos extends Entry {

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

    Todos(String summary, String location, String duedate, String progress, String status, String priority, String uid, String dtstamp) {
        super(summary, location, uid, dtstamp);
        this.dueDate = duedate;
        this.progress = progress;
        this.status = status;
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getProgress() {
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

    public void setProgress(String progress) {
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
        Date dueTime;
        String due = dueDate;
        if (dueDate.isEmpty()) {
            due = "No specified due date!";          
        } else {
            try {
                dueTime = dateFormat.parse(dueDate);
                SimpleDateFormat prettyFormat = new SimpleDateFormat("dd/MM/yyyy");
                due = prettyFormat.format(dueTime);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }

        String locaString = location;
        if (locaString.isEmpty()) {
            locaString = "No specified location!";          
        }

        String chaine = "Todo [\n" +
               Green + "  Task      : " + Reset + summary + "\n" +
               Green + "  Due Date  : " + Reset + due + "\n" +
               Green + "  Status    : " + Reset + status + "\n" +
               Green + "  Progress  : " + Reset + progress + "\n" +
               Green + "  Location  : " + Reset + locaString + "\n" +
               "]\n";
        return chaine;
    }

}
