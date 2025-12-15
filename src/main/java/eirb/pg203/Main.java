package eirb.pg203;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Args: " + Arrays.toString(args));
        if (args.length < 1){
            System.out.println("Please provide a path to an ICS file as an argument.");
            System.out.println("Example: ./gradlew run --args=\"./src/test/resources/i2.ics event\"");
            return;
        }
        String filePath = args[0];
        List<String> filterOptions = new ArrayList<>();
        List<String> exportOptions = new ArrayList<>();
        if (args.length >= 2){
            List <String> options = Arrays.asList(args);
            filterOptions = options.subList(1, args.length); 
            for (int i=0; i<args.length; i++){
                if (args[i].equals("text") || args[i].equals("html") || args[i].equals("ics")){
                    exportOptions = options.subList(i, args.length);
                    filterOptions = options.subList(1, i);
                    break;
                }
            }   
        }


        Parser parser = new Parser(filePath);

        parser.parse(); // remplissage de la liste des evenements du parser
        Calendar calendar = parser.getCalendar();
        calendar.sortCalendar();
        calendar.filterCalendar(filterOptions);

        ExporterFactory factory = new ExporterFactory();
        factory.outputHandler(exportOptions, calendar);    
    }
}
