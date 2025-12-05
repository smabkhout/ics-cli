public class exporteur{
    private formateur f;
    public exporteur(formateur f){
        this.f=f;
    }
    public void exporter(){
        public void exporter(List<   > L, String fichier) throws Exception {
        PrintWriter pw = new PrintWriter(fichier);  
        f.formatEntete(pw);

        for (ICS e : L) {
            f.formatEntry(pw, e);
        }

        f.formatPied(pw);

        pw.close();
        }
        }
    }
