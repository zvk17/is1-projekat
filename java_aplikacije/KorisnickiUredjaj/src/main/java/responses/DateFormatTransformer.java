/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author user2
 */
public class DateFormatTransformer implements Transform<Date> {

    
    @Override
    public Date read(String date) throws Exception {
        OffsetDateTime odt = OffsetDateTime.parse(date);
        
        return Date.from(odt.toInstant());
    }

    @Override
    public String write(Date t) throws Exception {
        OffsetDateTime odt = OffsetDateTime.ofInstant(t.toInstant(), ZoneId.systemDefault());
        return odt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    
}
