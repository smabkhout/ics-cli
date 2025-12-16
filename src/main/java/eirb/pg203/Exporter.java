package eirb.pg203;

import java.io.IOException;
import java.io.Writer;


public interface Exporter{
    //fonction qui ecrit le calendrier cal dans output (html, text, ics)
    public void export(Calendar cal, Writer output) throws IOException;
}
