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
@Root(name = "distance")
public class Distance {
    @Element(name = "length")
    Integer length;
    @Element(name = "duration")
    Integer duration;
    @Element(name = "origin")
    String origin;
    @Element(name = "destination")
    String destination;
    
    @Override
    public String toString() {
        return "Distance{" + "length=" + length + ", duration=" + duration + ", origin=" + origin + ", destination=" + destination + '}';
    }
    

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Distance() {
    }
    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append(getOrigin())
          .append(" -> ")
          .append(getDestination())
          .append(": Rastojanje: ")
          .append(getLength())
          .append(" metara, potrebno vreme: ");
        int duration = getDuration();
        if (duration > 3600) {
            sb.append( (int)Math.floor(duration / 3600))
              .append(" sati ");
        }
        if (duration > 60) {
            sb.append( (int)Math.floor( (duration%3600) /60) )
               .append(" minuta ");
        }
        sb.append( duration % 60)
           .append(" sekundi");
        return sb.toString();
    }
    
}
