
package fr.designpattern.pocman.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.cpp.graph.Vertex.Type;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;
import fr.designpattern.pocman.view.MazeAsGraphView;

public class MazeAsGraph {

    private final MazeAsBoard board;

    public MazeAsBoard getBoard() {
        return this.board;
    }

    private final Map<Integer, Vertex> walkableGameTiles;

    public Map<Integer, Vertex> getWalkableGameTiles() {
        return this.walkableGameTiles;
    }

    private final Map<Integer, List<WeightedEdge<Vertex>>> edgeByVertexId;

    public Map<Integer, List<WeightedEdge<Vertex>>> getEdgeByVertexId() {
        return this.edgeByVertexId;
    }

    private final WeightedEdge.Factory<Vertex> edgeFactory = new WeightedEdge.Factory<Vertex>();

    public WeightedEdge.Factory<Vertex> getEdgeFactory() {
        return this.edgeFactory;
    }

    private final UndirectedGraph<Vertex> graph;

    public UndirectedGraph<Vertex> getGraph() {
        return this.graph;
    }

    private final int numberOfVertices;

    public int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    public static class Factory {

        public MazeAsGraph newMazeAsGraph(final MazeAsBoard board) {
            return new MazeAsGraph(board);
        }

    }

    private Map<Integer, Vertex> buildVertices(final MazeAsBoard board) {
        final Builder<Integer, Vertex> builder = new ImmutableSortedMap.Builder<Integer, Vertex>(Ordering.natural());
        for (int i = 0; i < MazeAsBoard.SIZE; ++i) {
            final HashSet<Move> options = Sets.newHashSet();
            if (board.getCell(i) == Constants.PILL || board.getCell(i) == Constants.SPACE) {
                final List<Direction> directions = board.getDirections(i);
                for (final Direction direction : directions) {
                    final Move move = Move.from(direction);
                    final int neighbour = i + move.getDelta();
                    if (board.getCell(neighbour) == Constants.PILL || board.getCell(neighbour) == Constants.SPACE) options.add(move);
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
                    edges.add(this.edgeFactory.newEdge(gameTile, currentTile, betweenTiles.size()));
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
        this.edgeByVertexId = this.buildEdges(this.getWalkableGameTiles());
        this.numberOfVertices = this.getEdgeByVertexId().size();
        final UndirectedGraph.Builder<Vertex> graphBuilder = new UndirectedGraph.Builder<Vertex>(this.getNumberOfVertices());
        for (final Entry<Integer, List<WeightedEdge<Vertex>>> entry : this.getEdgeByVertexId().entrySet())
            for (final WeightedEdge<Vertex> edge : entry.getValue())
                if (!graphBuilder.contains(edge)) graphBuilder.addEdge(edge);
        this.graph = graphBuilder.build();
    }

    public boolean isConnected() {
        return this.getGraph().isConnected();
    }

    @Override
    public String toString() {
        return new MazeAsGraphView().render(this); // TODO
    }

    public Vertex getNodeById(final int nodeId) {
        return this.getWalkableGameTiles().get(nodeId); // TODO
    }

}