/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package pocman.game;

import graph.Path;
import graph.UndirectedGraph;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Supplier;

public final class Maze implements Supplier<UndirectedGraph<MazeNode>> {

    private final MazeAsGraph mazeAsGraph;

    private static Maze from(final MazeAsGraph mazeAsGraph) {
        //Preconditions.checkState(!mazeAsGraph.hasIsland(), "Maze must not contain island."); // TODO à faire lors du CPP
        return new Maze(mazeAsGraph);
    }

    public static Maze from(final String data) {
        return from(MazeAsGraph.from(MazeAsBoard.from(data)));
    }

    public static Maze from(final char[] data) {
        return from(MazeAsGraph.from(MazeAsBoard.from(data)));
    }

    private Maze(final MazeAsGraph mazeAsGraph) {
        this.mazeAsGraph = mazeAsGraph;
    }

    public char[] toCharArray() {
        return this.mazeAsGraph.getBoard().toCharArray();
    }

    public MazeNode getNode(final int nodeId) {
        return this.mazeAsGraph.getNode(nodeId);
    }

    public int size() {
        return MazeAsBoard.SIZE;
    }

    public int width() {
        return MazeAsBoard.WIDTH;
    }

    public int height() {
        return MazeAsBoard.HEIGHT;
    }

    @Override
    public UndirectedGraph<MazeNode> get() {
        return this.mazeAsGraph.get();
    }

    public MazeAsBoard getBoard() {
        return this.mazeAsGraph.getBoard();
    }

    // TODO ? retourner un MazePath (liste de vertices)
    public Path<MazeNode> getShortestPath(final MazeNode endPoint1, final MazeNode endPoint2) {
        return this.mazeAsGraph.getShortestPath(endPoint1, endPoint2);
    }

    public MazeNode getNearestGraphNode(final int mazeNodeId) {
        return this.mazeAsGraph.getNearestGraphNode(mazeNodeId);
    }

    public MazeNode getNearestGraphNode(final MazeNode mazeNode) {
        return this.getNearestGraphNode(mazeNode.getId());
    }

    public MazeNode find(final Tile tile, final MazeNode mazeNode) {
        final int nodeId = this.getBoard().find(tile, mazeNode.getId() + 1);
        return this.getNode(nodeId);
    }

    public MazeNode find(final Tile tile) {
        final int nodeId = this.getBoard().find(tile);
        return this.getNode(nodeId);
    }

    public Map<MazeNode, Entry<Move, Integer>> getGraphNodeRange(final MazeNode mazeNode) {
        return this.mazeAsGraph.getGraphNodeRange(mazeNode);
    }

    public boolean hasIsland() {
        return this.mazeAsGraph.hasIsland();
    }

    public int getNumberOfMazeNodes() {
        return this.mazeAsGraph.getNumberOfMazeNodes();
    }

    public boolean hasPath(final MazeNode endPoint1, final MazeNode endPoint2) {
        return this.mazeAsGraph.hasPath(endPoint1, endPoint2);
    }

}