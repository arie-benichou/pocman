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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pocman.game.Move;
import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.maze.MazeNode.Type;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public final class MazeAsGraph implements Supplier<UndirectedGraph<MazeNode>> {

    private final MazeAsBoard board;

    public MazeAsBoard getBoard() {
        return this.board;
    }

    private final Map<Integer, MazeNode> walkableGameTiles;

    public Map<Integer, MazeNode> getWalkableNodes() {
        return this.walkableGameTiles;
    }

    private final UndirectedGraph<MazeNode> graph;

    private final int numberOfVertices;

    public int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    public static MazeAsGraph from(final MazeAsBoard board) {
        return new MazeAsGraph(board);
    }

    public static MazeAsGraph from(final String data) {
        return from(MazeAsBoard.from(data));
    }

    private Map<Integer, MazeNode> buildVertices(final MazeAsBoard board) {
        final Builder<Integer, MazeNode> builder = new ImmutableSortedMap.Builder<Integer, MazeNode>(Ordering.natural());
        for (int i = 0; i < MazeAsBoard.SIZE; ++i) {
            final HashSet<Move> options = Sets.newHashSet();
            if (board.getCell(i).isWalkable()) {
                final List<Direction> directions = board.getDirections(i);
                for (final Direction direction : directions) {
                    final Move move = Move.from(direction);
                    final int neighbour = i + move.getDelta();
                    if (board.getCell(neighbour).isWalkable()) options.add(move);
                }
            }
            if (!options.isEmpty()) builder.put(i, MazeNode.from(i, options));
        }
        return builder.build();
    }

    private Map<Integer, List<WeightedEdge<MazeNode>>> buildEdges(final Map<Integer, MazeNode> gameTiles) {
        final Builder<Integer, List<WeightedEdge<MazeNode>>> builder =
                                                                       new ImmutableSortedMap.Builder<Integer, List<WeightedEdge<MazeNode>>>(Ordering.natural());
        for (final MazeNode gameTile : gameTiles.values()) {
            if (!gameTile.is(Type.STREET)) {
                final List<WeightedEdge<MazeNode>> edges = Lists.newArrayList();
                for (final Move move : gameTile.getOptions()) {
                    final List<Integer> betweenTiles = Lists.newArrayList();
                    MazeNode currentTile = gameTile;
                    while ((currentTile = gameTiles.get(currentTile.getId() + move.getDelta())).is(Type.STREET))
                        betweenTiles.add(currentTile.getId());
                    edges.add(WeightedEdge.from(gameTile, currentTile, betweenTiles.size()));
                }
                builder.put(gameTile.getId(), ImmutableList.copyOf(edges));
            }
        }
        return builder.build();
    }

    private MazeAsGraph(final MazeAsBoard board) {
        this.board = board;
        this.walkableGameTiles = this.buildVertices(this.getBoard());
        final Map<Integer, List<WeightedEdge<MazeNode>>> edgeByNodeId = this.buildEdges(this.getWalkableNodes());
        this.numberOfVertices = edgeByNodeId.size();
        final UndirectedGraph.Builder<MazeNode> graphBuilder = new UndirectedGraph.Builder<MazeNode>(this.getNumberOfVertices());
        for (final Entry<Integer, List<WeightedEdge<MazeNode>>> entry : edgeByNodeId.entrySet())
            for (final WeightedEdge<MazeNode> edge : entry.getValue())
                if (!graphBuilder.contains(edge)) graphBuilder.addEdge(edge);
        this.graph = graphBuilder.build();
    }

    public boolean hasIsland() {
        return !this.graph.isConnected();
    }

    public MazeNode getNodeById(final int nodeId) {
        return this.getWalkableNodes().get(nodeId); // TODO
    }

    @Override
    public UndirectedGraph<MazeNode> get() {
        return this.graph;
    }

}