package MazeEntities;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

/**
 * Created by DEli on 9.2.2016.
 */
public class Node {

    private int color;
    private SimpleIntegerProperty colorProperty;
    private Cell cell;
    private ArrayList<Node> neighbourhood;
    private boolean validNode, objectMarker_pointerTest;

    public Node(){

        this.validNode = false;
    }

    public Node(Cell cell, int color) {
        this.cell = cell;
        this.neighbourhood = new ArrayList<>();
        this.color = color;
        this.colorProperty = new SimpleIntegerProperty(color);
        this.validNode = true;
    }


    public boolean isValidNode(){

        return validNode;
    }

    public Cell getCell(){

        return this.cell;
    }

    public int getColor() {

        return this.colorProperty.get();
    }

    public void setColor(int color){
        this.colorProperty.set(color);
    }

    public SimpleIntegerProperty colorProperty(){

        return this.colorProperty;
    }

    public boolean isColoredAs(int color){

        return color == getColor();
    }

    public void addToNeighbourhood(Node B) {

        this.neighbourhood.add(B);
    }

    public ArrayList<Node> getNeighbourhood(){

        return this.neighbourhood;
    }



    // ******************************************************************
    // OVERRIDE'S
    // ******************************************************************

    @Override
    public boolean equals(Object obj) {

        Node remote = (Node)obj;

        return  this.getCell().equals(remote.getCell()) &&
                this.isColoredAs(remote.getColor());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("[node: color=").append(getColor()).append(" cell=").append(getCell().toString()).append("]");

        return sb.toString();
    }

    // ******************************************************************
    // ROUTINES
    // ******************************************************************

    private void setMarkerTo(boolean value){

        this.objectMarker_pointerTest = value;
    }

    private boolean isMarkerSet(){

        return this.objectMarker_pointerTest;
    }

    public boolean isSameObjectAs(Object obj){

        Node remote = ((Node)obj);

        this.setMarkerTo(true);
        remote.setMarkerTo(false);

        return this.isMarkerSet() == remote.isMarkerSet();
    }
}

