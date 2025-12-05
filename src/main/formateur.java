import java.io.PrintWriter;
public interface formateur{
    void formatEntete(PrintWriter p);
    void formatEvent(PrintWriter p,ICS c);
    Pvoid formatPieds(PrintWriter p);
}