package model;

import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;

import java.util.Observable;

public class Player extends Observable implements Runnable {


    private boolean end = false;

    private String hostName;
    private String playerName;

    public Player(String hostName, String playerName) {
        this.hostName = hostName;
        this.playerName = playerName;
    }

    public void run() {

        NetworkClient networkClient = new NetworkClient(hostName, playerName);
        networkClient.getMyPlayerNumber(); // 0 = rot, 1 = grün, 2 = blau

        int[] gameBoardPixels = new int[1024*1024];

        for(int i = 0; i < 1024; i++)
        {
            for(int j = 0; j < 1024; j++)
            {
                int pos = (1024 * i) + j;
                gameBoardPixels[pos] = networkClient.getBoard(i, j);
            }
        }

        GameboardGraph graph = new GameboardGraph(1024, 8, networkClient.getMyPlayerNumber());
        graph.calculateGraph(gameBoardPixels);


        //networkClient.getInfluenceRadiusForBot(0); // -> 40

        //networkClient.getScore(0); // Punkte von rot

        ///networkClient.isWalkable(0, 0); // begehbar oder Hinderniss?


        networkClient.setMoveDirection(0, 1, 0); // bot 0 nach rechts
        networkClient.setMoveDirection(1, 0.23f, -0.52f); // bot 1 nach rechts unten
        networkClient.setMoveDirection(2, 0.23f, -0.52f); // bot 1 nach rechts unten



        System.out.println("set move direction!");


        while(networkClient.isAlive()) {

            //System.out.println("is alive!");

            ColorChange colorChange;
            while ((colorChange = networkClient.pullNextColorChange()) != null) {

                graph.processColorChanges(colorChange);

                System.out.println("player: " + networkClient.getMyPlayerNumber() + "colorchange " + colorChange.toString());


                networkClient.setMoveDirection(0, 1, 0); // bot 0 nach rechts
                networkClient.setMoveDirection(1, 0.23f, -0.52f); // bot 1 nach rechts unten
                networkClient.setMoveDirection(2, 0.23f, -0.52f); // bot 1 nach rechts unten


                //verarbeiten von colorChange
                //colorChange.player, colorChange.bot, colorChange.x, colorChange.y;
            }
        }
        setChanged();
        notifyObservers();
    }
}