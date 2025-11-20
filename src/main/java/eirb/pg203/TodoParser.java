        package eirb.pg203;

        import java.io.BufferedReader; // Pour lire le texte de manière optimisée
        import java.io.FileReader;     // Pour lire un fichier caractère par caractère
        import java.io.IOException;  // L'exception que la lecture de fichier peut lever
        import java.util.List;
        import java.util.ArrayList;

public class Todoparser extends Parser {
    List<Todos> todos;

    public List<Todos> getICS() {
        return todos;
    }
    private void lineProcess(String line, List<Integer> i) {
        String begintodo = "BEGIN:VTODO";
        String endtodo = "END:VTODO";
        String summaryprefix = "SUMMARY:";
        String dueDateprefix = "DUE;";
        String statutprefix="STATUS:";
        String locationprefix = "LOCATION:";
        String progressprefix="PRECENT-COMPLETE:";
        String descriptionprefix = "DESCRIPTION:";

        if (line.equals(begintodo)) {

            Todos t = new Todos();
            this.todos.add(t);

        } else if (line.equals(endtodo)) {
            i.set(0, i.get(0) + 1); // on passe à l'evenement suivant

        } else if (line.startsWith(summaryprefix)) {
            String summary = line.substring(summaryprefix.length()); // on lit la valeur de summary
            this.todos.get(i.get(0)).setSummary(summary); // on la mets sur notre event
        } else if (line.startsWith(dueDateprefix)) {
            String dueDate = line.substring(dueDateprefix.length());
            this.todos.get(i.get(0)).setDueDate(dueDate);
        } else if (line.startsWith(locationprefix)) {
            String location = line.substring(locationprefix.length());
            this.todos.get(i.get(0)).setLocation(location);
        } else if (line.startsWith(descriptionprefix)) {
            String description = line.substring(descriptionprefix.length());
            this.todos.get(i.get(0)).setDescription(description);
        } else if (line.startsWith(statutprefix)) {
            String status = line.substring(statutprefix.length());
            this.todos.get(i.get(0)).setStatus(status);
        } else if (line.startsWith(progressprefix)) {
            String progress = line.substring(progressprefix.length());
            this.todos.get(i.get(0)).setStatus(progress);
        }
    }
}