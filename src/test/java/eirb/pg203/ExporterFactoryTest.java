package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.StringWriter;

public class ExporterFactoryTest {

    private ExporterFactory exporterFactory = new ExporterFactory();

    @BeforeEach
    public void setUp() {
        exporterFactory = new ExporterFactory();
    }

    @Test
    public void testCreateTextExporter() {
        Exporter exporter = exporterFactory.getExporter("-text");
        Assertions.assertTrue(exporter instanceof TextExporter);
    }

    @Test
    public void testCreateHtmlExporter() {
        Exporter exporter = exporterFactory.getExporter("-html");
        Assertions.assertTrue(exporter instanceof HtmlExporter);
    }

    @Test
    public void testCreateICSExporter() {
        Exporter exporter = exporterFactory.getExporter("-ics");
        Assertions.assertTrue(exporter instanceof ICSExporter);
    }

}

