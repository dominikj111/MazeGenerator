package Routines;

import java.awt.*;

/**
 * Created by DEli on 8.7.2016.
 */
public enum NeighborhoodDirections {

    VonNeumann_4
            (
                new Point[] {
                    new Point( 0, -1), new Point(-1, 0), new Point(+1, 0), new Point( 0, +1)
                }
            ),

    Moore_8
            (
                new Point[] {
                        new Point(-1, -1), new Point( 0, -1), new Point(+1, -1), new Point(-1,  0), new Point(+1,  0), new Point(-1, +1), new Point( 0, +1), new Point(+1, +1)
                }
            );

    private Point[] directions;

    NeighborhoodDirections(Point[] directions){

        this.directions = directions;
    }

    public int getCountOfNeighbourhoods(){

        return this.directions.length;
    }

    public Point[] getDirections(){

        return this.directions;
    }
}
