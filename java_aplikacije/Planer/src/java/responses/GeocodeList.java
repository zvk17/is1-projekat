/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package responses;

import java.util.List;

/**
 *
 * @author user2
 */
public class GeocodeList {
    public List<Geocode> items;

    public List<Geocode> getItems() {
        return items;
    }

    public void setItems(List<Geocode> items) {
        this.items = items;
    }
    public Position getFirstPosition() {
        if (getItems() == null) {
            return null;
        }
        if (getItems().size() == 0) {
            return null;
        }
        return getItems().get(0).getPosition();
    }
}
