package MazeDefinitions;

import EventLibrary.BasicEventOfferer;
import EventLibrary.EventBundle;
import EventLibrary.EventData;
import EventLibrary.EventTrigger;
import MazeEntities.Node;
import EventLibrary.EventOfferer;

import java.util.ArrayList;

public class MazeDataWorkThread extends MazeData {

    public enum events { stopRequest, resumeRequest, startRequest, workFinished }

    private Runnable mazeWork;
    private EventOfferer eventOfferer;
    private EventTrigger eventTrigger;

    private boolean started;

    public MazeDataWorkThread(){

        this.mazeWork = null;
        this.started = false;

        EventBundle bundle = BasicEventOfferer.createBundle();
        this.eventOfferer = bundle.offerer;
        this.eventTrigger = bundle.trigger;
    }

    public void initWork(Runnable mazeGenerating){

        this.mazeWork = mazeGenerating;
    }

    public void doMazeGenerating() throws Exception {

        if(this.mazeWork == null){ throw new Exception("Maze work is not initialized."); }

        if(!this.started){
            this.started = true;
            this.eventTrigger.fireEvent(events.startRequest, new EventData(this.mazeWork, this));
        }

        Thread work = new Thread(this.mazeWork);
        work.setDaemon(true);
        work.start();
    }

    public void resumeMazeGenerating() throws Exception {

        this.eventTrigger.fireEvent(events.resumeRequest, new EventData(this.mazeWork, this));
        doMazeGenerating();
    }

    public void stopMazeGenerating(){

        this.eventTrigger.fireEvent(events.stopRequest, new EventData(this.mazeWork, this));
    }

    public EventOfferer getEventOfferer(){
        return this.eventOfferer;
    }

    @Override
    public void setMazeData(ArrayList<Node> nodesList, Node[][] environment, int width, int height) throws Exception {
        super.setMazeData(nodesList, environment, width, height);

        this.eventTrigger.fireEvent(events.workFinished, new EventData(this.mazeWork, this));
    }
}
