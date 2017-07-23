package logic;

import model.RasterNode;

import java.util.List;

/**
 * Created by matthiasdaiber on 21.06.17.
 */
public interface Pathfinder {

    public List<RasterNode> findPath(RasterNode source, RasterNode target);

}
