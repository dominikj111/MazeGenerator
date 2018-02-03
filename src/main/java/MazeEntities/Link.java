package MazeEntities;

import java.awt.*;

/**
 * Created by DEli on 9.7.2016.
 */
public class Link {

    private Node A, B;
    private Point direction;

    public Link(Node A, Node B, Point direction){

        this.A = A;
        this.B = B;
        this.direction = direction;
    }

    public Node getNodeA() {
        return this.A;
    }

    public Node getNodeB() {
        return this.B;
    }

    public Point getDirection(){
        return this.direction;
    }
}
