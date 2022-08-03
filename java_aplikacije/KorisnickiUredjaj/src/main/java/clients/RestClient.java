/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import clients.AlarmClient;
import clients.PlanerClient;
import clients.SongClient;
import responses.Activity;
import responses.ActivityList;
import responses.Alarm;
import responses.AlarmList;
import responses.Distance;
import responses.Song;
import responses.SongList;
import responses.XmlMomentList;
import retrofit2.http.Header;
import uredjaj.korisnickiuredjaj.AuthConverterHelper;



/**
 *
 * @author user2
 */
public class RestClient {
    
    String username;
    String password;
    String authorization;

    public RestClient(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorization = AuthConverterHelper.getAuthString(username, password);
    }
    //AlarmAPI
    
    public Alarm postAlarm(Alarm alarm) throws Exception {
        
        return AlarmClient.postAlarm(authorization, alarm);
    }
    public AlarmList getAlarmList() throws Exception {
        return AlarmClient.getAlarmList(authorization);
    }
    public String putAlarmSong(Integer idSong) throws Exception {
        return AlarmClient.putAlarmSong(authorization, idSong);
    }
    public XmlMomentList getMomentList() throws Exception {
        return AlarmClient.getMomentList(authorization);
    }
    public Alarm postMomentAlarm(Integer idMoment) throws Exception {
        return AlarmClient.postMomentAlarm(authorization, idMoment);
    }
    
    
    //SongReproductionAPI
    public SongList getSongList() throws Exception {
        return SongClient.getSongList(authorization);
    }
    public SongList getPlayedSongList() throws Exception {
        return SongClient.getPlayedSongList(authorization);        
    }
    public Song playSong(Integer idSong) throws Exception {
        return SongClient.playSong(authorization, idSong);
    }
    
    
    
    
    
    //ActivityAPI
    public Distance getDistanceBetween(String origin, String destination) throws Exception {
        return PlanerClient.getDistanceBetween(authorization, origin, destination);
    }
    public Distance getDistanceCurrent(String destination) throws Exception {
        return PlanerClient.getDistanceCurrent(authorization, destination);
    }
    public ActivityList getActivityList() throws Exception {
        return PlanerClient.getActivityList(authorization);
    }
    public String deleteActivity(Integer idActivity) throws Exception {
        return PlanerClient.deleteActivity(authorization, idActivity);
    }
    public Activity postActivity(Activity activity, Boolean addAlarm) throws Exception {
        return PlanerClient.postActivity(authorization, activity, addAlarm);
    }
    public Activity updateActivity(Activity activity, Boolean addAlarm, Integer idActivity) throws Exception {
        return PlanerClient.updateActivity(authorization, activity, addAlarm, idActivity);
    }
    
    
}
