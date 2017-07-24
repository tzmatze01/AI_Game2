package logic;

import model.RasterEdge;
import model.RasterNode;
import strategy.Strategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by FSCHWANK on 22.07.2017.
 */
public class AStarAlgorithm implements Pathfinder {

    private List<RasterNode> openList;
    private List<RasterNode> closedList;

    public static Strategy gameStrategy;

    public AStarAlgorithm(Strategy strategy) {
        gameStrategy = strategy;
    }

    public List<RasterNode> findPath(RasterNode sourceNode, RasterNode targetNode) {
        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        boolean calculatedPath = false;

        openList.add(sourceNode);

        while(!calculatedPath) {
            RasterNode currentNode = getMinimumFNodeFromOpenList();
            closedList.add(currentNode);
            openList.remove(currentNode);

            if (currentNode.equals(targetNode)) {
                return calculatePath(sourceNode, currentNode);
            }

            List<RasterNode> neighborNodes = getNeighborNodesOfNode(currentNode);
            for (RasterNode neighbor : neighborNodes) {
                if (!openList.contains(neighbor)) {
                    neighbor.setPreviousNode(currentNode);

                    int calculatedCost = gameStrategy.calculateCostOfField(targetNode);
                    neighbor.sethCosts(targetNode, calculatedCost);
                    neighbor.setgCosts(targetNode);
                    openList.add(neighbor);
                }
                else {
                    if (neighbor.getgCosts() > neighbor.calculateGCosts(currentNode)) {
                        neighbor.setPreviousNode(currentNode);
                        neighbor.setgCosts(currentNode);
                    }
                }
            }
            if (openList.isEmpty()) { // no path exists
                return new LinkedList<RasterNode>(); // return empty list
            }
        }

        return null;
    }

    private RasterNode getMinimumFNodeFromOpenList() {
        RasterNode minimum = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getfCosts() < minimum.getfCosts()) {
                minimum = openList.get(i);
            }
        }
        return minimum;
    }

    private List<RasterNode> calculatePath(RasterNode sourceNode, RasterNode targetNode) {
        LinkedList<RasterNode> path = new LinkedList<>();

        RasterNode currentNode = targetNode;
        boolean done = false;
        while (!done) {

            path.addFirst(currentNode);
            currentNode = currentNode.getPreviousNode();

            if (currentNode.equals(sourceNode)) {
                done = true;
            }
        }
        return path;
    }

    private List<RasterNode> getNeighborNodesOfNode(RasterNode node) {
        List<RasterNode> neighbors = new ArrayList<>();
        List<RasterEdge> edgesToNeighbors = node.getWayPoints();

        for (RasterEdge edge : edgesToNeighbors) {
            neighbors.add(edge.getDestination());
        }

        return neighbors;
    }
}
