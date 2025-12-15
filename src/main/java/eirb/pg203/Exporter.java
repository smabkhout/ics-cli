package eirb.pg203;

import java.io.IOException;
import java.io.Writer;


public interface Exporter{
    
    public void export(Calendar cal, Writer output) throws IOException;
}
