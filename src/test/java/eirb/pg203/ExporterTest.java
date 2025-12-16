package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

public class ExporterTest {

    @Test
    public void testTextExporter() throws IOException {
        Calendar cal = new Calendar();
        Todos t = new Todos();
        t.setSummary("Soutenance POO");
        cal.addItem(t);

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

        Todos t = new Todos();
        t.setSummary("Faire les devoirs");
        cal.addItem(t);

        StringWriter tmpWriter = new StringWriter();
        HtmlExporter exporter = new HtmlExporter();

        exporter.export(cal, tmpWriter);
        String output = tmpWriter.toString();

        Assertions.assertTrue(output.startsWith("<!DOCTYPE html>\n<html>\n<body>\n<ul>\n"));
        Assertions.assertTrue(output.contains("<li>"));
        Assertions.assertTrue(output.contains("Soutennce POO"));
        Assertions.assertTrue(output.contains("Ligne 1<br>Ligne 2"));
        Assertions.assertTrue(output.contains("Faire les devoirs"));
        Assertions.assertTrue(output.endsWith("</ul>\n</body>\n</html>"));
    }

    @Test
    public void testICSExporter() throws IOException {
        Calendar cal = new Calendar();
        Event e = new Event();
        e.setSummary("TestICS");
        e.setDtstart("20240101");
        String longDescription = "This is a very long description that is intended to test the line folding functionality of the ICS exporter. "
                + "According to the ICS specification, lines longer than 75 characters should be folded by inserting a CRLF followed by a space or tab. "
                + "This description should be long enough to trigger that behavior in the exporter implementation.";
        e.setDescription(longDescription);
        cal.addItem(e);

        StringWriter tmpWriter = new StringWriter();
        ICSExporter exporter = new ICSExporter();

        exporter.export(cal, tmpWriter);
        String output = tmpWriter.toString();

        Assertions.assertTrue(output.startsWith("BEGIN:VCALENDAR"));
        Assertions.assertTrue(output.contains("BEGIN:VEVENT"));
        Assertions.assertTrue(output.contains("SUMMARY:TestICS"));
        Assertions.assertTrue(output.contains("DTSTART:20240101"));
        Assertions.assertTrue(output.contains("DESCRIPTION:This is a very long description that"));
        Assertions.assertTrue(output.contains("END:VEVENT"));
        Assertions.assertTrue(output.endsWith("END:VCALENDAR\r\n"));
    }
}


