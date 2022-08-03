/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

/**
 *
 * @author user2
 */
public class Summary {
    Integer duration;
    Integer length;
    Integer baseDuration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getBaseDuration() {
        return baseDuration;
    }

    public void setBaseDuration(Integer baseDuration) {
        this.baseDuration = baseDuration;
    }

    @Override
    public String toString() {
        return "Summary{" + "duration=" + duration + ", length=" + length + ", baseDuration=" + baseDuration + '}';
    }
    
    
}
