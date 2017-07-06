import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;
import model.GameboardGraph;
import model.Player;

/**
 * Created by matthiasdaiber on 21.06.17.
 */
public class Main {


    public static void main(String args[])
    {
       String hostName = "localhost";
       //String playerName = "Malermeister";

        /*
        Player player = new Player(hostName, playerName, playerImage);
        Thread playerThread = new Thread(player);
        playerThread.start();
        */

        for (int i = 1; i <= 3; i++) {
            String playerName = "Player " + i;
            Player player = new Player(hostName, playerName);
            Thread playerThread = new Thread(player);
            playerThread.start();
        }

    }
}
