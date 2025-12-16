package eirb.pg203;


class Entry  {

    protected String summary;
    protected String location;
    protected String uid;
    protected String dtstamp;

    Entry(){
        summary = "";
        location = "";
        uid ="";
        dtstamp="";
    }

    Entry(String summary, String location, String uid, String dtstamp){
        this.summary = summary;
        this.location = location;
        this.uid = uid;
        this.dtstamp=dtstamp;
    }

    public String getSummary() {
        return summary;
    }
    public String getLocation() {
        return location;
    }

    public String getUID() {
        return uid;
    }

    public String getDtstamp() {
        return dtstamp;
    }

    public void setDtstamp(String dtstamp) {
        this.dtstamp = dtstamp;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setLocation(String location) {
        this.location = location;
    }

}