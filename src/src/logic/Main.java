package logic;

import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;
import model.GameboardGraph;

/**
 * Created by matthiasdaiber on 21.06.17.
 */
public class Main {


    public static void main(String args[])
    {
        NetworkClient networkClient = new NetworkClient("localhost", "Yoloswag");
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

        GameboardGraph graph = new GameboardGraph(8);
        graph.calculateGraph(1024, gameBoardPixels);






        int rgb = networkClient.getBoard(0, 0); // 0-1023 ->
        int b = rgb & 255;
        int g = (rgb >> 8) & 255;
        int r = (rgb >> 16) & 255;

        networkClient.getInfluenceRadiusForBot(0); // -> 40

        networkClient.getScore(0); // Punkte von rot

        networkClient.isWalkable(0, 0); // begehbar oder Hinderniss?

        networkClient.setMoveDirection(0, 1, 0); // bot 0 nach rechts
        networkClient.setMoveDirection(1, 0.23f, -0.52f); // bot 1 nach rechts unten

        ColorChange colorChange;
        while ((colorChange = networkClient.pullNextColorChange()) != null) {
            //verarbeiten von colorChange
            //colorChange.player, colorChange.bot, colorChange.x, colorChange.y;
        }
    }
}
