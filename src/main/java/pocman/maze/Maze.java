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

package pocman.maze;

import pocman.graph.UndirectedGraph;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public class Maze implements Supplier<UndirectedGraph<MazeNode>> {

    private final MazeAsGraph mazeAsGraph;

    public Maze(final String data) {
        this.mazeAsGraph = MazeAsGraph.from(MazeAsBoard.from(data));
        Preconditions.checkState(this.mazeAsGraph.hasIsland(), "Maze must not contain island");
    }

    public char[] toCharArray() {
        return this.mazeAsGraph.getBoard().toCharArray();
    }

    public MazeNode getNode(final int nodeId) {
        return this.mazeAsGraph.getNodeById(nodeId);
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

}