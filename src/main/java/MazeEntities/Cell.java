package MazeEntities;

import MathUtils.CalcRoutines;
import java.awt.*;

/**
 * Created by DEli on 29.3.2016.
 */
public class Cell {

    private int row, column;



    public Cell(int row, int column){

        this.row    = row;
        this.column = column;
    }



    public int getRow() {

        return  this.row;
    }

    public int getColumn() {

        return this.column;
    }

    public Cell createNeighbourhoodCell(Point direction, int jumpDistance){

        return new Cell(getRow() + direction.y * jumpDistance, getColumn() + direction.x * jumpDistance);
    }

    public Point calcDirection(Cell nCell){

        return new Point(CalcRoutines.signum(nCell.getColumn() - this.getColumn()), CalcRoutines.signum(nCell.getRow() - this.getRow()));
    }

    @Override
    public boolean equals(Object obj) {
        Cell remoteCell = (Cell)obj;
        return getRow() == remoteCell.getRow() && getColumn() == remoteCell.getColumn();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("[").append(getRow()).append(",").append(getColumn()).append("]");

        return sb.toString();
    }
}
