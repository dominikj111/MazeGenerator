package MazeEntities;

import java.util.ArrayList;

/**
 * Created by DEli on 12.5.2016.
 */
public class NodeCreator {

    private Node[][] nodeSpace;
    private ArrayList<Node> nodesList;
    private int defaultNodeColor, width, height;
    private ArrayList<NodeCreatedListener> nodeCreatedListeners;

    public NodeCreator(int width, int height, int defaultNodeColor){

        this.nodeSpace = new Node[height][width];
        this.nodesList = new ArrayList<>();

        this.width = width;
        this.height = height;
        this.defaultNodeColor = defaultNodeColor;

        this.nodeCreatedListeners = new ArrayList<>();
    }

    public Node getNodeAtCellPosition(Cell cell) {


        if(!cellIsInBound(cell)){ return getNonValidNode(); }


        if(!isNodeAt(cell)){

            Node newNode = new Node(cell, getDefaultNodeColor());

            setNodeToPlace(newNode);
            saveNodeToList(newNode);

            fireNodeCreated(newNode);
        }


        return getNodeFrom(cell);
    }

    public Node getNonValidNode(){

        return new Node();
    }

    public boolean isNodeAt(Cell c){

        return isNodeAt(c.getRow(), c.getColumn());
    }

    public void addNodeCreatedListener(NodeCreatedListener listener) {

        this.nodeCreatedListeners.add(listener);
    }

    /* ********* */
    /* ACCESSORS */
    /* ********* */

    private int getDefaultNodeColor() {

        return this.defaultNodeColor;
    }

    public Node[][] getNodeSpace(){

        return this.nodeSpace;
    }

    public ArrayList<Node> getNodesList(){

        return this.nodesList;
    }

    /* **************** */
    /* PRIVATE ROUTINES */
    /* **************** */

    private void saveNodeToList(Node node){

        this.nodesList.add(node);
    }

    private boolean isNodeAt(int row, int column){

        return this.nodeSpace[row][column] != null;
    }

    private void setNodeToPlace(Node n){

        this.nodeSpace[n.getCell().getRow()][n.getCell().getColumn()] = n;
    }

    private Node getNodeFrom(Cell c){

        return getNodeFrom(c.getRow(), c.getColumn());
    }

    private Node getNodeFrom(int row, int column){

        return this.nodeSpace[row][column];
    }

    private boolean cellIsInBound(Cell cell){

        return  cell.getRow() >= 0 &&
                cell.getRow() < this.height &&
                cell.getColumn() >= 0 &&
                cell.getColumn() < this.width;
    }

    private void fireNodeCreated(Node node){
        this.nodeCreatedListeners.stream().forEach((listener)->listener.nodeCreated(node));
    }

    private void fireNodesGenerationFinished(){
        this.nodeCreatedListeners.stream().forEach((listener)->listener.nodesGenerationFinished());
    }
}
