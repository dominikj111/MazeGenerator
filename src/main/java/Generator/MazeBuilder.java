package Generator;

import MazeDefinitions.MazeData;
import MazeDefinitions.MazeDataWorkThread;
import MazeEntities.Cell;
import MazeEntities.Node;
import MazeEntities.NodeCreatedListener;
import Routines.GridDirections;
import Routines.NeighborhoodDirections;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static MathUtils.CalcRoutines.getRandomIntOdd;

public class MazeBuilder {

    private MazeBuilder(){}

    public static void consistencyTest(MazeData mz) throws Exception {

        ArrayList<Node> nodesList = mz.getNodesList(), neighbourhoodsFromTempNode, neighbourhoodsFromField;
        Node tempNode;
        GridDirections directions = new GridDirections(NeighborhoodDirections.VonNeumann_4, 1).initNeighbourhoodsList();

        for (int row = 0; row < mz.getRowsCount(); row++) {
            for (int column = 0; column < mz.getColumnsCount(); column++) {

                if( mz.getEnvironment()[row][column] == null ){ continue; }

                tempNode = mz.getEnvironment()[row][column];

                /////////////////////////////////////
                // TEST 1 - both links to same object
                if( !nodesList.remove(nodesList.indexOf(tempNode)).isSameObjectAs(tempNode) ){
                    throw new Exception("Non Consistency: node from environment is not in nodes list");
                }

                /////////////////////////////////
                // TEST 2 - neighbourhood testing
                neighbourhoodsFromTempNode  = tempNode.getNeighbourhood();
                neighbourhoodsFromField     = extractNeighbourhoods(
                        mz.getEnvironment(), mz.getColumnsCount(), mz.getRowsCount(),
                        row, column, directions.getNeighboursInOrderRandom());

                if(containsDuplicities(neighbourhoodsFromTempNode) ){
                    throw new Exception("Non Consistency: Duplicities between neighbourhoods of node from list nodes.");
                }

                if(morePointersShowToSameObject(neighbourhoodsFromTempNode)){
                    throw new Exception("Non Consistency: ");
                }

                if(neighbourhoodsFromField.size() != neighbourhoodsFromTempNode.size()){
                    throw new Exception("Non Consistency: different size of neighbourhoods (list <> environment)");
                }

                for (Node n : neighbourhoodsFromTempNode) {
                    if( !neighbourhoodsFromField.get(neighbourhoodsFromField.indexOf(n)).isSameObjectAs(n) ){
                        throw new Exception("Non Consistency: neighbourhoods are different objects");
                    }
                }
            }
        }

        /////////////////////////////////////////////////////////////////////////////
        // TEST 3 - all nodes from list was used (have representation in environment)
        if( !nodesList.isEmpty() ){
            throw new Exception("Non Consistency: not all node are assigned to environment");
        }
    }

    private static boolean morePointersShowToSameObject(ArrayList<Node> neighbourhoods) {

        for (int i = 0; i < neighbourhoods.size() - 1; i++) {
            for (int j = i + 1; j < neighbourhoods.size(); j++) {
                if(neighbourhoods.get(i).isSameObjectAs(neighbourhoods.get(j))){
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean containsDuplicities(ArrayList<Node> neighbourhoods) {

        for (int i = 0; i < neighbourhoods.size() - 1; i++) {
            for (int j = i + 1; j < neighbourhoods.size(); j++) {
                if(neighbourhoods.get(i).equals(neighbourhoods.get(j))){
                    return true;
                }
            }
        }

        return false;
    }

    private static ArrayList<Node> extractNeighbourhoods(Node[][] environment, int width, int height, int row, int column, List<Point> directions){

        ArrayList<Node> returnList = new ArrayList<>();
        int nRow, nCol;

        for (Point dir : directions) {

            nRow = row + dir.y;
            nCol = column + dir.x;

            if(nRow < 0 || nCol < 0 || nRow >= height || nCol >= width){ continue; }

            if(environment[nRow][nCol] != null){ returnList.add(environment[nRow][nCol]); }
        }

        return returnList;
    }

    private static void printAround(Node[][] environment, int width, int height, int row, int column){

        StringBuilder sb = new StringBuilder();
        int nRow, nCol;

        for (int rDir = -1; rDir < 2 ; rDir++) {
            for (int cDir = -1; cDir < 2; cDir++) {

                nRow = row + rDir;
                nCol = column + cDir;

                if (nRow < 0 || nCol < 0 || nRow >= height || nCol >= width) {
                    continue;
                }

                if (environment[nRow][nCol] != null) {
                    sb.append("[ ]");
                } else {
                    sb.append("[#]");
                }

            }
            sb.append("\n");
        }

        System.out.println(sb);
    }

    /* *** */
    /* API */
    /* *** */

    public static MazeData generateMaze(int width, int height, int percentageCycles) {
        return generateMaze(width, height, percentageCycles, null);    }

    public static MazeData generateMaze(int width, int height, int percentageCycles, NodeCreatedListener nodeCreatedListener) {

        TreeGridBuilder GrhBuilder = new TreeGridBuilder(width, height);

        GrhBuilder.addNodeCreatedListener(nodeCreatedListener);

        GrhBuilder.addStartPosition(new Cell(getRandomIntOdd(0, height), getRandomIntOdd(0, width)));

        Node tempNode;

        while((tempNode = GrhBuilder.depthFirstSearch()).isValidNode()) {
            GrhBuilder.goToAndLookAround(tempNode);
        }

        GrhBuilder.createBridgeConnectors(percentageCycles);

        return new MazeData( GrhBuilder.getNodesList(), GrhBuilder.getNodesField(), width, height );
    }

    public static MazeDataWorkThread generateMazeWork(int width, int height, int percentageCycles, NodeCreatedListener nodeCreatedListener) {

        MazeDataWorkThread mazeDataWork = new MazeDataWorkThread();

        Runnable work = new Runnable() {

            private TreeGridBuilder GrhBuilder = null;
            private Integer countOfConnectors = -1;
            private Boolean allowToRun = true;
            private Boolean stopCallBackInitialized = false;

            @Override
            public void run() {

                allowToRun = true;

                if(!stopCallBackInitialized){
                    mazeDataWork.getEventOfferer().addListener(MazeDataWorkThread.events.stopRequest, (data) -> allowToRun = false);
                    stopCallBackInitialized = true;
                }

                if(GrhBuilder == null){
                    GrhBuilder = new TreeGridBuilder(width, height);
                    GrhBuilder.addNodeCreatedListener(nodeCreatedListener);
                    GrhBuilder.addStartPosition(new Cell(getRandomIntOdd(0, height), getRandomIntOdd(0, width)));
                }

                Node tempNode;
                while((tempNode = GrhBuilder.depthFirstSearch()).isValidNode() && allowToRun) {
                    GrhBuilder.goToAndLookAround(tempNode);
                }

                if(countOfConnectors == -1){
                    countOfConnectors = GrhBuilder.getCountOfBridgeConnectors(percentageCycles);
                    nodeCreatedListener.setCountOfBridgeConnectors(countOfConnectors);
                }

                while(--countOfConnectors >= 0 && allowToRun){
                    GrhBuilder.createBridgeConnector();
                }

                try{
                    mazeDataWork.setMazeData(GrhBuilder.getNodesList(), GrhBuilder.getNodesField(), width, height);
                }catch (Exception ex){}
            }
        };

        mazeDataWork.initWork(work);

        return mazeDataWork;
    }

    /* ************************************************* */
    /* MAIN FUNCTION                                     */
    /* ************************************************* */

    public static void main(String[] args) throws Exception {

        MazeData maze = MazeBuilder.generateMaze(31, 13, 0);

        ///////////////////////////
        // maze print to console //
        ///////////////////////////

        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < maze.getRowsCount(); row++) {
            for (int col = 0; col < maze.getColumnsCount(); col++) {

                if(maze.isNodeAt(row, col)){
                    sb.append("   ");
                } else {
                    sb.append("[#]");
                }
            }
            sb.append("\r\n");
        }

        System.out.println(sb.toString());


        /////////////////////////////////////////////
        // test neighbourhood search in node field //
        /////////////////////////////////////////////

        int rowToPrint = 5, colToPrint = 3;
        System.out.println("[" + rowToPrint + "," + colToPrint + "]");
        printAround(maze.getEnvironment(), maze.getColumnsCount(), maze.getRowsCount(), rowToPrint, colToPrint);


        //////////////////////
        // consistency test //
        //////////////////////

        consistencyTest(maze);


        System.out.println("END");
    }
}
