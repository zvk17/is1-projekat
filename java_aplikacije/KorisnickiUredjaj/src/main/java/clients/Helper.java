/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import responses.DateFormatTransformer;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 *
 * @author user2
 */
public class Helper {
    public static final String BASE_URL = "http://localhost:8080/PametnaKucaServis/api/";
    private static RegistryMatcher rm = new RegistryMatcher();
    static final String XmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
    static {
        rm.bind(Date.class, new DateFormatTransformer());
    }
    public static Serializer serializer = new Persister(rm);
   
    
    public static Retrofit getStringRetrofit() {
        return new Retrofit
                    .Builder()                    
                    .baseUrl(Helper.BASE_URL)                    
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
    }
    public static Retrofit getXmlRetrofit() {
        
        return new Retrofit
                    .Builder()                    
                    .baseUrl(Helper.BASE_URL)                    
                    .addConverterFactory(SimpleXmlConverterFactory.create(Helper.serializer))
                    .build();
        
    }
    public static String toXml(Object object) throws Exception {
        StringWriter writer = new StringWriter();
        RegistryMatcher rm = new  RegistryMatcher();
        rm.bind(Date.class, new DateFormatTransformer());        
        Serializer serializer = new Persister(rm);
        serializer.write(object, writer);
        StringBuilder sb = new StringBuilder();
        sb.append(Helper.XmlHeader).append(writer.getBuffer().toString() );
        return sb.toString();
    }
    public static RequestBody toRequestBody(Object object) throws Exception {
        String xml = toXml(object);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), xml);
        return requestBody;
    }
    public static Date fromLocalDateTime(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    public static Integer toSecondsFromLocalTime(LocalTime lt) {
        int seconds = 0;
        seconds += lt.getHour() * 3600;
        seconds += lt.getMinute() * 60;
        seconds += lt.getSecond();
        return seconds;
    }

}
