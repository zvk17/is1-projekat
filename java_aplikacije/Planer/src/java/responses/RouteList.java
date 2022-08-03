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
public class RouteList {
    List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        return "RouteList{" + "routes=" + routes + '}';
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
    public Summary getFirstSummary() {
        if (routes == null)
            return null;
        if (routes.size() == 0)
            return null;
        if (routes.get(0).sections == null)
            return null;
        if (routes.get(0).sections.size() == 0)
            return null;
        return routes.get(0).sections.get(0).getSummary();
    }
}
