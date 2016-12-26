/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.io.File;
import javax.swing.JFrame;

public class Top {

    //ANT Un-mod
    // You may add other variables and methods
    public static boolean competing = false;
    public static JFrame gui = null;
    public static String[] testCities = {"Grand Junction", "Denver", "Durango", "Craig", "Alamosa", "Boulder",
        "Pueblo", "Naturita","Walsenburg","Delta","Rifle","Walden","Fort Collins","Loveland","Dinosaur"};
    public static Route currBestRoute;
    public static Ant ants;
    public static int[] topBestTour;

    public static void planRoute(String[] cities) {
        Route r = new Route();
        City[] citySet = new City[cities.length];
        for (int i = 0; i < citySet.length; i++) {
            citySet[i] = City.find(cities[i]);
        }
        for (int i = 0; i < citySet.length; i++) {
            r = new Route(r, citySet[i]);
        }
        currBestRoute = r;
        ants = new Ant();
        for (int i = 0; i < Ant.numAnts; i++) {
            Ant.theSwarm[i] = new Ant();
        }
    }
    public static Journey paths[][];

    public static void getShortest() {
        paths = new Journey[City.cities.size()][City.cities.size()];
        for (City c : City.cities) {
            for (Road r : c.roads) {                if (r.start == c) {
                    paths[r.start.n][r.end.n] = new Journey(r, r.start, r.end);
                } else {
                    paths[r.end.n][r.start.n] = new Journey(r, r.end, r.start);
                }
            }
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (City from : City.cities) {
                for (Road r : from.roads) {
                    City via;
                    if (r.start == from) {
                        via = r.end;
                    } else {
                        via = r.start;
                    }
                    for (City to : City.cities) {
                        Journey ft = paths[from.n][to.n];
                        Journey vt = paths[via.n][to.n];
                        if (from != to && via != to && vt != null) {
                            if (ft == null || ft.length > r.length + vt.length) {
                                paths[from.n][to.n] = new Journey(r, from, to, vt);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public static Route populateBest(String[] cities) {
        Route r = new Route();
        City[] citySet = new City[cities.length];
        for (int i = 0; i < citySet.length; i++) {
            citySet[i] = City.find(cities[i]);
        }
        for (int i = 0; i < citySet.length; i++) {
            r = new Route(r, citySet[i]);
        }
        for (int i = 0; i < currBestRoute.c.length; i++) {
            r.c[i] = currBestRoute.c[topBestTour[i]];
        }
        r.length = (int)Ant.bestLength;
        return r;
    }

    public static void main(String[] args) {
        String err = MapReader.readMapFile(new File("coloradomap.csv"));
        if (err != null) {
            System.out.println("Error: " + err);
            System.exit(0);
        }
        getShortest();
        Journey p = paths[City.find("Gunnison").n][City.find("Denver").n];
        planRoute(testCities);//Just a setup, didn't want to re-name plan route
        Route bestRoute = null;
        for (int i = 0; ; i++) {
            topBestTour = ants.solve();
            bestRoute = populateBest(testCities);//Changes the current best route based on best tour.
            //Used because current updating cannot be done in a loop
        }
    }
}
