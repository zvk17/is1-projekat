/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import responses.GeocodeList;
import responses.Position;
import responses.RouteList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * @author user2
 */
public class MapsClient {
    private static final String API_KEY = "-CqK0MqKJfyWGdjhpLNXpYsVRwe7j5R_o6EfijNGP1M";
    private static final String GEOCODE_URL = "https://geocode.search.hereapi.com/";
    private static final String ROUTE_URL = "https://router.hereapi.com/";
    public static interface GeocodeInterface {
        @GET("v1/geocode")
        Call<GeocodeList> getGeocodeList(@Query("q") String query, @Query("apiKey") String apiKey);
    }
    public static GeocodeList geocodeLocation(String query) throws Exception {
        System.out.println("clients.MapsClient.geocodeLocation()");
        Retrofit retrofit = new Retrofit
                    .Builder()                    
                    .baseUrl(GEOCODE_URL)                    
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
       
        GeocodeInterface service = retrofit.create(GeocodeInterface.class); 
        Call<GeocodeList> call = service.getGeocodeList(query, API_KEY);
        
       
        Response<GeocodeList> e = call.execute();
        
        if (e.code() != 200) {
            throw new Exception();
        }
        return e.body();
    }
    public static interface RouteInterface {
        @GET("v8/routes")
        Call<RouteList> getList(
                @Query("transportMode") String transportMode,
                @Query("apiKey") String apiKey,
                @Query("origin") String origin,
                @Query("destination") String destination,
                @Query("return") String returnFormat
        );
    }
    public static RouteList getRouteList(Position origin, Position destination) throws Exception {
        System.out.println("clients.MapsClient.getRouteList():position");
        Retrofit retrofit = new Retrofit
                    .Builder()                    
                    .baseUrl(ROUTE_URL)                    
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
       
        RouteInterface service = retrofit.create(RouteInterface.class); 
        Call<RouteList> call = service.getList(
                "car",
                API_KEY,
                origin.toUrlFormat(),
                destination.toUrlFormat(),
                "summary"
        );
        
        Response<RouteList> e = call.execute();
        if (e.code() != 200) {
            throw new Exception();
        }
        RouteList retVal = e.body();
        System.out.println("Summary: " + retVal.getFirstSummary());
        return retVal;
    }
    public static RouteList getRouteList(String origin, String destination) throws Exception {
        System.out.println("clients.MapsClient.getRouteList(" + origin + "," + destination + "):string");
        GeocodeList originGeocode = MapsClient.geocodeLocation(origin);
        GeocodeList destinationGeocode = MapsClient.geocodeLocation(destination);
        
        RouteList retVal = MapsClient.getRouteList(originGeocode.getFirstPosition(), destinationGeocode.getFirstPosition());
        System.out.println("Summary: " + retVal.getFirstSummary());
        return retVal;
    }
}
