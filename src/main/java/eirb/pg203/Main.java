package eirb.pg203;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Args: " + Arrays.toString(args));
        // arguments manquants
        if (args.length < 1){
            System.out.println("Please provide a path to an ICS file as an argument.");
            System.out.println("Example: ./gradlew run --args=\"./src/test/resources/i2.ics events -from 20251208 -to 20251214\"");
            return;
        }
        String filePath = args[0];
        List<String> options = new ArrayList<>(); // liste des options (peut etre vide)
        if (args.length >= 2){
            options = Arrays.asList(args);
            options = options.subList(1, args.length);   
        }

        Parser parser = new Parser(filePath);

        parser.parse(); // remplissage de la liste des evenements du parser
        Calendar calendar = parser.getCalendar();
        calendar.sortCalendar();
        calendar.filterCalendar(options); //filtrer selon options

        ExporterFactory factory = new ExporterFactory();
        try {
            factory.outputHandler(options, calendar);    //generer l'output convenable
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}
