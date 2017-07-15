package model;

import lenz.htw.kipifub.ColorChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matthiasdaiber on 21.06.17.
 */
public class GameboardGraph {

    private int pixelBlocksPerSide;
    final int playerNumber;

    private List<List<Integer>> pixelBlocks;
    private List<List<RasterNode>> hotPOIsForBots;

    private Map<Integer, Integer> matchingPixelToBlocks;
    private Map<Integer, Integer> playersBotsPositions;

    // Idee: Darstellung des Rasters als Graph: Knoten und Kanten
    private List<RasterNode> nodes;
    private List<RasterEdge> edges;

    int gameBoardWidth = 0;
    int gameBoardHeight = 0;


    public GameboardGraph(int pixelBlocksPerSide, int playerNumber)
    {
        this.pixelBlocksPerSide = pixelBlocksPerSide;
        this.playerNumber = playerNumber;

        this.pixelBlocks = new ArrayList<>();
        this.hotPOIsForBots = new ArrayList<>();

        // init hotPOIsForBots with a list for each bot
        for(int i = 0; i < 3; ++i)
        {
            this.hotPOIsForBots.add(new ArrayList<>());
        }

        this.playersBotsPositions = new HashMap<>();

        // init playerBotsPositions with 9 bots for 3 players at position 0
        for(int i = 0; i < 3; ++i)
        {
            this.playersBotsPositions.put((i*10), 0);
            this.playersBotsPositions.put((i*10)+1, 0);
            this.playersBotsPositions.put((i*10)+2, 0);
        }

        this.matchingPixelToBlocks = new HashMap<>();
    }

    public void calculateGraph(int gbPixelsPerSide, int[] gbPixels)
    {
        int pixelsPerBlockSide = gbPixelsPerSide / this.pixelBlocksPerSide;

        // fill list with empty blocks
        for(int i = 0; i < (this.pixelBlocksPerSide*this.pixelBlocksPerSide); i++)
        {
            pixelBlocks.add(new ArrayList<>());
        }

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

                matchingPixelToBlocks.put(gbPixels[posPixels], posPB);
                pixelBlocks.get(posPB).add(gbPixels[posPixels]);
            }
            pbWidth = 0;
            numberOfPixelsWidth = pixelsPerBlockSide;
        }


        // check if blocks are overall black or white
        for(List<Integer> pixelBlock : pixelBlocks)
        {
            int black = 0;
            int white = 0;

            for(int pixel :  pixelBlock)
            {
                int b = pixel & 255;
                int g = (pixel >> 8) & 255;
                int r = (pixel >> 16) & 255;

                // black pixel
                if(r == 0 & g == 0 & b == 0)
                    black++;
                else
                    white++;
            }

            //? = (black > white) ? true : false;
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

    private List<Integer> getBlocksOfRaster(int fromWidth, int toWidth, int fromHeight, int toHeight) {
        List<Integer> tmpAllBlocks = new ArrayList<>();

        for(int height = fromHeight; height < toHeight; ++height)
        {
            for(int width = fromWidth; width < toWidth; ++width)
            {
                int pos = height * (toWidth - fromWidth) + width;

                tmpAllBlocks.addAll(pixelBlocks.get(pos));
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

        // clear corresponding list of hotPOIsForBot
        this.hotPOIsForBots.get(bot).clear();

        int raster = getRasterOfBot(bot);

        int horizBorder = raster;
        int vertBorder = raster;

        for(int height = vertBorder; height < gameBoardHeight; ++height)
        {
            for(int width = horizBorder; width < gameBoardWidth; ++width)
            {

            }
        }


        // sort ascending to 'hotness' of raster


        // calculate center of raster

        return this.hotPOIsForBots.get(bot).remove(0);
    }

    public RasterNode findNextHotPOI(int bot) {
        return this.hotPOIsForBots.get(bot).remove(0);
    }

    public RasterNode findColdPOI(int bot) {
        return null;
    }

    private int getRasterOfBot(int bot) {
        // return (index) raster of given bot
        return  (bot == 0) ? 3 :
                (bot == 1) ? 2 : 1;
    }


    public RasterNode getRasterBin(RasterNode position)
    {
        
        return null;
    }

    // liefert bin + botnummer
    public Map<Integer, Integer> getMyBotPositions()
    {
        // TODO only after first colochange

        Map<Integer, Integer> botPositions = new HashMap<>();

        for(int i = 0; i < 3; ++i)
        {
            // TODO find bin
            botPositions.put((this.playerNumber*10)+i, null);
        }

        return botPositions;
    }

    public void processColorChanges(ColorChange colorChange)
    {
        // update position of players bot
        this.playersBotsPositions.put((colorChange.player*10)+colorChange.bot, null);
    }
}
