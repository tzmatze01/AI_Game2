package model;

import lenz.htw.kipifub.ColorChange;


import java.awt.image.Raster;
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
    private Map<Integer, Integer> xyOfBlocks;
    private Map<Integer, Integer> playersBotsPositions;
    private Map<Integer, RasterNode> colorBlocks;

    public GameboardGraph(int gbPixelsPerSide, int pixelsPerBlock, int playerNumber)
    {
        this.gbPixelsPerSide = gbPixelsPerSide;
        this.pixelBlocksPerSide = gbPixelsPerSide / pixelsPerBlock;
        this.playerNumber = playerNumber;

        //this.pixelBlocks = new ArrayList<>();
        this.hotPOIsForBots = new HashMap<>();
        // creating 'abstraction' where every x-y-Position has its corresponding bin and Color
        this.xyOfBlocks = new HashMap<>();
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

        System.out.println("pixelsblocksperside * pixelsblocksperside: "+pixelBlocksPerSide*pixelBlocksPerSide);

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

                xyOfBlocks.put((x * 10000) + y, posPB);
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
        List<RasterNode> nodes =  new ArrayList<>();
        nodes.addAll(colorBlocks.values());
        return nodes;
    }

    // parameters are the bin numbers
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

    private List<RasterNode> getNeighborBlocks(int bin)
    {
        int[] positions = new int[]{bin+1, bin-1, bin+pixelBlocksPerSide, bin-pixelBlocksPerSide};
        List<RasterNode> neighborBins = new ArrayList<>();

        for(int position : positions)
        {
            if(isValidNeighborBin(bin, position))
            {
                neighborBins.add(colorBlocks.get(position));
            }
        }

        return neighborBins;
    }

    private boolean isValidNeighborBin(int sourceBin, int destinationBin)
    {
        if(destinationBin < 0)
            return false;
        if(destinationBin >= (pixelBlocksPerSide*pixelBlocksPerSide))
            return false;

        // source bin is on the left side -> no destinationbin to the left side
        if(sourceBin % pixelBlocksPerSide == 0 && destinationBin == (sourceBin-1))
            return false;
        // source bin is on the right side -> no destinationbin to the left side
        if((sourceBin+1) % pixelBlocksPerSide == 0 && (destinationBin-1) == sourceBin)
            return false;

        // check if bin is a obstacle
        if(colorBlocks.get(destinationBin).getMeanColor() == Colors.BLACK.getValue())
            return false;

        return true;
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

    public List<RasterNode> findHotPOIs(int bot) {

        int radius = getRasterOfBot(bot);

        // clear corresponding list of hotPOIsForBot
        hotPOIsForBots.get(bot).clear();
        List<RasterNode> tmpEnemyBlocks = new ArrayList<>();
        List<RasterNode> tmpWhiteBlocks = new ArrayList<>();

        // iterate over gameboard
        // startposition changes according to raster of Bot
        for(int height = radius; height < pixelBlocksPerSide; height++) {

            for(int width = radius; width < pixelBlocksPerSide; width++) {

                int binPosition = height * pixelBlocksPerSide + width;

                List<RasterNode> nodesInRaster = getBlocksOfRaster(width-radius, width, height-(pixelBlocksPerSide*radius), height);
                int color = getMeanColorOfRaster(nodesInRaster);

                // is an enemy color
                if(color != getColorOfPlayer(playerNumber) && color != Colors.WHITE.getValue() && color != Colors.BLACK.getValue())
                {
                    tmpEnemyBlocks.add(extractInfoFromRaster(nodesInRaster, color, radius));
                    continue;
                }
                if(color == Colors.WHITE.getValue())
                {
                    tmpWhiteBlocks.add(extractInfoFromRaster(nodesInRaster, color, radius));
                    continue;
                }
            }
        }

        // sorts after bots in raster
        Collections.sort(tmpEnemyBlocks);

        // add white fields after enemy fields, because they have a lower interest
        tmpEnemyBlocks.addAll(tmpWhiteBlocks);
        hotPOIsForBots.put(bot, tmpEnemyBlocks);

        return this.hotPOIsForBots.get(bot);
    }

    private RasterNode extractInfoFromRaster(List<RasterNode> nodes, int color, int botRadius)
    {
        // find center of all nodes and extract corresponding bin
        RasterNode firstNode = nodes.get(0);

                            // pixels per block
        int rasterSideLength = (gbPixelsPerSide / pixelBlocksPerSide) * botRadius;

        int middleX = (firstNode.getMiddleX() + rasterSideLength) / 2;
        int middleY = (firstNode.getMiddleY() + rasterSideLength) / 2;

        int bin = xyOfBlocks.get((middleX * 10000) + middleY);

        // get bots of raster and save
        // TODO save only the bots in the bin, not the raster
        int numberOfBots = 0;
        for(RasterNode node : nodes)
        {
            // TODO what if there are multiple bots in one bin -> pretty unlikely?
            if(playersBotsPositions.containsValue(node.getId()))
                numberOfBots++;
        }

        RasterNode node = colorBlocks.get(bin);
        node.setNumberOfBotsInRaster(numberOfBots);
        node.setMeanColor(color);

        return node;
    }

    private int getMeanColorOfRaster(List<RasterNode> bins)
    {
        // has length of all colors
        int[] colorOccurences = new int[]{0, 0, 0, 0, 0};

        for(RasterNode bin : bins)
        {
            ++colorOccurences[bin.getId()];
        }

        int max = 0;
        int color = 0;

        for(int index = 0; index < colorOccurences.length; ++index)
        {
            if(colorOccurences[index] > max)
            {
                max = colorOccurences[index];
                color = index;
            }
        }

        return color;
    }
    public RasterNode findHotPOI(int bot) {
        return hotPOIsForBots.get(bot).remove(0);
    }

    public RasterNode findColdPOI(int bot) {
        return hotPOIsForBots.get(bot).remove(hotPOIsForBots.size()-1);
    }

    public RasterNode getCurrentRasterNodeOfBot(int bot) {
        int rasterID = getRasterOfBot(bot);
        RasterNode botNode = colorBlocks.get(rasterID);

        return botNode;
    }

    private int getRasterOfBot(int bot) {

        // TODO change numbers due testing
        // return (index) raster of given bot
        return  (bot == 0) ? 3 :
                (bot == 1) ? 2 : 1;
    }

    // gets postion as = (x * 10000) + y
    public int getBinAtPostion(int position)
    {
        return xyOfBlocks.get(position);
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
        int colorBlock = xyOfBlocks.get((colorChange.x * 10000) + colorChange.y);

        // update position of players bot
        playersBotsPositions.put((colorChange.player*10)+colorChange.bot, colorBlock);

        // update color of corresponding bin
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

    // umliegende felder von bot -> hot poi, selbe cold poi als raster

}
