
package tdd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import tdd.MatchingSolver.Extremum;
import tdd.MatchingSolver.Match;
import tdd.MatchingSolver.Position;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

    private final Map<Edge, Integer> edgeInstancesByEdgeHascode;

    private final double lowerBoundCost;

    public double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    private final double extraCost;

    public double getExtraCost() {
        return this.extraCost;
    }

    private final double totalCost;

    public double getTotalCost() {
        return this.totalCost;
    }

    private static double computeCost(final Map<Edge, Integer> edgeInstancesByEdgeHascode) {
        double cost = 0;
        for (final Entry<Edge, Integer> entry : edgeInstancesByEdgeHascode.entrySet()) {
            final Edge edge = entry.getKey();
            final Integer k = entry.getValue();
            cost += k * edge.getCost();
        }
        return cost;
    }

    private Graph buildResidualGraph(final List<Integer> oddVertices) {
        final UndirectedGraphBuilder builder = new UndirectedGraphBuilder(oddVertices.size());
        for (int i = 0; i < oddVertices.size(); ++i) {
            for (int j = 0; j < oddVertices.size(); ++j) {
                if (i != j) {
                    final Path path = this.getShortestPaths(oddVertices.get(i), oddVertices.get(j));
                    if (!builder.contains(path.hashCode())) builder.addEdge(path.hashCode(), path.getFirstVertex(), path.getLastVertex(), path.getCost());
                }
            }
        }
        return builder.build();
    }

    private List<Integer> oddVerticeIndexes(final List<Integer> prunedOddVerticeIds) {
        return Lists.transform(prunedOddVerticeIds, new Function<Integer, Integer>() {

            @Override
            public Integer apply(final Integer vertexId) {
                return ClosedCPPSolver.this.getVertexIndexByVertexId(vertexId);
            }

        });
    }

    private Function<Position, Path> mapping(final List<Integer> oddVertices) {
        return new Function<Position, Path>() {

            @Override
            public Path apply(final Position position) {
                final int vertexIndex1 = oddVertices.get(position.getRowIndex());
                final int vertexIndex2 = oddVertices.get(position.getColumnIndex());
                return ClosedCPPSolver.this.getShortestPaths(vertexIndex1, vertexIndex2);
            }

        };
    }

    // TODO extract algo
    public void fleuryEulerianTrailAlgorithm(final int vertexId, final HashMap<Edge, Integer> hashMap, final List<Integer> trail) {
        final List<Edge> edges = this.getEdgesByVertexId(vertexId);
        for (final Edge edge : edges) {
            final Integer integer = hashMap.get(edge);
            if (integer == 0) continue;
            hashMap.put(edge, integer - 1);
            final int v = edge.getFirstVertex() == vertexId ? edge.getLastVertex() : edge.getFirstVertex();
            this.fleuryEulerianTrailAlgorithm(v, hashMap, trail);
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
        final Map<Edge, Integer> edgeInstancesByEdgeHascode = Maps.newHashMap();
        final Set<Integer> oddVertices = Sets.newHashSet();
        for (int i = 0; i < this.gameGraph.getNumberOfVertices(); ++i) {
            final Integer vertexId = this.getVertexIdByVertexIndex(i);
            final List<Edge> edges = this.getEdgesByVertexId(vertexId);
            if (edges.size() % 2 == 1) oddVertices.add(vertexId);
            for (final Edge edge : edges)
                edgeInstancesByEdgeHascode.put(edge, 1);
        }
        this.lowerBoundCost = computeCost(edgeInstancesByEdgeHascode);
        // TODO ?! fusionner les noeuds de type corner en utilisant un path
        final HashSet<Integer> prunableOddVertices = Sets.newHashSet();
        for (final Integer u : oddVertices) {
            final List<Edge> edges = this.getEdgesByVertexId(u);
            if (edges.size() == 1) {
                final Edge edge = edges.get(0);
                final Integer v = edge.getLastVertex();
                if (oddVertices.contains(v)) {
                    if (!prunableOddVertices.contains(v)) {
                        prunableOddVertices.add(u);
                        prunableOddVertices.add(v);
                        //final int key = edge.hashCode();
                        final Integer instances = edgeInstancesByEdgeHascode.get(edge);
                        edgeInstancesByEdgeHascode.put(edge, instances + 1);
                    }
                }
            }
        }
        // TODO ?! trier par degré / somme des chemins : de manière à provoquer le plus de coupures lors du branch & bound
        final List<Integer> prunedOddVertices = this.oddVerticeIndexes(Lists.newArrayList(Sets.difference(oddVertices, prunableOddVertices)));
        final Graph residualGraph = this.buildResidualGraph(prunedOddVertices);
        final Match match = new MatchingSolver(residualGraph).match(Extremum.MIN);
        for (final Path path : match.apply(this.mapping(prunedOddVertices)))
            for (final Edge edge : path.getEdges())
                edgeInstancesByEdgeHascode.put(edge, edgeInstancesByEdgeHascode.get(edge) + 1);
        this.totalCost = computeCost(edgeInstancesByEdgeHascode);
        this.extraCost = this.getTotalCost() - this.getLowerBoundCost();
        final Builder<Edge, Integer> builder = new ImmutableMap.Builder<Edge, Integer>();
        this.edgeInstancesByEdgeHascode = builder.putAll(edgeInstancesByEdgeHascode).build();
    }

    // TODO ?! utiliser l'objet Vertex à la place de vertexId
    public List<Integer> solveFrom(final int vertexIndex) throws InterruptedException {
        final List<Integer> trail = Lists.newArrayList();
        this.fleuryEulerianTrailAlgorithm(this.getVertexIdByVertexIndex(vertexIndex), Maps.newHashMap(this.edgeInstancesByEdgeHascode), trail);
        return trail;
    }

}