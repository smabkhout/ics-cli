class Parser{
    String path;
    List<? extends ICS> ICSs;
    public Parser(string path){
        this.path=path;
        this.ICSs= new ArrayList<>();
    }
    List<? extends ICS> getICS(){
        return ICSs;
    }
    public void parse() {
        
        List<Integer> i = new ArrayList<>(); //indice curseur (à incrémenter après chaque event)
        i.add(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            String line;
            String currentLine = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith((" ")) || line.startsWith("\t")){
                    currentLine += line.substring(1);
                    continue;
                }

                if (currentLine != null){
                    lineProcess(currentLine, i);
                }               
                currentLine = line;
            }
            if (currentLine != null){
                lineProcess(currentLine, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}