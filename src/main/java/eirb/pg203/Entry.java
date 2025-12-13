package eirb.pg203;


class Entry  {

    protected String summary;
    protected String location;

    Entry(){
        summary = "";
        location = "";
    }

    Entry(String summary, String location){
        this.summary = summary;
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }
    public String getLocation() {
        return location;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setLocation(String location) {
        this.location = location;
    }

}