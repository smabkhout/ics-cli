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

        ICSparser parser = new ICSparser("src/test/resources/i2.ics"); //creation du parser avec le path
        parser.parse(); // remplissage de la liste des evenements du parser
        for (Event event : parser.getEvents()) { // affichage des evenements
            System.out.println("--------NEW EVENT--------");
            System.out.println(event);
        }
    }

    public static String loadCalendarData(Path path) throws IOException {
        return Files.readString(path)
            .lines()
            .limit(20)
            .collect(Collectors.joining("\n"));
    }
}
