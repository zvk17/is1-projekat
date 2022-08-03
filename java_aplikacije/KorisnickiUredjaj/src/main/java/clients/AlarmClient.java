/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;


import java.io.StringWriter;
import java.util.Date;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import responses.XmlMomentList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import uredjaj.korisnickiuredjaj.AuthConverterHelper;

import responses.Alarm;
import responses.AlarmList;
import responses.DateFormatTransformer;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
/**
 *
 * @author user2
 */
public class AlarmClient {

    
    public static interface GetMomentListInterface {
        @GET("alarm/moment_list")
        Call<XmlMomentList> getMomentList(@Header("Authorization") String auth);
    }
    
    static public XmlMomentList getMomentList(String authorizationString) throws Exception {
        Retrofit retrofit = Helper.getXmlRetrofit();
        
        GetMomentListInterface service = retrofit.create(GetMomentListInterface.class); 
        Call<XmlMomentList> call = service.getMomentList(authorizationString);
        Response<XmlMomentList> response = call.execute();

        if (response.code() != 200) {
            throw new Exception();
        }
        return response.body();
    }
    
    
    public static interface PostMomentList {
        @POST("alarm/{idMoment}")
        Call<Alarm> createAlarm(@Header("Authorization") String auth, @Path("idMoment") Integer idMoment);
    }
    static public Alarm postMomentAlarm(String authorizationString, Integer idMoment) throws Exception {
        
       Retrofit retrofit = Helper.getXmlRetrofit();
                    
        
       PostMomentList service = retrofit.create(PostMomentList.class); 
       Call<Alarm> call = service.createAlarm(authorizationString, idMoment);
       return call.execute().body();
    }
    
    
    public static interface GetAlarmListInterface {
        @GET("alarm/list")
        Call<AlarmList> getAlarmtList(@Header("Authorization") String auth);
    }
    static public AlarmList getAlarmList(String authorizationString) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        GetAlarmListInterface service = retrofit.create(GetAlarmListInterface.class); 
        Call<AlarmList> call = service.getAlarmtList(authorizationString);
        return call.execute().body();
    }
    
    
    
    public static interface PutSongInterface  {
        @FormUrlEncoded
        @PUT("alarm/song/")        
        Call<String> putAlarmSong(@Header("Authorization") String auth, @Field("idSong") Integer idMoment);
    }
    static public String putAlarmSong( String authorizationStrign, Integer idSong) throws Exception {
        Retrofit retrofit  = new Retrofit.Builder()                    
                    .baseUrl(Helper.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        
        PutSongInterface service = retrofit.create(PutSongInterface.class); 
        Call<String> call = service.putAlarmSong(authorizationStrign, idSong);
        Response<String> response = call.execute();
        int code = response.code();
        if (code != 200) {
            throw new ResponseErrorCode(code);
        }
        return response.body();
    }
    
    public static interface PostAlarmInterface {
        @POST("alarm")          
        Call<Alarm> postAlarm(@Header("Authorization") String auth, @Body RequestBody body);
    }
    public static Alarm postAlarm(String authorizationString, Alarm alarm) throws Exception {
        Retrofit retrofit = Helper.getXmlRetrofit();
        PostAlarmInterface service = retrofit.create(PostAlarmInterface.class);
      
        Call<Alarm> call = service.postAlarm(authorizationString, Helper.toRequestBody(alarm));
        Response<Alarm> res = call.execute();
        int code = res.code();
        if (code != 200) {
            throw new ResponseErrorCode(code);            
        }
        return res.body();
    }

}
