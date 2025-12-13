package eirb.pg203;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Args: " + Arrays.toString(args));
        // System.out.println(loadCalendarData(Path.of("src", "test", "resources", "i2.ics")));
        if (args.length < 1){
            System.out.println("Please provide a path to an ICS file as an argument.");
            System.out.println("Example: ./gradlew run --args=\\\"./src/test/resources/i2.ics event\\\\");
            return;
        }
        String filePath = args[0];
        List<String> options = new ArrayList<>();
        if (args.length >= 2){
            options = Arrays.asList(args);
            options = options.subList(1, args.length);
        }
        else {
            options.add("no options");
        }
        Parser parser = new Parser(filePath);

        parser.parse(); // remplissage de la liste des evenements du parser
        Calendar calendar = parser.getCalendar();
        calendar.sortCalendar();
        calendar.filterCalendar(options);
        String type;
        for (Entry entry : calendar.getItems()) { // affichage des evenements
            if (entry instanceof Event){
                type = "event";
            }
            else {
                type = "todo";
            }
            System.out.println("--------NEW " +type.toUpperCase() + "--------");
            System.out.println(entry);
        }
    }

    public static String loadCalendarData(Path path) throws IOException {
        return Files.readString(path)
            .lines()
            .limit(20)
            .collect(Collectors.joining("\n"));
    }
}
