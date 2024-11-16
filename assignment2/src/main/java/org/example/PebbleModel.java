package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The model for the pebble game, managing the grid of pebbles and players.
 */
public class PebbleModel {
    private int DIM;
    private Player p1;
    private Player p2;
    //private int TotalPebbles;
    private String[][] pebbles;

    /**
     * Creates a new game model with a grid of specified dimensions.
     *
     * @param DIM the dimension of the grid
     */
    public PebbleModel(int DIM) {
        this.DIM = DIM;
        p1 = new Player("White", DIM );
        p2 = new Player("Black", DIM );
        //TotalPebbles = 2 * DIM;
        pebbles = new String[DIM][DIM];
        ArrayList<String> pebbleList = new ArrayList<>();
        for (int i = 0; i < DIM; i++) {
            pebbleList.add("White");
            pebbleList.add("Black");
        }
        int freespaces = (DIM*DIM) - pebbleList.size();
        for (int i = 0; i < freespaces; i++) {
            pebbleList.add(" ");
        }
        Collections.shuffle(pebbleList);
        int index = 0;
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                pebbles[row][col] = pebbleList.get(index++);
            }
        }
    }
    public int GetDim(){ return DIM;}
    public String[][] GetPebbles(){ return pebbles;}
    public  Player GetP1(){ return p1;}
    public  Player GetP2(){ return p2;}
}