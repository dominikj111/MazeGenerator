package Routines;

import MathUtils.CalcRoutines;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridDirections {

    private final NeighborhoodDirections neighboursType;
    private final int numberOfNeighbourhoodsList;
    private final List<Point> permutationList;
    private List<List<Integer>> permutationIndexList;

    public GridDirections(NeighborhoodDirections neighborhoodType, int numberOfNeighbourhoodsList){

        this.neighboursType = neighborhoodType;
        this.numberOfNeighbourhoodsList = numberOfNeighbourhoodsList;
        this.permutationList = new ArrayList<>();
        this.permutationIndexList = new ArrayList<>();
    }

    //////////////////////////////////////////
    //  API

    public GridDirections initNeighbourhoodsList(){

        setPermutationIndexList(
                generateRandomPermutationIndexListTo(
                    getPermutationIndexList(),
                    getNeighboursType().getCountOfNeighbourhoods(),
                    getNumberOfNeighbourhoodsList()
                )
        );

        return this;
    }

    public List<Point> getNeighboursInOrder(int ... indexes) throws Exception {

        if(indexes.length != getNeighboursType().getCountOfNeighbourhoods()){ throw new Exception("Count of indexes must be same as count of directions goes from enum NeighborhoodDirections."); }

        if(containsDuplicityValue(indexes)){ throw new Exception("Indexes must be unique"); }

        getPermutationList().clear();

        for (int i = 0; i < indexes.length; i++) {
            getPermutationList().add(getNeighboursType().getDirections()[indexes[i]]);
        }

        return getPermutationList();
    }

    public List<Point> getNeighboursInOrderRandom(){

        // init was not run yet, then return {}
        if(getPermutationIndexList().isEmpty()){ return new ArrayList<>(); }

        List<Integer> randomOrder = getPermutationIndexList().get(CalcRoutines.getRandomInt(0, getPermutationIndexList().size()));

        getPermutationList().clear();

        for (int i = 0; i < randomOrder.size(); i++) {
            getPermutationList().add(getNeighboursType().getDirections()[randomOrder.get(i)]);
        }

        return getPermutationList();
    }

    //////////////////////////////////////////
    //  PRIVATE ACCESSOR'S

    private List<List<Integer>> getPermutationIndexList(){

        return this.permutationIndexList;
    }

    private void setPermutationIndexList(List<List<Integer>> permutationIndexList){

        this.permutationIndexList = permutationIndexList;
    }

    private NeighborhoodDirections getNeighboursType(){

        return this.neighboursType;
    }

    private int getNumberOfNeighbourhoodsList(){

        return this.numberOfNeighbourhoodsList;
    }

    private List<Point> getPermutationList(){

        return this.permutationList;
    }

    //////////////////////////////////////////
    //  ROUTINES

    private boolean containsDuplicityValue(int[] indexes) {

        for (int i = 0; i < indexes.length - 1; i++) {
            for (int j = i + 1; j < indexes.length; j++) {

                if(indexes[i] == indexes[j]) { return true; }
            }
        }

        return false;
    }

    private List<List<Integer>> generateRandomPermutationIndexListTo(List<List<Integer>> indexList, int countElementsInPermutation, int countOfResults){

        if(!indexList.isEmpty()){ indexList.clear(); }

        ArrayList<Integer>  pickupList = generateList(countElementsInPermutation),
                            clonedPickupList,
                            tempList;

        while (--countOfResults >= 0){

            clonedPickupList = (ArrayList<Integer>) pickupList.clone();
            tempList = new ArrayList<>();

            while (!clonedPickupList.isEmpty()){
                tempList.add(clonedPickupList.remove(CalcRoutines.getRandomInt(0, clonedPickupList.size())));
            }

            indexList.add(tempList);
        }

        return indexList;
    }

    private ArrayList<Integer> generateList(int size){

        ArrayList<Integer> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) { list.add(i); }

        return list;
    }
}
