package MazeDefinitions;

import MazeEntities.Node;

import java.util.ArrayList;

/**
 * Created by DEli on 29.3.2016.
 */
public class MazeData {

    private ArrayList<Node> nodesList;
    private Node[][] environment;

    private int width, height;

    protected MazeData(){}

    public MazeData(ArrayList<Node> nodesList, Node[][] environment, int width, int height){

        this.nodesList = nodesList;
        this.environment = environment;
        this.width = width;
        this.height = height;
    }


    public ArrayList<Node> getNodesList() {

        return nodesList;
    }

    public Node[][] getEnvironment() {

        return environment;
    }


    /* *** */
    /* API */
    /* *** */

    public int getRowsCount(){

        return this.height;
    }

    public int getColumnsCount(){

        return this.width;
    }

    public boolean isNodeAt(int row, int column){

        return this.environment[row][column] != null;
    }

    public void setMazeData(ArrayList<Node> nodesList, Node[][] environment, int width, int height) throws Exception {

        if(this.dataArePopulated()){
            throw new Exception("Maze data are already populated");
        }

        this.nodesList = nodesList;
        this.environment = environment;
        this.width = width;
        this.height = height;
    }

    public boolean dataArePopulated(){
        return this.nodesList != null && this.environment != null && this.width > 0 && this.height > 0;
    }
}
