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
@Root(name = "alarm")
public class Alarm {
    @Element(name = "idAlarm", required = false)
    private Integer idAlarm;
    @Element(name = "intervalSeconds", required = false)
    private Integer intervalSeconds;
    
    @Element(name ="datetimeMoment")
    private Date datetimeMoment;

    public Integer getIdAlarm() {
        return idAlarm;
    }

    public Alarm() {
    }

    public void setIdAlarm(Integer idAlarm) {
        this.idAlarm = idAlarm;
    }

    public Integer getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(Integer intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public Date getDatetimeMoment() {
        return datetimeMoment;
    }

    public void setDatetimeMoment(Date datetimeMoment) {
        this.datetimeMoment = datetimeMoment;
    }

    @Override
    public String toString() {
        OffsetDateTime odt = OffsetDateTime.ofInstant(datetimeMoment.toInstant(), ZoneId.systemDefault());
    
        String s = odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return "ID: " + idAlarm + " datetime: " + s + " interval: " + intervalSeconds;
    }
    public String toText() {
        StringBuilder sb = new StringBuilder();
        OffsetDateTime odt = OffsetDateTime.ofInstant(getDatetimeMoment().toInstant(), ZoneId.systemDefault());
        sb.append("ID alarma: ")
          .append(getIdAlarm())
          .append(", zvoni u: ")
          .append(odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        if (getIntervalSeconds() != null && getIntervalSeconds() > 0) {
            sb.append(", ponavlja se na")
              .append(getIntervalSeconds())
              .append(" sekundi.");
        }
        return sb.toString();
    }
}
