/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import java.io.IOException;
import okhttp3.RequestBody;
import responses.Activity;
import responses.ActivityList;
import responses.Alarm;
import responses.Distance;
import responses.SongList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author user2
 */
public class PlanerClient {

    public static interface GetDistanceBeetween {
        @GET("planer/distance_between")
        Call<Distance> getDistanceBetween(@Header("Authorization") String auth, @Query("origin") String origin, @Query("destination") String destination);
    }
    static public Distance getDistanceBetween(String authorizationString, String origin, String destination) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        GetDistanceBeetween service = retrofit.create(GetDistanceBeetween.class); 
        Call<Distance> call = service.getDistanceBetween(authorizationString, origin, destination);
        return call.execute().body();
    }
    public static interface GetDistanceCurrent {
        @GET("planer/distance_current")
        Call<Distance> getDistanceCurrent(@Header("Authorization") String auth, @Query("destination") String destination);
    }
    static public Distance getDistanceCurrent(String authorizationString, String destination) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        GetDistanceCurrent service = retrofit.create(GetDistanceCurrent.class); 
        Call<Distance> call = service.getDistanceCurrent(authorizationString, destination);
        
        return call.execute().body();
    }
    
    
    public static interface GetActivityList {
        @GET("planer")
        Call<ActivityList> getActivityList(@Header("Authorization") String auth);
    }
    static public ActivityList getActivityList(String authorizationString) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        GetActivityList service = retrofit.create(GetActivityList.class); 
        Call<ActivityList> call = service.getActivityList(authorizationString);
        return call.execute().body();
    }
    public static interface PostActivityInterface {
        @POST("planer")          
        Call<Activity> postActivity(
                @Header("Authorization") String auth,
                @Body RequestBody body,
                @Query("addAlarm") Boolean addAlarm
        );
    }
    public static Activity postActivity(@Header("Authorization") String authorizationString, Activity activity, Boolean addAlarm) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        PostActivityInterface service = retrofit.create(PostActivityInterface.class); 
        Call<Activity> call = service.postActivity(authorizationString, Helper.toRequestBody(activity), addAlarm);
        Response<Activity> response = call.execute();
        int code = response.code();
        if (code != 200) {
            throw new ResponseErrorCode(code);
        }
        return response.body();
    }
    
    public static interface DeleteActivityInterface {
        @DELETE("planer/{idPlan}")          
        Call<String> deleteActivity(@Header("Authorization") String auth, @Path("idPlan") Integer idActivity);
    }
    static public String deleteActivity(String authorizationString,Integer idActivity) throws Exception {
        Retrofit retrofit  = Helper.getStringRetrofit();
        
        DeleteActivityInterface service = retrofit.create(DeleteActivityInterface.class); 
        Call<String> call = service.deleteActivity(authorizationString, idActivity);
        Response<String> response = call.execute();
        int code = response.code();
        if (code != 200) {
            throw new ResponseErrorCode(code);
        }
        return response.body();
    }
    public static interface UpdateActivityInterface {
        @POST("planer/{idPlan}")          
        Call<Activity> updateActivity(
                @Header("Authorization") String auth,
                @Body RequestBody body,
                @Query("addAlarm") Boolean addAlarm,
                @Path("idPlan") Integer idActivity
        );
    }
    public static Activity updateActivity(String authorizationString, Activity activity, Boolean addAlarm, Integer idActivity) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        UpdateActivityInterface service = retrofit.create(UpdateActivityInterface.class); 
        Call<Activity> call = service.updateActivity(
                authorizationString,
                Helper.toRequestBody(activity),
                addAlarm,
                idActivity
        );
        return call.execute().body();
    }
    
}
