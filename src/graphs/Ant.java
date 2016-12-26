/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import static graphs.Top.currBestRoute;
import static graphs.Top.paths;
import java.util.Random;

/**
 *
 * @author Miller, Blake
 */
public class Ant {

    public static int currentIndex = 0;
    public int tour[] = new int[Top.currBestRoute.c.length];
    public boolean visited[] = new boolean[tour.length];
    public static int townCount = Top.testCities.length + 1;
    public static int numAnts = (int) (townCount * 0.8);
    public static Ant[] theSwarm = new Ant[numAnts];
    public double trails[][] = new double[townCount][townCount];
    public double probs[] = new double[townCount];
    public int[] bestTour;
    public static double bestLength;
    private Random rand = new Random();
    public double evap = 0.5;

    public void visitTown(int town) {
        tour[currentIndex + 1] = town;
        visited[town] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    //Length of current route
    public double tLength() {
        double length = 0;
        for (int i = 0; i < townCount - 1; i++) {
            length += Top.paths[Top.currBestRoute.c[tour[i]].n][Top.currBestRoute.c[tour[i + 1]].n].length;
        }
        return length;
    }

    public void clear() {
        for (int i = 0; i < townCount; i++) {
            visited[i] = false;
        }
    }
    //Faster pow method found in structure online.
    //25x > greater than Math.pow, but less accurate.
    //Accuracy does not concern us, too many values/rands to affect
    public static double pow(final double a, final double b)
    {
        final int x = (int)(Double.doubleToLongBits(a) >> 32);
        final int y = (int)(b*(x-1072632447)+1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }
    //Create prob array, store probability of moving to selected town
    //Stronger and shorter trails are followed
    public void probTo(Ant ant) {
        int i = ant.tour[currentIndex];
        double denom = 0.0;
        for (int j = 0; j < townCount; j++) {
            if (!ant.visited(j)) {
                denom += pow(trails[i][j], 1) * pow(1.0 / paths[i][j].length, 5);//Greedy coefficent of 5
            }
            for (int x = 0; x < townCount; x++) {
                if (ant.visited(x)) {
                    probs[x] = 0.0;
                } else {
                    double num = pow(trails[i][x], 1.0) * pow(1.0 / paths[i][x].length, 5.0);
                    probs[x] = num / denom;
                }
            }
        }
    }

    //Select the next town based on probs array; Each town has a probability 
    //Choose totally randomly.
    public int selectNext(Ant ant) {
        if (rand.nextDouble() < 0.1 * Top.testCities.length) {
            int randT = rand.nextInt(townCount - currentIndex);
            int temp = -1;
            for (int i = 0; i < townCount; i++) {
                if (!ant.visited(i)) {
                    temp++;
                }
                if (temp == randT) {
                    return i;
                }
            }
        }
        //calculate probs for individual towns
        probTo(ant);
        double r = rand.nextDouble();
        double total = 0;
        for (int i = 0; i < townCount; i++) {
            total += probs[i];
            if (total >= r) {
                return i;
            }
        }
        return -1;
    }

    //Update trails based on ant array routes
    public void updateTrails() {
        //degradation
        for (int i = 0; i < townCount; i++) {
            for (int j = 0; j < townCount; j++) {
                trails[i][j] *= evap;
            }
        }
        //each ant drop for trail
        for (Ant ant : theSwarm) {
            double drop = 500 / ant.tLength();
            for (int i = 0; i < townCount - 1; i++) {
                trails[ant.tour[i]][ant.tour[i + 1]] += drop;
            }
            trails[ant.tour[townCount - 1]][ant.tour[0]] += drop;
        }
    }

    //Next place for ants array
    public void moveAnts() {
        while (currentIndex < townCount - 1) {
            for (Ant ant : theSwarm) {
                ant.visitTown(selectNext(ant));
            }
            currentIndex++;
        }
    }

    //Start in gunnison -visitTown(0)- and intialize using clear
    public void setup() {
        currentIndex = -1;
        for (int i = 0; i < theSwarm.length; i++) {
            theSwarm[i].clear();
            theSwarm[i].visitTown(0);
        }
        currentIndex++;
    }

    //Update the current best route
    public void updateBest() {
        if (bestTour == null) {
            bestTour = theSwarm[0].tour;
            bestLength = theSwarm[0].tLength();
        }
        for (Ant ant : theSwarm) {
            if (ant.tLength() < bestLength) {
                bestLength = ant.tLength();
                bestTour = ant.tour.clone();
            }
        }
    }

    public static String tourToString(int tour[]) {
        String temp = new String();
        for (int i : tour) {
            temp = temp + " " + i;
        }
        return temp;
    }

    public int[] solve() {
        //Make a new, 100% strong trail
        for (int i = 0; i < townCount; i++) {
            for (int j = 0; j < townCount; j++) {
                trails[i][j] = 1.0;
            }
        }
        int repeat = 0;

        while (repeat < 2000) {
            setup();
            moveAnts();
            updateTrails();
            updateBest();
            repeat++;
        }
        System.out.println("Best route length: " + (bestLength));
        System.out.println("Best Route: " + tourToString(bestTour));
       
        return bestTour.clone();
    }

}
