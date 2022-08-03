/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author user2
 */
@Root(name = "moment")
public class Moment {
    
    @Element(name = "date-time")
    private Date dateTime;
    
    @Element(name = "id")
    private int id;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public Moment() {

    }
    

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID: " + id + " ; DATE-TIME: " + dateTime;
    }
    

}
