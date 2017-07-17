package model;

import lenz.htw.kipifub.ColorChange;
import lenz.htw.kipifub.net.NetworkClient;

import java.util.*;

/**
 * Created by matthiasdaiber on 21.06.17.
 */
public class GameboardGraph {

    private int gbPixelsPerSide;
    private int pixelBlocksPerSide;
    final int playerNumber;

    private Map<Integer, List<RasterNode>> hotPOIsForBots;

    // maps the x-y-Position to {numberOfBlock, Color}
    private Map<Integer, Integer> colorOfBlockPosition;
    private Map<Integer, Integer> playersBotsPositions;
    private Map<Integer, RasterNode> colorBlocks;

    // Idee: Darstellung des Rasters als Graph: Knoten und Kanten
    private List<RasterNode> nodes;
    private List<RasterEdge> edges;

    public GameboardGraph(int gbPixelsPerSide, int pixelsPerBlock, int playerNumber)
    {
        this.gbPixelsPerSide = gbPixelsPerSide;
        this.pixelBlocksPerSide = gbPixelsPerSide / pixelsPerBlock;
        this.playerNumber = playerNumber;

        //this.pixelBlocks = new ArrayList<>();
        this.hotPOIsForBots = new HashMap<>();
        // creating 'abstraction' where every x-y-Position has its corresponding bin and Color
        this.colorOfBlockPosition = new HashMap<>();
        this.playersBotsPositions = new HashMap<>();
        this.colorBlocks = new HashMap<>();

        // init hotPOIsForBots with a list for each bot
        // init playerBotsPositions with 9 bots for 3 players at position 0
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                this.hotPOIsForBots.put((i*10)+j,new ArrayList<>());
                this.playersBotsPositions.put((i*10)+j, 0);
            }
        }
    }

    public void calculateGraph(int[] gbPixels)
    {
        int pixelsPerBlockSide = gbPixelsPerSide / pixelBlocksPerSide;
        List<List<Integer>> pixelBlocks = new ArrayList<>();

        // fill list with empty blocks
        for(int i = 0; i < (this.pixelBlocksPerSide*this.pixelBlocksPerSide); i++)
        {
            pixelBlocks.add(new ArrayList<>());
        }

        System.out.println("pixelsblocks: "+pixelBlocksPerSide*pixelBlocksPerSide);

        // count how many black and white pixel a bin has
        Map<Integer, int[]> countBW = new HashMap<>();
        for(int i = 0; i < pixelBlocksPerSide*pixelBlocksPerSide; ++i)
            countBW.put(i, new int[]{0,0});

        // to determine the correct pixelBlock in pixelBlocks List
        int pbHeight = 0;
        int pbWidth = 0;

        // to add the right amount of pixels to each block
        int numberOfPixelsHeight = pixelsPerBlockSide;
        int numberOfPixelsWidth = pixelsPerBlockSide;

        // sort pixels to blocks
        for(int y = 0; y < gbPixelsPerSide; y++) {

            if(y == numberOfPixelsHeight) {
                numberOfPixelsHeight += pixelsPerBlockSide;
                ++pbHeight;
            }

            for(int x = 0; x < gbPixelsPerSide; x++) {

                if(x == numberOfPixelsWidth) {
                    numberOfPixelsWidth += pixelsPerBlockSide;
                    ++pbWidth;
                }

                // store current pixel in corresponding ArrayList (which is representing a pixelblock)
                int posPixels = (y * this.pixelBlocksPerSide) + x;
                int posPB = pbHeight * pixelBlocksPerSide + pbWidth;

                // check if pixel is black of white
                int b = gbPixels[posPixels] & 255;
                int g = (gbPixels[posPixels] >> 8) & 255;
                int r = (gbPixels[posPixels] >> 16) & 255;

                // black pixel
                if(r == 0 & g == 0 & b == 0)
                {
                    int[] oldVal = countBW.get(posPB);
                    //System.out.println("black posPB: "+posPB+" oldVal: "+oldVal[0]+" "+oldVal[1]);
                    countBW.put(posPB, new int[]{oldVal[0], ++oldVal[1]});
                }
                // white pixel
                else
                {
                    int[] oldVal = countBW.get(posPB);
                    //System.out.println("white posPB: "+posPB+" oldVal: "+oldVal[0]+" "+oldVal[1]);
                    countBW.put(posPB, new int[]{++oldVal[0], oldVal[1]});
                }

                colorOfBlockPosition.put((x * 10000) + y, posPB);
                pixelBlocks.get(posPB).add(gbPixels[posPixels]);
            }
            pbWidth = 0;
            numberOfPixelsWidth = pixelsPerBlockSide;
        }

        // update blocks with correct color: black or white
        for(int blockHeight = 0; blockHeight < pixelBlocksPerSide; ++blockHeight)
        {

            for(int blockWidth = 0; blockWidth < pixelBlocksPerSide; ++blockWidth)
            {
                int blockPos = (blockHeight * pixelBlocksPerSide) + blockWidth;
                int[] bw = countBW.get(blockPos);

                // calculate the middle of the block
                int middleOfBlock = (gbPixelsPerSide / pixelBlocksPerSide) / 2;
                int middleX = middleOfBlock + (blockWidth * (gbPixelsPerSide / pixelBlocksPerSide));
                int middleY = middleOfBlock + (blockHeight * (gbPixelsPerSide / pixelBlocksPerSide));

                if(bw[0] > bw[1])
                {
                    colorBlocks.put(blockPos, new RasterNode(blockPos, middleX, middleY, Colors.WHITE.getValue()));
                }
                else
                {
                    colorBlocks.put(blockPos, new RasterNode(blockPos, middleX, middleY, Colors.BLACK.getValue()));
                }
            }
        }
    }


    public List<RasterNode> getRasterNodes() {
        return null;
    }

    public List<RasterEdge> getRasterEdges() {
        return null;
    }

    public int[] getHeatMap() {
        return null;
    }

    // TODO parameters are the block numbers or pixelPositions ?
    private List<RasterNode> getBlocksOfRaster(int fromWidth, int toWidth, int fromHeight, int toHeight) {
        List<RasterNode> tmpAllBlocks = new ArrayList<>();

        for(int height = fromHeight; height < toHeight; ++height)
        {
            for(int width = fromWidth; width < toWidth; ++width)
            {
                int pos = height * (toWidth - fromWidth) + width;

                tmpAllBlocks.add(colorBlocks.get(pos));
            }
        }

        return tmpAllBlocks;
    }

    private int getMeanColorOfBlock(List<Integer> block) {

        int accRed = 0;
        int accGreen = 0;
        int accBlue = 0;

        for(int pixel : block)
        {
            accRed = (pixel >> 16) & 255;
            accGreen = (pixel >> 8) & 255;
            accBlue = pixel & 255;
        }

        accRed /= block.size();
        accGreen /= block.size();
        accBlue /= block.size();

        if(accRed > 255)
            accRed = 255;
        if(accRed < 0)
            accRed = 0;
        if(accGreen > 255)
            accGreen = 255;
        if(accGreen < 0)
            accGreen = 0;
        if(accBlue > 255)
            accBlue = 255;
        if(accBlue < 0)
            accBlue = 0;

        return (0xFF << 24) | (accRed << 16) | (accGreen << 8) | accBlue;
    }

    public RasterNode findHotPOI(int bot) {

        // TODO radius of Bot is not used atm

        // clear corresponding list of hotPOIsForBot
        hotPOIsForBots.get(bot).clear();
        List<RasterNode> tmpEnemyBlocks = new ArrayList<>();
        List<RasterNode> tmpWhiteBlocks = new ArrayList<>();
        //List<RasterNode> tmpOwnBlocks = new ArrayList<>();

        int raster = getRasterOfBot(bot);


        for(RasterNode rn : colorBlocks.values())
        {
            // is an enemy color
            if(rn.getMeanColor() != getColorOfPlayer(playerNumber) && rn.getMeanColor() != Colors.WHITE.getValue() && rn.getMeanColor() != Colors.BLACK.getValue())
            {
                tmpEnemyBlocks.add(rn);
                continue;
            }
            if(rn.getMeanColor() == Colors.WHITE.getValue())
            {
                tmpWhiteBlocks.add(rn);
                continue;
            }
        }

        // TODO sort after distance to bot ?
        // TODO sort ascending to 'hotness' of raster

        // add white fields after enemy fields, because they have a lower interest
        tmpEnemyBlocks.addAll(tmpWhiteBlocks);
        hotPOIsForBots.put(bot, tmpEnemyBlocks);

        return this.hotPOIsForBots.get(bot).remove(0);
    }

    public RasterNode findNextHotPOI(int bot) {
        return hotPOIsForBots.get(bot).remove(0);
    }

    public RasterNode findColdPOI(int bot) {
        return null;
    }

    private int getRasterOfBot(int bot) {
        // return (index) raster of given bot
        return  (bot == 0) ? 3 :
                (bot == 1) ? 2 : 1;
    }

    // gets postion as = (x * 10000) + y
    public int getBinAtPostion(int position)
    {
        return colorOfBlockPosition.get(position);
    }

    // liefert bin + botnummer
    public Map<Integer, Integer> getMyBotPositions()
    {
        // TODO only after first colochange ?!

        Map<Integer, Integer> botPositions = new HashMap<>();

        for(int i = 0; i < 3; ++i)
        {
            int playerBot = (this.playerNumber*10)+i;
            botPositions.put(playerBot, playersBotsPositions.get(playerBot));
        }

        return botPositions;
    }

    public void processColorChanges(ColorChange colorChange)
    {
        // update position of players bot
        playersBotsPositions.put((colorChange.player*10)+colorChange.bot, (colorChange.x * 10000) + colorChange.y);

        // update color of corresponding bin
        int colorBlock = colorOfBlockPosition.get((colorChange.x * 10000) + colorChange.y);
        RasterNode oldRN = colorBlocks.get(colorBlock);

        if(oldRN.getMeanColor() != Colors.BLACK.getValue())
            colorBlocks.put(colorBlock, new RasterNode(colorBlock, oldRN.getMiddleX(), oldRN.getMiddleX(), getColorOfPlayer(colorChange.player)));

        System.out.println("update bin: "+colorBlock+" with color: "+getColorOfPlayer(colorChange.player)+" oldColor: "+oldRN.getMeanColor());
    }

    private int getColorOfPlayer(int player)
    {
        // TODO check if this is correct

        switch (player) {
            case 0:
                return Colors.RED.getValue();
            case 1:
                return Colors.GREEN.getValue();
            case 2:
                return Colors.BLUE.getValue();
            default:
                return Colors.NONE.getValue();
        }
    }

}
