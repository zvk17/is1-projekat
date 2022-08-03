/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

import com.google.gson.Gson;
import com.google.gson.annotations.*;

/**
 *
 * @author user2
 */
public class Position {
    
    public Double lat;
    public Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
    public String toUrlFormat() {
        return lat + "," + lng;
    }
    
    @Override
    public String toString() {
        return "{" + lat + ", " + lng+ "}";
    }
    
}
