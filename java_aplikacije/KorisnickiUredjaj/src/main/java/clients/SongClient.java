/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import responses.AlarmList;
import responses.Song;
import responses.SongList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *
 * @author user2
 */
public class SongClient {
    public static interface GetSongListInterface {
        @GET("song/list")
        Call<SongList> getSongList(@Header("Authorization") String auth);
    }
    static public SongList getSongList(String authorizationString) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        GetSongListInterface service = retrofit.create(GetSongListInterface.class); 
        Call<SongList> call = service.getSongList(authorizationString);
        return call.execute().body();
    }
    
    
    public static interface GetPlayedSongListInterface {
        @GET("song/played_list")
        Call<SongList> getPlayedSongList(@Header("Authorization") String auth);
    }
    static public SongList getPlayedSongList(String authorizationString) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        GetPlayedSongListInterface service = retrofit.create(GetPlayedSongListInterface.class); 
        Call<SongList> call = service.getPlayedSongList(authorizationString);
        return call.execute().body();
    }
    
    
    public static interface PlaySongInterface {
        @GET("song/play/{idSong}")
        Call<Song> playSong(@Header("Authorization") String auth, @Path("idSong") Integer idSong);
    }
    public static Song playSong(String authorizationString, Integer idSong) throws Exception {
        Retrofit retrofit  = Helper.getXmlRetrofit();
        
        PlaySongInterface service = retrofit.create(PlaySongInterface.class); 
        Call<Song> call = service.playSong(authorizationString, idSong);
        Response<Song> response = call.execute();
        int code = response.code();
        if (code != 200) {
            throw new ResponseErrorCode(code);
        }
        return response.body();
    }
    
}
