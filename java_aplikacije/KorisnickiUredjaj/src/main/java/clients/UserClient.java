/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import java.io.IOException;
import responses.AlarmList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 *
 * @author user2
 */
public class UserClient {
    public static interface RegisterInterface {
        @FormUrlEncoded
        @POST("user/register")                
        Call<String> register(@Field("username") String username, @Field("password") String password, @Field("homeLocation")String homeLocation);
    }
    static public String register(String username, String password, String homeLocation) throws Exception {
        Retrofit retrofit  = Helper.getStringRetrofit();
        
        RegisterInterface service = retrofit.create(RegisterInterface.class); 
        Call<String> call = service.register(username, password, homeLocation);
        
        Response<String> response = call.execute();
        if (response.code() != 200) {
            throw new ResponseErrorCode(response.code());
        }
        return response.body();
    }
    public static interface LoginInterface {
        @FormUrlEncoded
        @POST("user/login")                
        Call<String> login(@Field("username") String username, @Field("password") String password);
    }
    static public String login(String username, String password) throws Exception {
        Retrofit retrofit  = Helper.getStringRetrofit();
        
        LoginInterface service = retrofit.create(LoginInterface.class); 
        Call<String> call = service.login(username, password);
        Response<String> response = call.execute();
        if (response.code() != 200) {
            throw new ResponseErrorCode(response.code());
        }
        return response.body();
    }
}
