package eirb.pg203;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Args: " + Arrays.toString(args));
        // System.out.println(loadCalendarData(Path.of("src", "test", "resources", "i2.ics")));
        if (args.length < 2){
            System.out.println("Please provide a path to an ICS file as an argument as well as the type of the elements to be parsed.");
            System.out.println("Example: ./gradlew run --args=\\\"./src/test/resources/i2.ics event\\\\");
            return;
        }
        String filePath = args[0];
        String type = args[1];
        String option = null;
        Parser<? extends ICS> parser;

        if (type.equalsIgnoreCase("event")) {
            parser = new EventParser(filePath);
            option = "today";
        } else if (type.equalsIgnoreCase("todo")) {
            parser = new TodoParser(filePath);
            option = "incomplete";
        } else {
            System.out.println("Unknown type: " + type);
            return;
        }

        if (args.length==3){
            option = args[2];
        }

        parser.parse(); // remplissage de la liste des evenements du parser
        parser.icssSort(); //ordonner la liste des evenements par (dueDate ou dtStart)
        parser.icsFilter(option); //filtrer la liste selon l'option passé
        for (ICS ics : parser.getICSs()) { // affichage des evenements
            System.out.println("--------NEW " +type.toUpperCase() + "--------");
            System.out.println(ics);
        }
    }

    public static String loadCalendarData(Path path) throws IOException {
        return Files.readString(path)
            .lines()
            .limit(20)
            .collect(Collectors.joining("\n"));
    }
}
