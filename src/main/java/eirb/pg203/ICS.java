package eirb.pg203;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

class ICS  {

    protected String summary;
    protected String location;

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