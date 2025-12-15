package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void testDefaultConstructor() {
        Event event = new Event();
        
        Assertions.assertEquals("", event.getDtstart());
        Assertions.assertEquals("", event.getDtend());
        Assertions.assertEquals("", event.getDescription());
        Assertions.assertEquals("", event.getSummary());
        Assertions.assertEquals("", event.getLocation());
    }
    @Test
    public void testParamConstructor() {
        Event event = new Event(
            "Reunion",       // summary
            "Salle 101",     // location
            "20050318T083000", // dtstart
            "20251318T104500", // dtend
            "Description",   // description
            "uid123",        // uid
            "20230901T120000" // dtstamp
        );

        Assertions.assertEquals("Reunion", event.getSummary());
        Assertions.assertEquals("Salle 101", event.getLocation());
        Assertions.assertEquals("20050318T083000", event.getDtstart());
        Assertions.assertEquals("20251318T104500", event.getDtend());
        Assertions.assertEquals("Description", event.getDescription());
        Assertions.assertEquals("uid123", event.getUID());
        Assertions.assertEquals("20230901T120000", event.getDtstamp());
    }

    @Test
    public void testSettersGetters() {
        Event event = new Event();
        
        event.setDtstart("20050318T000000");
        event.setDtend("20250318T000000");
        event.setDescription("Ma description des descriptions");
        
        Assertions.assertEquals("20050318T000000", event.getDtstart());
        Assertions.assertEquals("20250318T000000", event.getDtend());
        Assertions.assertEquals("Ma description des descriptions", event.getDescription());
    }

    @Test
    public void testToString() {
        Event event = new Event();
        event.setSummary("Daily");
        event.setLocation("Zoom");
        event.setDtstart("20050318T000000"); 
        event.setDtend("20250318T000000");
        event.setDescription("Noel");

        String output = event.toString();

        Assertions.assertTrue(output.contains("Daily"));
        Assertions.assertTrue(output.contains("Zoom"));
        Assertions.assertTrue(output.contains("2005")); 
        Assertions.assertTrue(output.contains("Noel"));
    }
}