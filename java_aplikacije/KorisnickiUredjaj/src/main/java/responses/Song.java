/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author user2
 */
@Root(name = "song")
public class Song {
    @Element(name = "idSong")
    private Integer idSong;
    
    @Element(name = "url")
    private String url;
    
    @Element(name = "name")
    private String name;

    public Song() {
    }

    public Integer getIdSong() {
        return idSong;
    }

    public void setIdSong(Integer idSong) {
        this.idSong = idSong;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID: " + idSong + "; NAME: " + name + "; URL: " + url;
    } 
}
