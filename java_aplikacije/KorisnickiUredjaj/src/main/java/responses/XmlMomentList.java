/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author user2
 */
@Root(name = "xmlMoments")
public class XmlMomentList {
    @ElementList(name="moment", inline=true)
    private List<Moment> momentList;

    public List<Moment> getMomentList() {
        return momentList;
    }

    public void setMomentList(List<Moment> momentList) {
        this.momentList = momentList;
    }
    public XmlMomentList() {}

    @Override
    public String toString() {
        return this.momentList.size() + "";
    }
    
    
}
