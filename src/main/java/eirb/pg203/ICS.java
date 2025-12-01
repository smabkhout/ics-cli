package eirb.pg203;


class ICS  {

    protected String summary;
    protected String location;

    ICS(){
        summary = "";
        location = "";
    }

    ICS(String summary, String location){
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