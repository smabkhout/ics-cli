package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TodosTest {

    @Test
    public void testDefaultConstructor() {
        Todos todo = new Todos();
        
        Assertions.assertEquals("", todo.getDueDate());
        Assertions.assertEquals("", todo.getProgress());
        Assertions.assertEquals("", todo.getStatus());
        Assertions.assertEquals("", todo.getPriority());
        Assertions.assertEquals("", todo.getSummary());
        Assertions.assertEquals("", todo.getLocation());
        Assertions.assertEquals("", todo.getUID());
    }

    @Test
    public void testParamConstructor() {
        Todos todo = new Todos(
            "Rendre projet",  // summary
            "Bureau",         // location
            "20050318",       // duedate (format yyyyMMdd)
            "50",             // progress
            "IN-PROCESS",     // status
            "1",              // priority
            "uid987",         // uid
            "19990101T090000" // dtstamp
        );

        Assertions.assertEquals("Rendre projet", todo.getSummary());
        Assertions.assertEquals("Bureau", todo.getLocation());
        Assertions.assertEquals("20050318", todo.getDueDate());
        Assertions.assertEquals("50", todo.getProgress());
        Assertions.assertEquals("IN-PROCESS", todo.getStatus());
        Assertions.assertEquals("1", todo.getPriority());
        Assertions.assertEquals("uid987", todo.getUID());
        Assertions.assertEquals("19990101T090000", todo.getDtstamp());
    }

    @Test
    public void testSettersGetters() {
        Todos todo = new Todos();
        
        todo.setDueDate("20050318");
        todo.setStatus("COMPLETED");
        todo.setProgress("100");
        todo.setPriority("5");
        
        Assertions.assertEquals("20050318", todo.getDueDate());
        Assertions.assertEquals("COMPLETED", todo.getStatus());
        Assertions.assertEquals("100", todo.getProgress());
        Assertions.assertEquals("5", todo.getPriority());
    }

    @Test
    public void testToString() {
        Todos todo = new Todos();
        todo.setSummary("Acheter lait");
        todo.setLocation("Supermarche");
        todo.setDueDate("20251231"); 
        todo.setStatus("NEEDS-ACTION");
        todo.setProgress("0");

        String output = todo.toString();

        Assertions.assertTrue(output.contains("Acheter lait"));
        Assertions.assertTrue(output.contains("Supermarche"));
        Assertions.assertTrue(output.contains("31/12/2025")); 
        Assertions.assertTrue(output.contains("NEEDS-ACTION"));
    }
    
    @Test
    public void testToStringEmpty() {
        Todos todo = new Todos();

        String output = todo.toString();

        Assertions.assertTrue(output.contains("No specified due date!"));
        Assertions.assertTrue(output.contains("No specified location!"));
    }
}