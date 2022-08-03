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
public class Section {
    Summary summary;

    public Summary getSummary() {
        return summary;
    }

    @Override
    public String toString() {
        return "Section{" + "summary=" + summary + '}';
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }
    
}
