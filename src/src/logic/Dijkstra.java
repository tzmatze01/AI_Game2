package logic;

import model.GameboardGraph;
import model.RasterEdge;
import model.RasterNode;

import java.util.*;

/**
 * Created by fschwank on 11.07.17.
 */
public class Dijkstra {

    private List<RasterNode> nodes;
    private List<RasterEdge> edges;
    private List<RasterNode> settledNodes;
    private List<RasterNode> unSettledNodes;
    private Map<RasterNode, RasterNode> predecessors;
    private Map<RasterNode, Integer> distance;

    private Map<RasterNode, RasterNode> predecessors;

    public Dijkstra(GameboardGraph graph) {
        this.nodes = graph.getRasterNodes();
        this.edges = graph.getRasterEdges();
    }

    public LinkedList<RasterNode> getPathToTarget(RasterNode target) {
        LinkedList<RasterNode> path = new LinkedList<RasterNode>();
        RasterNode step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

    private void findMinimalDistances(RasterNode node) {
        List<RasterNode> adjacentNodes = getNeighbors(node);
        for (RasterNode target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(RasterNode node, RasterNode target) {
        for (RasterEdge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private RasterNode getMinimum(Set<RasterNode> vertexes) {
        RasterNode minimum = null;
        for (RasterNode vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private List<RasterNode> getNeighbors(RasterNode source) {
        List<RasterNode> neighbors = new ArrayList<>();
        for (RasterEdge edge : edges) {
            if (edge.getSource().equals(source)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private int getShortestDistance(RasterNode destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    private boolean isSettled(RasterNode source) {
        return settledNodes.contains(source);
    }
}
