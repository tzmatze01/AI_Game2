package model;

import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;
import logic.AStarAlgorithm;
import logic.Pathfinder;
import strategy.AggressiveStrategy;
import strategy.Strategy;

import java.util.List;
import java.util.Observable;

public class Player extends Observable implements Runnable {

    private boolean end = false;

    private String hostName;
    private String playerName;

    GameboardGraph graph;

    public Player(String hostName, String playerName) {
        this.hostName = hostName;
        this.playerName = playerName;
    }

    public void run() {

        NetworkClient networkClient = new NetworkClient(hostName, playerName);
        int myPlayerNumber = networkClient.getMyPlayerNumber(); // 0 = rot, 1 = grün, 2 = blau

        int[] gameBoardPixels = new int[1024*1024];

        for(int i = 0; i < 1024; i++)
        {
            for(int j = 0; j < 1024; j++)
            {
                int pos = (1024 * i) + j;
                gameBoardPixels[pos] = networkClient.getBoard(i, j);
            }
        }

        graph = new GameboardGraph(1024, 8, networkClient.getMyPlayerNumber());
        graph.calculateGraph(gameBoardPixels);

        System.out.println("set move direction!");

        while(networkClient.isAlive()) {

            //System.out.println("is alive!");

            Strategy gameStrategy = new AggressiveStrategy();

            ColorChange colorChange;
            while ((colorChange = networkClient.pullNextColorChange()) != null) {

                graph.processColorChanges(colorChange);

                if (colorChange.player == myPlayerNumber) {
                    for (int bot = 0; bot <= 2; bot++) {
                        Pathfinder pathAlgorithm = new AStarAlgorithm(gameStrategy);

                        RasterNode botPosition = graph.getCurrentRasterNodeOfBot(bot);
                        RasterNode nextTarget = checkOutNextTarget(bot);

                        List<RasterNode> path = pathAlgorithm.findPath(botPosition, nextTarget);
                    }
                }

            }
        }
        setChanged();
        notifyObservers();
    }

    private RasterNode checkOutNextTarget(int bot) {
        RasterNode nextTarget = null;
        RasterNode nextHotPOI = graph.findHotPOI(bot);

        return nextTarget;
    }

    private void moveBot(int bot, List<RasterNode> path) {
        
    }
}