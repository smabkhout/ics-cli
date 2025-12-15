package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseSimpleEvent() throws IOException {
        Path icsFile = Files.createTempFile("event_test", ".ics"); //creer un fichier temporaire
        icsFile.toFile().deleteOnExit(); //le supprimer a la fin du programme

        String content = "BEGIN:VCALENDAR\n" +
                         "BEGIN:VEVENT\n" +
                         "SUMMARY:Reunion tres tres Importante\n" +
                         "LOCATION:SalleSerieuse\n" +
                         "DTSTART:20261020T100000\n" +
                         "UID:uid-12345\n" +
                         "END:VEVENT\n" +
                         "END:VCALENDAR";
        Files.writeString(icsFile, content);

        Parser parser = new Parser(icsFile.toString());
        parser.parse();
        
        List<Entry> items = parser.getCalendar().getItems();
        Assertions.assertEquals(1, items.size());
        
        Entry entry = items.get(0);
        Assertions.assertTrue(entry instanceof Event);
        Assertions.assertEquals("Reunion tres tres Importante", entry.getSummary());
        Assertions.assertEquals("SalleSerieuse", entry.getLocation());
        Assertions.assertEquals("uid-12345", entry.getUID());
    }

    @Test
    public void testParseTodo() throws IOException {
        Path icsFile = Files.createTempFile("todo_test", ".ics");
        icsFile.toFile().deleteOnExit();

        String content = "BEGIN:VCALENDAR\n" +
                         "BEGIN:VTODO\n" +
                         "SUMMARY:Faire a manger\n" +
                         "STATUS:NEEDS-ACTION\n" +
                         "DUE;VALUE=DATE:20051231\n" +
                         "END:VTODO\n" +
                         "END:VCALENDAR";
        Files.writeString(icsFile, content);

        Parser parser = new Parser(icsFile.toString());
        parser.parse();
        
        List<Entry> items = parser.getCalendar().getItems();
        Assertions.assertEquals(1, items.size());
        
        Entry entry = items.get(0);
        Assertions.assertTrue(entry instanceof Todos);
        
        Todos todo = (Todos) entry;
        Assertions.assertEquals("Faire a manger", todo.getSummary());
        Assertions.assertEquals("NEEDS-ACTION", todo.getStatus());
        Assertions.assertEquals("20051231", todo.getDueDate());
    }

    // 3. Test du "Unfolding" (Lignes coupées)
    @Test
    public void testLineUnfolding() throws IOException {
        Path icsFile = Files.createTempFile("test_desc", ".ics");
        icsFile.toFile().deleteOnExit();

        String content = "BEGIN:VCALENDAR\n" +
                         "BEGIN:VEVENT\n" +
                         "SUMMARY:Titre vraiment Court\n" +
                         "DESCRIPTION:Ceci est une des\n" +
                         " cription tres lon\n" + 
                         " gue qui a ete coupee.\n" +
                         "END:VEVENT\n" +
                         "END:VCALENDAR";
        Files.writeString(icsFile, content);

        Parser parser = new Parser(icsFile.toString());
        parser.parse();
        
        Event event = (Event) parser.getCalendar().getItems().get(0);
        
        String expected = "Ceci est une description tres longue qui a ete coupee.";
        Assertions.assertEquals(expected, event.getDescription());
    }

    @Test
    public void testMixedContent() throws IOException {
        Path icsFile = Files.createTempFile("mixed_test", ".ics");
        icsFile.toFile().deleteOnExit();

        String content = "BEGIN:VCALENDAR\n" +
                         "BEGIN:VEVENT\n" +
                         "SUMMARY:Event 1\n" +
                         "END:VEVENT\n" +
                         "BEGIN:VTODO\n" +
                         "SUMMARY:Task 1\n" +
                         "END:VTODO\n" +
                         "END:VCALENDAR";
        Files.writeString(icsFile, content);

        Parser parser = new Parser(icsFile.toString());
        parser.parse();
        
        List<Entry> items = parser.getCalendar().getItems();
        Assertions.assertEquals(2, items.size());
        Assertions.assertTrue(items.get(0) instanceof Event);
        Assertions.assertTrue(items.get(1) instanceof Todos);
    }
}