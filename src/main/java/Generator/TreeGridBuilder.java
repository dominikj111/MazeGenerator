package Generator;

import MazeEntities.*;
import Routines.GridDirections;
import MathUtils.CalcRoutines;
import Routines.NeighborhoodDirections;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static MathUtils.CalcRoutines.isEven;

/**
 * Created by DEli on 16.4.2016.
 */
public class TreeGridBuilder {

    //constants

    private final int   _visitedNodeColor   = 1,
                        _unvisitedNodeColor = 0,
                        _frontier           = 3,
                        _jumpDistance       = 2;


    /* ******* */
    /* MEMBERS */
    /* ******* */

    private GridDirections directions;
    private NodeCreator nodeCreator;

    private ArrayList<Node> frontiers;
    private ArrayList<Link> links;

    private ArrayList<NodeCreatedListener> nodeCreatedListeners;

    /* *********** */
    /* CONSTRUCTOR */
    /* *********** */

    public TreeGridBuilder(int width, int height) {

        if( width < 3 || isEven(width) || height < 3 || isEven(height) ){
            throw new IllegalArgumentException("Input's cannot be less then 3 and should be odd.");
        }

        this.directions = new GridDirections(NeighborhoodDirections.VonNeumann_4, 10).initNeighbourhoodsList();
        this.nodeCreator = new NodeCreator(width, height, _unvisitedNodeColor);

        this.frontiers = new ArrayList<>();
        this.links = new ArrayList<>();

        this.nodeCreatedListeners = new ArrayList<>();
    }

    /* ***************** */
    /* PRIVATE ACCESSORS */
    /* ***************** */

    private GridDirections getDirectionsSolver(){

        return this.directions;
    }

    private NodeCreator getNodeCreator(){

        return this.nodeCreator;
    }

    private ArrayList<Node> getFrontiers() {

        return this.frontiers;
    }

    private ArrayList<Link> getLinks() {

        return this.links;
    }

    /* *** */
    /* API */
    /* *** */

    public void addStartPosition(Cell ... cells){

        Arrays.stream(cells).forEach((cell -> getFrontiers().add(getNodeCreator().getNodeAtCellPosition(cell))));
    }

    public Node breadthFirstSearch(){

        if(getFrontiers().isEmpty()){

            return getNodeCreator().getNonValidNode();
        }

        return getFrontiers().remove(0);
    }

    public Node depthFirstSearch(){

        if(getFrontiers().isEmpty()) {

            return getNodeCreator().getNonValidNode();
        }

        return getFrontiers().remove(getFrontiers().size() - 1);
    }

    public Node randomSearch(){

        if(getFrontiers().isEmpty()) {

            return getNodeCreator().getNonValidNode();
        }

        return getFrontiers().remove(CalcRoutines.getRandomInt(0, getFrontiers().size()));
    }

    public void goToAndLookAround(Node node) {

        node.setColor(_visitedNodeColor);

        Node tempNode;

        for (Point direction : getDirectionsSolver().getNeighboursInOrderRandom()) {

            tempNode = getNodeCreator().getNodeAtCellPosition(node.getCell().createNeighbourhoodCell(direction, _jumpDistance));

            if(!tempNode.isValidNode()){
                continue;
            }

            if(tempNode.isColoredAs(_unvisitedNodeColor)){
                tempNode.setColor(_frontier);
                getFrontiers().add(tempNode);
                createLink(node, tempNode, direction);
                continue;
            }

            if(tempNode.isColoredAs(_visitedNodeColor) && !existConnection(node, direction)){
                getLinks().add(new Link(node, tempNode, direction));
            }
        }
    }

    public void createBridgeConnectors(int percentage){

        int numberOfLinkToConnect = getCountOfBridgeConnectors(percentage);

        final int tempNumberOfLinkToConnect = numberOfLinkToConnect;
        this.nodeCreatedListeners.forEach((listener) -> listener.setCountOfBridgeConnectors(tempNumberOfLinkToConnect));

        while (--numberOfLinkToConnect >= 0){
            createBridgeConnector();
        }
    }

    public void createBridgeConnector(){
        applyLink(getLinks().remove(CalcRoutines.getRandomInt(0, getLinks().size())));
    }

    public int getCountOfBridgeConnectors(int percentage){
        percentage = (percentage > 0) ? (percentage < 100) ? percentage : 100 : 0;
        return (int)Math.round(percentage * getLinks().size() / 100.0);
    }

    public void applyLink(Link link){

        createLink(link.getNodeA(), link.getNodeB(), link.getDirection());
    }

    public ArrayList<Node> getNodesList() {

        return getNodeCreator().getNodesList();
    }

    public Node[][] getNodesField() {

        return  getNodeCreator().getNodeSpace();
    }

    public void addNodeCreatedListener(NodeCreatedListener listener) {

        if(listener == null) { return; }

        this.nodeCreatedListeners.add(listener);
        getNodeCreator().addNodeCreatedListener(listener);
    }

    /* **************** */
    /* PRIVATE ROUTINES */
    /* **************** */

    private void createLink(Node A, Node B, Point direction){

        createChain(A, B, _jumpDistance - 1, direction, 1, getNodeCreator(), _visitedNodeColor);
    }

    private void createChain(
            Node startNode, Node endNode, int innerNodeCount, Point direction, int nodesDistance, NodeCreator nodeCreator, int nodeColor) {

        Node tempNodeA = startNode, tempNodeB;

        while(--innerNodeCount >= 0){

            tempNodeB = nodeCreator.getNodeAtCellPosition(tempNodeA.getCell().createNeighbourhoodCell(direction, nodesDistance));
            tempNodeB.setColor(nodeColor);

            tempNodeA.addToNeighbourhood(tempNodeB);
            tempNodeB.addToNeighbourhood(tempNodeA);

            tempNodeA = tempNodeB;
        }

        tempNodeA.addToNeighbourhood(endNode);
        endNode.addToNeighbourhood(tempNodeA);
    }

    private boolean existConnection(Node nodeA, Point direction){

        //Node nodeB, originally in arguments (included in direction)
        //count direction z nodeA a nodeB
        //Point direction = nodeA.getCell().calcDirection(nodeB.getCell()); //same as direction in arguments

        return getNodeCreator().isNodeAt(nodeA.getCell().createNeighbourhoodCell(direction, 1));
    }
}
