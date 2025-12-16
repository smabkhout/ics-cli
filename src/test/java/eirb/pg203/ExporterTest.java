package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

public class ExporterTest {

    @Test
    public void testTextExporter() throws IOException {
        Calendar cal = new Calendar();
        Event e = new Event();
        e.setSummary("Soutenance POO");
        cal.addItem(e);

        StringWriter tmpWriter = new StringWriter();
        TextExporter exporter = new TextExporter();

        exporter.export(cal, tmpWriter);

        String output = tmpWriter.toString();
        
        Assertions.assertTrue(output.contains("Soutenance POO"));
    }

    @Test
    public void testHtmlExporter() throws IOException {
        Calendar cal = new Calendar();
        Event e = new Event();
        e.setSummary("Soutennce POO");
        e.setDescription("Ligne 1\nLigne 2");
        cal.addItem(e);

        StringWriter tmpWriter = new StringWriter();
        HtmlExporter exporter = new HtmlExporter();

        exporter.export(cal, tmpWriter);
        String output = tmpWriter.toString();

        Assertions.assertTrue(output.startsWith("<!DOCTYPE html>\n<html>\n<body>\n<ul>\n"));
        Assertions.assertTrue(output.contains("<li>"));
        Assertions.assertTrue(output.contains("<br>"));
        Assertions.assertTrue(output.contains("Soutennce POO"));
        Assertions.assertTrue(output.endsWith("</ul>\n</body>\n</html>"));
    }

    @Test
    public void testICSExporter() throws IOException {
        Calendar cal = new Calendar();
        Event e = new Event();
        e.setSummary("TestICS");
        e.setDtstart("20240101");
        cal.addItem(e);

        StringWriter tmpWriter = new StringWriter();
        ICSExporter exporter = new ICSExporter();

        exporter.export(cal, tmpWriter);
        String output = tmpWriter.toString();

        Assertions.assertTrue(output.startsWith("BEGIN:VCALENDAR"));
        Assertions.assertTrue(output.contains("BEGIN:VEVENT"));
        Assertions.assertTrue(output.contains("SUMMARY:TestICS"));
        Assertions.assertTrue(output.contains("END:VEVENT"));
        Assertions.assertTrue(output.endsWith("END:VCALENDAR\r\n"));
    }
}


