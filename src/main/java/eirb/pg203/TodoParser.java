package eirb.pg203;

import java.util.List;

public class TodoParser extends Parser<Todos> {

    public TodoParser(String path) {
        super(path);
    }

    @Override
    protected void lineProcess(String line) {
        String begintodo = "BEGIN:VTODO";
        String endtodo = "END:VTODO";
        String summaryprefix = "SUMMARY:";
        String dueDateprefix = "DUE;VALUE=DATE:";
        String statusprefix = "STATUS:";
        String locationprefix = "LOCATION:";
        String progressprefix = "PERCENT-COMPLETE:";

        if (line.equals(begintodo)) {
            Todos todo = new Todos();
            this.ICSs.add(todo);
        } else if (!this.ICSs.isEmpty() && this.currentCursor >= 0) { // on s'assure qu'on a commencé au moins un event
            if (line.equals(endtodo)) {
                this.currentCursor++;
            } else if (line.startsWith(summaryprefix)) {
                String summary = line.substring(summaryprefix.length()); // on lit la valeur de summary
                this.ICSs.get(currentCursor).setSummary(summary); // on la mets sur notre event
            } else if (line.startsWith(dueDateprefix)) {
                String dueDate = line.substring(dueDateprefix.length());
                this.ICSs.get(currentCursor).setDueDate(dueDate);
            } else if (line.startsWith(statusprefix)) {
                String status = line.substring(statusprefix.length());
                this.ICSs.get(currentCursor).setStatus(status);
            } else if (line.startsWith(locationprefix)) {
                String location = line.substring(locationprefix.length());
                this.ICSs.get(currentCursor).setLocation(location);
            } else if (line.startsWith(progressprefix)) {
                String progress = line.substring(progressprefix.length());
                this.ICSs.get(currentCursor).setProgress(progress);
            }
        }
    }
    
    public void icssSort(){
        this.ICSs.sort((a, b)->a.getdueDate().compareTo(b.getdueDate()));
    }
    
    public void icsFilter(List<String> options){

        switch (options.get(0)) {
            case "all":
                this.ICSs.removeIf((Todos T) -> (false));
                break;
            
            case "complete":
                this.ICSs.removeIf((Todos T)-> (!(T.getStatus().equals("COMPLETED"))));
                break;
            
            case "inprocess":
                this.ICSs.removeIf((Todos T)-> (!(T.getStatus().equals("IN-PROCESS"))));
                break;
            
            case "needsaction":
                this.ICSs.removeIf((Todos T)-> (!(T.getStatus().equals("NEEDS-ACTION"))));
                break;
        
            default: // par defaut on affiche les todos incomplet
                this.ICSs.removeIf((Todos T)-> ((T.getStatus().equals("COMPLETED"))));
                break;
        }
    }
}
