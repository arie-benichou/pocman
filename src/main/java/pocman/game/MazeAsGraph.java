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

import graph.Feature;
import graph.Path;
import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.features.Connectivity;
import graph.features.Routing;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import pocman.game.MazeNode.Type;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public final class MazeAsGraph implements Supplier<UndirectedGraph<MazeNode>> {

    private final MazeAsBoard board;

    public MazeAsBoard getBoard() {
        return this.board;
    }

    private final Map<Integer, MazeNode> mazeNodes;

    public Map<Integer, MazeNode> getWalkableNodes() {
        return this.mazeNodes;
    }

    private final UndirectedGraph<MazeNode> graph;

    private final int numberOfVertices;

    private final SortedMap<Integer, List<WeightedEdge<MazeNode>>> edgeByNodeId;

    public int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    public static MazeAsGraph from(final MazeAsBoard board) {
        return new MazeAsGraph(board);
    }

    public static MazeAsGraph from(final String data) {
        return from(MazeAsBoard.from(data));
    }

    private Map<Integer, MazeNode> buildMazeNodes(final MazeAsBoard board) {
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

    private SortedMap<Integer, List<WeightedEdge<MazeNode>>> buildEdges(final Map<Integer, MazeNode> gameTiles) {
        final ImmutableSortedMap.Builder<Integer, List<WeightedEdge<MazeNode>>> builder =
                                                                                          new ImmutableSortedMap.Builder<Integer, List<WeightedEdge<MazeNode>>>(
                                                                                                  Ordering.natural());
        for (final MazeNode gameTile : gameTiles.values()) {
            if (!gameTile.is(Type.STREET)) {
                final List<WeightedEdge<MazeNode>> edges = Lists.newArrayList();
                for (final Move move : gameTile.getOptions()) {
                    final List<Integer> betweenTiles = Lists.newArrayList();
                    MazeNode currentTile = gameTile;
                    while ((currentTile = gameTiles.get(currentTile.getId() + move.getDelta())).is(Type.STREET))
                        betweenTiles.add(currentTile.getId());
                    edges.add(WeightedEdge.from(gameTile, currentTile, betweenTiles.size() + 1));
                }
                builder.put(gameTile.getId(), ImmutableList.copyOf(edges));
            }
        }
        return builder.build();
    }

    private MazeAsGraph(final MazeAsBoard board) {
        this.board = board;
        this.mazeNodes = this.buildMazeNodes(this.getBoard());
        this.edgeByNodeId = this.buildEdges(this.getWalkableNodes());
        this.numberOfVertices = this.edgeByNodeId.size();
        final UndirectedGraph.Builder<MazeNode> graphBuilder = new UndirectedGraph.Builder<MazeNode>(this.getNumberOfVertices());
        for (final Entry<Integer, List<WeightedEdge<MazeNode>>> entry : this.edgeByNodeId.entrySet())
            for (final WeightedEdge<MazeNode> edge : entry.getValue())
                if (!graphBuilder.contains(edge)) graphBuilder.addEdge(edge);
        this.graph = graphBuilder.build();
    }

    public boolean hasIsland() {
        final Connectivity<MazeNode> connectivityFeature = this.graph.getFeature(Feature.CONNECTIVITY);
        return !connectivityFeature.isConnected();
    }

    public MazeNode getNode(final int nodeId) {
        return this.getWalkableNodes().get(nodeId); // TODO
    }

    public MazeNode getNearestGraphNode(final int mazeNodeId) {
        Preconditions.checkArgument(this.getBoard().getCell(mazeNodeId).isWalkable(), "Node must be walkable");
        final Map<Direction, Tile> neighbours = this.board.getNeighbours(mazeNodeId);
        final List<Move> moves = Lists.newArrayList();
        for (final Entry<Direction, Tile> entry : neighbours.entrySet())
            if (entry.getValue().isWalkable()) moves.add(Move.from(entry.getKey()));
        final TreeMap<Integer, Move> data = Maps.newTreeMap();
        for (final Move move : moves) {
            int k = 0;
            while (this.getNode(mazeNodeId + k * move.getDelta()).is(Type.STREET))
                ++k;
            data.put(k, move);
        }
        System.out.println(data);
        final Entry<Integer, Move> firstEntry = data.firstEntry();
        final MazeNode nearestGraphNode = this.getNode(mazeNodeId + firstEntry.getKey() * firstEntry.getValue().getDelta());
        return nearestGraphNode;
    }

    public Map<MazeNode, Entry<Move, Integer>> getGraphNodeRange(final MazeNode mazeNode) {
        Preconditions.checkArgument(this.getBoard().getCell(mazeNode.getId()).isWalkable(), "Node must be walkable");
        final Map<Direction, Tile> neighbours = this.board.getNeighbours(mazeNode.getId());
        final List<Move> moves = Lists.newArrayList();
        for (final Entry<Direction, Tile> entry : neighbours.entrySet())
            if (entry.getValue().isWalkable()) moves.add(Move.from(entry.getKey()));
        final Map<Move, Integer> data = Maps.newHashMap();
        for (final Move move : moves) {
            int k = 0;
            while (this.getNode(mazeNode.getId() + k * move.getDelta()).is(Type.STREET))
                ++k;
            data.put(move, k);
        }
        System.out.println(data);
        final Map<MazeNode, Entry<Move, Integer>> range = Maps.newHashMap();
        for (final Entry<Move, Integer> entry : data.entrySet()) {
            final Move move = entry.getKey();
            final Integer distance = entry.getValue();
            final MazeNode node = this.getNode(mazeNode.getId() + distance * move.getDelta());
            range.put(node, entry);
        }
        return range;
    }

    @Override
    public UndirectedGraph<MazeNode> get() {
        return this.graph;
    }

    public Path<MazeNode> getShortestPath(final MazeNode endPoint1, final MazeNode endPoint2) {
        final Routing<MazeNode> feature = this.graph.getFeature(Feature.ROUTING);
        return feature.getShortestPathBetween(endPoint1, endPoint2);
    }

    public int getNumberOfMazeNodes() {
        return this.mazeNodes.size();
    }

}