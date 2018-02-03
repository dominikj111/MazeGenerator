package MazeEntities;

public interface NodeCreatedListener {
    void nodeCreated(Node node);
    void setCountOfMazeNodes(int nodeCount);
    void nodesGenerationFinished();
    void setCountOfBridgeConnectors(int bridgesCount);
}
