
package tdd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tdd.MatchingSolver.Extremum;
import tdd.MatchingSolver.Match;
import tdd.MatchingSolver.Position;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.view.BoardView;

public class ClosedCPPSolver {

    private final GameGraph gameGraph;

    public List<Edge> getEdgesByVertexId(final Integer vertexId) {
        return this.gameGraph.getEdgesByVertexId(vertexId);
    }

    private Path getShortestPaths(final Integer vertexIndex1, final Integer vertexIndex2) {
        return this.gameGraph.getShortestPath(vertexIndex1, vertexIndex2);
    }

    public Integer getVertexIdByVertexIndex(final Integer vertexIndex) {
        return this.gameGraph.getVertexIdByVertexIndex(vertexIndex);
    }

    public Integer getVertexIndexByVertexId(final Integer vertexIndex) {
        return this.gameGraph.getVertexIndexByVertexId(vertexIndex);
    }

    private Map<Integer, Integer> mapOfEdgesHascodesByIntance;

    private Graph buildResidualGraph(final List<Integer> oddNodes) {
        final UndirectedGraphBuilder builder = new UndirectedGraphBuilder(oddNodes.size());
        for (int i = 0; i < oddNodes.size(); ++i) {
            for (int j = 0; j < oddNodes.size(); ++j) {
                if (i != j) {
                    final Path path = this.getShortestPaths(oddNodes.get(i), oddNodes.get(j));
                    if (!builder.contains(path.hashCode())) builder.addEdge(path.hashCode(), path.getFirstNode(), path.getLastNode(), path.getCost());
                }
            }
        }
        return builder.build();
    }

    // TODO extract algo
    public void fleuryEulerianTrailAlgorithm(final int vertexId, final HashMap<Integer, Integer> mapOfEdgesHascodesByIntance, final List<Integer> trail) {
        final List<Edge> edges = this.getEdgesByVertexId(vertexId);
        for (final Edge edge : edges) {
            final Integer integer = mapOfEdgesHascodesByIntance.get(edge.hashCode());
            if (integer == 0) continue;
            mapOfEdgesHascodesByIntance.put(edge.hashCode(), integer - 1);
            final int v = edge.getFirstNode() == vertexId ? edge.getLastNode() : edge.getFirstNode();
            this.fleuryEulerianTrailAlgorithm(v, mapOfEdgesHascodesByIntance, trail);
        }
        trail.add(vertexId);
    }

    public void debugTrail(final List<Integer> trail) throws InterruptedException {
        for (final Integer integer : trail) {
            final char[] array1 = this.gameGraph.getBoard().toCharArray();
            array1[integer] = Constants.PACMAN;
            System.out.println(new BoardView().render(Board.from(array1)));
            Thread.sleep(600);
        }
    }

    public ClosedCPPSolver(final GameGraph gameGraph) {

        this.gameGraph = gameGraph;

        final Map<Integer, Integer> edgeInstancesByEdgeHascode = Maps.newHashMap();

        final Set<Integer> oddNodes = Sets.newHashSet();
        for (int i = 0; i < this.gameGraph.getNumberOfVertices(); ++i) {
            final Integer vertexId = this.getVertexIdByVertexIndex(i);
            final List<Edge> edges = this.getEdgesByVertexId(vertexId);
            if (edges.size() % 2 == 1) oddNodes.add(vertexId);
            for (final Edge edge : edges)
                edgeInstancesByEdgeHascode.put(edge.hashCode(), 1);
        }

        // TODO ?! fusionner les noeuds de type corner en utilisant un path
        final HashSet<Integer> toBeRemovedFromOddNodes = Sets.newHashSet();
        for (final Integer u : oddNodes) {
            final List<Edge> edges = this.getEdgesByVertexId(u);
            if (edges.size() == 1) {
                final Edge edge = edges.get(0);
                final Integer v = edge.getLastNode();
                if (oddNodes.contains(v)) {
                    if (!toBeRemovedFromOddNodes.contains(v)) {
                        toBeRemovedFromOddNodes.add(u);
                        toBeRemovedFromOddNodes.add(v);
                        final int key = edge.hashCode();
                        final Integer instance = edgeInstancesByEdgeHascode.get(key);
                        edgeInstancesByEdgeHascode.put(key, instance + 1);
                    }
                }
            }
        }

        // TODO ?! trier par degré / somme des chemins

        final List<Integer> oddNodes4 = Lists.transform(
                Lists.newArrayList(Sets.difference(oddNodes, toBeRemovedFromOddNodes)),
                new Function<Integer, Integer>() {

                    @Override
                    public Integer apply(final Integer input) {
                        return ClosedCPPSolver.this.getVertexIndexByVertexId(input);
                    }

                });

        final Graph residualGraph = this.buildResidualGraph(oddNodes4);
        final Match match = new MatchingSolver(residualGraph).match(Extremum.MIN);
        final Function<Position, Path> mapping2 = new Function<Position, Path>() {

            @Override
            public Path apply(final Position position) {
                final int vertexIndex1 = oddNodes4.get(position.getRowIndex());
                final int vertexIndex2 = oddNodes4.get(position.getColumnIndex());
                return ClosedCPPSolver.this.getShortestPaths(vertexIndex1, vertexIndex2);
            }

        };
        for (final Path path : match.apply(mapping2))
            for (final Edge edge : path.getEdges())
                edgeInstancesByEdgeHascode.put(edge.hashCode(), edgeInstancesByEdgeHascode.get(edge.hashCode()) + 1);

        final Builder<Integer, Integer> builder = new ImmutableSortedMap.Builder<Integer, Integer>(Ordering.natural());
        this.mapOfEdgesHascodesByIntance = builder.putAll(edgeInstancesByEdgeHascode).build();

    }

    /*
    public void solveFrom(final int vertexId) throws InterruptedException {
        final List<Integer> trail = Lists.newArrayList();
        this.fleuryEulerianTrailAlgorithm(vertexId, trail);
        this.debugTrail(trail);
    }
    */

    // TODO ?! utiliser l'objet Vertex à la place de vertexId
    public List<Integer> solveFrom(final int vertexIndex) throws InterruptedException {
        final List<Integer> trail = Lists.newArrayList();
        this.fleuryEulerianTrailAlgorithm(this.getVertexIdByVertexIndex(vertexIndex), Maps.newHashMap(this.mapOfEdgesHascodesByIntance), trail);
        return trail;
    }

}