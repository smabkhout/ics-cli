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
        String d1 = "20230101";
        String d2 = "20240201";
        String d3 = "20250301";
        String d4 = "20251215";

        Event e3 = new Event(); 
        e3.setDtstart(d3);
        Todos t4 = new Todos(); 
        t4.setDueDate(d4);
        Event e1 = new Event(); 
        e1.setDtstart(d1);
        Todos t2 = new Todos(); 
        t2.setDueDate(d2);
        Event e5 = new Event();
        e5.setDtstart(null);
        Todos t6 = new Todos();
        t6.setDueDate(null);
        Event e7 = new Event();
        e7.setDtstart(null);

        // not in order
        calendar.addItem(e3);
        calendar.addItem(e1);
        calendar.addItem(t2);
        calendar.addItem(t4);
        calendar.addItem(e5);
        calendar.addItem(t6);

        calendar.sortCalendar();

        List<Entry> items = calendar.getItems();
        Assertions.assertEquals(e1, items.get(0));
        Assertions.assertEquals(t2, items.get(1));
        Assertions.assertEquals(e3, items.get(2));
        Assertions.assertEquals(t4, items.get(3));
        Assertions.assertEquals(e5, items.get(4));
        Assertions.assertEquals(t6, items.get(5));
    }


    @Test
    public void testSortEmptyCalendar() {

        calendar.sortCalendar();

        List<Entry> items = calendar.getItems();
        Assertions.assertTrue(items.isEmpty());
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

    @Test
    public void testInprocessFilter() {
        Todos tInprocess = new Todos(); 
        tInprocess.setStatus("IN-PROCESS");
        Todos tIncomplete = new Todos(); 
        tIncomplete.setStatus("NEEDS-ACTION");
        calendar.addItem(tInprocess);
        calendar.addItem(tIncomplete);
        List<String> options = new ArrayList<>();
        options.add("-inprocess");
        calendar.filterCalendar(options);
        Assertions.assertTrue(calendar.getItems().contains(tInprocess));
        Assertions.assertFalse(calendar.getItems().contains(tIncomplete));
    }

    @Test
    public void testNeedsactionFilter() {
        Todos tNeedsaction = new Todos(); 
        tNeedsaction.setStatus("NEEDS-ACTION");
        Todos tIncomplete = new Todos(); 
        tIncomplete.setStatus("COMPLETED");
        calendar.addItem(tNeedsaction);
        calendar.addItem(tIncomplete);
        List<String> options = new ArrayList<>();
        options.add("-needsaction");
        calendar.filterCalendar(options);
        Assertions.assertTrue(calendar.getItems().contains(tNeedsaction));
        Assertions.assertFalse(calendar.getItems().contains(tIncomplete));
    }

    @Test
    public void testTypeFilterTodos() {
        Event e1 = new Event(); 
        Todos t1 = new Todos(); 

        calendar.addItem(e1);
        calendar.addItem(t1);

        List<String> options = new ArrayList<>();
        options.add("todos");

        calendar.filterCalendar(options);

        Assertions.assertFalse(calendar.getItems().contains(e1));
        Assertions.assertTrue(calendar.getItems().contains(t1));
    }

    @Test
    public void testTypeFilterEvents() {
        Event e1 = new Event();
        e1.setDtstart("20240101");

        Todos t1 = new Todos();
        t1.setDueDate("20240101");

        calendar.addItem(e1);
        calendar.addItem(t1);

        List<String> options = new ArrayList<>();
        options.add("events");
        options.add("-from");
        options.add("20231231");
        options.add("-to");
        options.add("20250201");

        calendar.filterCalendar(options);

        Assertions.assertTrue(calendar.getItems().contains(e1));
        Assertions.assertFalse(calendar.getItems().contains(t1));
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

    @Test
    public void testTomorrowFilter() {
        String tomorrow = LocalDate.now().plusDays(1).format(fmt);
        Event e1 = new Event(); 
        e1.setDtstart(tomorrow);
        Event e2 = new Event(); 
        e2.setDtstart("20250101");

        calendar.addItem(e1);
        calendar.addItem(e2);

        List<String> options = new ArrayList<>();
        options.add("-tomorrow");

        calendar.filterCalendar(options);

        Assertions.assertTrue(calendar.getItems().contains(e1));
        Assertions.assertFalse(calendar.getItems().contains(e2));
    }

    @Test
    public void testWeekFilter() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = now.with(java.time.DayOfWeek.SUNDAY);
        Event e1 = new Event(); 
        e1.setDtstart(startOfWeek.format(fmt));
        Event e2 = new Event(); 
        e2.setDtstart(endOfWeek.format(fmt));
        Event e3 = new Event(); 
        e3.setDtstart(endOfWeek.plusDays(1).format(fmt));
        calendar.addItem(e1);
        calendar.addItem(e2);
        calendar.addItem(e3);
        List<String> options = new ArrayList<>();
        options.add("-week");
        calendar.filterCalendar(options);
        Assertions.assertTrue(calendar.getItems().contains(e1));
        Assertions.assertTrue(calendar.getItems().contains(e2));
        Assertions.assertFalse(calendar.getItems().contains(e3));
    }
}
