/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.util.ArrayList;

/**
 *
 * @author Miller
 */
public class Route implements IRoute {

    public City[] c;
    int length;

    public Route() {
        length = 0;
        c = new City[1];
        c[0] = City.find("Gunnison");
    }

    public Route(Route r, City current) {
        c = new City[r.c.length + 1];
        for (int i = 0; i < r.c.length; i++) {
            c[i] = r.c[i];
        }
        c[c.length - 1] = current;
        length = r.length + Top.paths[c[c.length-2].n][current.n].length;
     }

    public City[] getCities() {
        return c;
    }

    public int getLength() {
        return this.length;
    }
}
