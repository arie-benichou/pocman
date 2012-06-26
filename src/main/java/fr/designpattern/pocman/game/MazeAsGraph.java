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

package fr.designpattern.pocman.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.Vertex;
import fr.designpattern.pocman.graph.Vertex.Type;
import fr.designpattern.pocman.graph.WeightedEdge;

public final class MazeAsGraph implements Supplier<UndirectedGraph<Vertex>> {

    private final MazeAsBoard board;

    public MazeAsBoard getBoard() {
        return this.board;
    }

    private final Map<Integer, Vertex> walkableGameTiles;

    public Map<Integer, Vertex> getWalkableNodes() {
        return this.walkableGameTiles;
    }

    private final Map<Integer, List<WeightedEdge<Vertex>>> edgeByVertexId;

    public Map<Integer, List<WeightedEdge<Vertex>>> getEdgeByVertexId() {
        return this.edgeByVertexId;
    }

    private final UndirectedGraph<Vertex> graph;

    private final int numberOfVertices;

    public int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    public static MazeAsGraph from(final MazeAsBoard board) {
        return new MazeAsGraph(board);
    }

    private Map<Integer, Vertex> buildVertices(final MazeAsBoard board) {
        final Builder<Integer, Vertex> builder = new ImmutableSortedMap.Builder<Integer, Vertex>(Ordering.natural());
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
            if (!options.isEmpty()) builder.put(i, Vertex.from(i, options));
        }
        return builder.build();
    }

    private Map<Integer, List<WeightedEdge<Vertex>>> buildEdges(final Map<Integer, Vertex> gameTiles) {
        final Builder<Integer, List<WeightedEdge<Vertex>>> builder = new ImmutableSortedMap.Builder<Integer, List<WeightedEdge<Vertex>>>(Ordering.natural());
        for (final Vertex gameTile : gameTiles.values()) {
            if (!gameTile.is(Type.STREET)) {
                final List<WeightedEdge<Vertex>> edges = Lists.newArrayList();
                for (final Move move : gameTile.getOptions()) {
                    final List<Integer> betweenTiles = Lists.newArrayList();
                    Vertex currentTile = gameTile;
                    while ((currentTile = gameTiles.get(currentTile.getId() + move.getDelta())).is(Type.STREET))
                        betweenTiles.add(currentTile.getId());
                    edges.add(WeightedEdge.from(gameTile, currentTile, betweenTiles.size()));
                }
                Collections.sort(edges); // TODO ? inutile
                builder.put(gameTile.getId(), ImmutableList.copyOf(edges));
            }
        }
        return builder.build();
    }

    private MazeAsGraph(final MazeAsBoard board) {
        this.board = board;
        this.walkableGameTiles = this.buildVertices(this.getBoard());
        this.edgeByVertexId = this.buildEdges(this.getWalkableNodes());
        this.numberOfVertices = this.edgeByVertexId.size();
        final UndirectedGraph.Builder<Vertex> graphBuilder = new UndirectedGraph.Builder<Vertex>(this.getNumberOfVertices());
        for (final Entry<Integer, List<WeightedEdge<Vertex>>> entry : this.edgeByVertexId.entrySet())
            for (final WeightedEdge<Vertex> edge : entry.getValue())
                if (!graphBuilder.contains(edge)) graphBuilder.addEdge(edge);
        this.graph = graphBuilder.build();
    }

    public boolean hasIsland() {
        return this.graph.isConnected();
    }

    public Vertex getNodeById(final int nodeId) {
        return this.getWalkableNodes().get(nodeId); // TODO
    }

    @Override
    public UndirectedGraph<Vertex> get() {
        return this.graph;
    }

}