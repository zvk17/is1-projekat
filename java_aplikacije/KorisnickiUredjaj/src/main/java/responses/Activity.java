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
@Root(name = "activity")
public class Activity {
    @Element(name = "destinationName", required = false)
    String destinationName;
    @Element(name = "durationSeconds")
    Integer durationSeconds; 
    @Element(name = "idActivity", required = false)
    Integer idActivity;
    @Element(name = "startDateTime")
    Date startDateTime;  

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    @Override
    public String toString() {
        return "Activity{" + "destinationName=" + destinationName + ", durationSeconds=" + durationSeconds + ", idActivity=" + idActivity + ", startDateTime=" + startDateTime + '}';
    }

    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ")
                .append(this.idActivity)
                .append(", Odrediste: ");
        if (this.destinationName == null) {
            sb.append("KUCA");
        } else {
            sb.append(destinationName);
        }
        OffsetDateTime odt = OffsetDateTime.ofInstant(startDateTime.toInstant(), ZoneId.systemDefault());
        
        sb.append(", Vreme: ")
            .append(odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        sb.append(", Traje: ")
           .append(durationSeconds)
           .append(" sekundi");
        return sb.toString();
    }
        
    
}
