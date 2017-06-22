package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthiasdaiber on 21.06.17.
 */
public class GameboardGraph {

    private int pixelBlocksPerSide;
    private List<List<Integer>> pixelBlocks;

    // Idee Map: bool, Int
    // bool gbt an ob feld betretbar ist, false wenn mauer

    public GameboardGraph(int pixelBlocksPerSide)
    {
        this.pixelBlocksPerSide = pixelBlocksPerSide;
        this.pixelBlocks = new ArrayList<>();
    }

    public void calculateGraph(int gbPixelsPerSide, int[] gbPixels)
    {
        int pixelsPerBlockSide = gbPixelsPerSide / this.pixelBlocksPerSide;
        int pixelsPerBlock = pixelBlocksPerSide * pixelsPerBlockSide;

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

                /* break because of float - int conversion
                if(pbHeight == pixelBlocksPerSide)
                    break;
                    */
            }

            for(int x = 0; x < gbPixelsPerSide; x++) {

                if(x == numberOfPixelsWidth) {
                    numberOfPixelsWidth += pixelsPerBlockSide;
                    ++pbWidth;

                    /* break because of float - int conversion
                    if(pbWidth == pixelBlocksPerSide)
                        break;
                        */
                }

                // store current pixel in corresponding ArrayList (which is representing a pixelblock)
                int posPixels = (y * this.pixelBlocksPerSide) + x;
                int posPB = pbHeight * pixelBlocksPerSide + pbWidth;

                //pixelBlocks.get(posPB).add(rgbValues[posPixels]);
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

    /*
      int numberOfBlocks = pixelBlocksPerSide*pixelBlocksPerSide;
        int pixelsPerBlockHeight = height / pixelBlocksPerSide;
        int pixelsPerBlockWidth = width / pixelBlocksPerSide;

        this.vectors = new ArrayList<>();

        // create and populate list with empty 'pixelblocks'
        List<List<Integer>> pixelBlocks = new ArrayList<>();
        for(int i = 0; i < numberOfBlocks; ++i)
            pixelBlocks.add(new ArrayList<>());

        // populate the pixelblocks with the corresponding pixels
        int pbHeight = 0;
        int pbWidth = 0;

        int pixelsHeight = pixelsPerBlockHeight;
        int pixelsWidth = pixelsPerBlockWidth;

        for(int y = 0; y < height; y++) {

            if(y == pixelsHeight) {
                pixelsHeight += pixelsPerBlockHeight;
                ++pbHeight;

                // break because of float - int conversion
                if(pbHeight == pixelBlocksPerSide)
                    break;
            }

            for(int x = 0; x < width; x++) {

                if(x == pixelsWidth) {
                    pixelsWidth += pixelsPerBlockWidth;
                    ++pbWidth;

                    // break because of float - int conversion
                    if(pbWidth == pixelBlocksPerSide)
                        break;
                }

                // store current pixel in corresponding ArrayList (which is representing a pixelblock)
                int posPixels = y * width + x;
                int posPB = pbHeight * pixelBlocksPerSide + pbWidth;

                pixelBlocks.get(posPB).add(rgbValues[posPixels]);
            }
            pbWidth = 0;
            pixelsWidth = pixelsPerBlockWidth;
        }
     */
}