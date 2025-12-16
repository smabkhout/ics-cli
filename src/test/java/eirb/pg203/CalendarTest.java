package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarTest {

    private Calendar calendar;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

    @BeforeEach
    public void setUp() {
        calendar = new Calendar();
    }


    @Test
    public void testSortCalendar() {
        String d1 = "20240101";
        String d2 = "20240201";
        String d3 = "20240301";

        Event e3 = new Event(); 
        e3.setDtstart(d3);
        Event e1 = new Event(); 
        e1.setDtstart(d1);
        Todos t2 = new Todos(); 
        t2.setDueDate(d2);

        // not in order
        calendar.addItem(e3);
        calendar.addItem(e1);
        calendar.addItem(t2);

        calendar.sortCalendar();

        List<Entry> items = calendar.getItems();
        Assertions.assertEquals(e1, items.get(0));
        Assertions.assertEquals(t2, items.get(1));
        Assertions.assertEquals(e3, items.get(2));
    }

    //default filter 
    @Test
    public void testDefaultFilters() {
        String today = LocalDate.now().format(fmt);
        String tomorrow = LocalDate.now().plusDays(1).format(fmt);

        // doit rester
        Event eToday = new Event(); 
        eToday.setDtstart(today);
        // doit etre supprimé
        Event eTomorrow = new Event(); 
        eTomorrow.setDtstart(tomorrow);

        //doit rester
        Todos tIncomplete = new Todos(); 
        tIncomplete.setStatus("NEEDS-ACTION");

        Todos tComplete = new Todos(); 
        tComplete.setStatus("COMPLETED");

        calendar.addItem(eToday);
        calendar.addItem(eTomorrow);
        calendar.addItem(tIncomplete);
        calendar.addItem(tComplete);

        // doit appliquer filtre par defaut car liste vide
        calendar.filterCalendar(new ArrayList<>());

        List<Entry> items = calendar.getItems();
        Assertions.assertTrue(items.contains(eToday));
        Assertions.assertFalse(items.contains(eTomorrow));
        Assertions.assertTrue(items.contains(tIncomplete));
        Assertions.assertFalse(items.contains(tComplete));
    }

    // filtre completed
    @Test
    public void testCompletedFilter() {
        Todos tComplete = new Todos(); 
        tComplete.setStatus("COMPLETED");
        Todos tIncomplete = new Todos(); 
        tIncomplete.setStatus("IN-PROCESS");
        
        // we add an event to make sure the default filter today is applied
        String yesterday = LocalDate.now().minusDays(1).format(fmt);
        Event eOld = new Event(); 
        eOld.setDtstart(yesterday);

        calendar.addItem(tComplete);
        calendar.addItem(tIncomplete);
        calendar.addItem(eOld);

        List<String> options = new ArrayList<>();
        options.add("-completed");

        calendar.filterCalendar(options);

        Assertions.assertTrue(calendar.getItems().contains(tComplete));
        Assertions.assertFalse(calendar.getItems().contains(tIncomplete));
        Assertions.assertFalse(calendar.getItems().contains(eOld));
    }

    // from to
    @Test
    public void testFromToFilter() {
        Event e1 = new Event(); 
        e1.setDtstart("20240101");
        Event e2 = new Event(); 
        e2.setDtstart("20240201");
        Event e3 = new Event(); 
        e3.setDtstart("20240301");

        calendar.addItem(e1);
        calendar.addItem(e2);
        calendar.addItem(e3);

        List<String> options = new ArrayList<>();
        // from 20240115 to 20240215
        options.add("-from");
        options.add("20240115");
        options.add("-to");
        options.add("20240215");

        calendar.filterCalendar(options);

        Assertions.assertFalse(calendar.getItems().contains(e1));
        Assertions.assertTrue(calendar.getItems().contains(e2));
        Assertions.assertFalse(calendar.getItems().contains(e3));
    }
}
