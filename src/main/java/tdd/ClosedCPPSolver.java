
package tdd;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import tdd.MatchingSolver.Match;
import tdd.MatchingSolver.Position;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import cpp.Arc;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.view.BoardView;

public class ClosedCPPSolver {

    private final GameGraph gameGraph;

    public GameGraph getGameGraph() {
        return this.gameGraph;
    }

    public List<Edge> getEdgesByVertexId(final Integer vertexId) {
        return this.gameGraph.getEdgesByVertexId(vertexId);
    }

    private Path getShortestPaths(final Integer vertexIndex1, final Integer vertexIndex2) {
        return this.gameGraph.getShortestPath(vertexIndex1, vertexIndex2);
    }

    public Integer getVertexIdByVertexIndex(final Integer vertexIndex) {
        return this.gameGraph.getVertexIdByVertexIndex(vertexIndex);
    }

    public Integer getVertexIndexByVertexId(final Integer vertexId) {
        return this.gameGraph.getVertexIndexByVertexId(vertexId);
    }

    private final Map<Edge, Integer> edgeInstancesByEdgeHascode;

    private final ImmutableList<Integer> oddVerticeIndexes;

    public ImmutableList<Integer> getOddVerticeIndexes() {
        return this.oddVerticeIndexes;
    }

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

    private List<Integer> mapVerticeIdsToVerticeIndexes(final List<Integer> verticeIds) {
        return Lists.transform(verticeIds, new Function<Integer, Integer>() {

            @Override
            public Integer apply(final Integer vertexId) {
                return ClosedCPPSolver.this.getVertexIndexByVertexId(vertexId);
            }
        });
    }

    /*
    private List<Integer> mapVerticeIndexesToVerticeIds(final List<Integer> verticeIds) {
        return Lists.transform(verticeIds, new Function<Integer, Integer>() {

            @Override
            public Integer apply(final Integer vertexId) {
                return ClosedCPPSolver.this.getVertexIdByVertexIndex(vertexId);
            }
        });
    }
    */

    private Function<Position, Path> mapPositionToPath(final List<Integer> oddVertices) {
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
    public void fleuryEulerianTrailAlgorithm(final int vertexId, final Map<Edge, Integer> map, final List<Integer> trail) {
        final List<Edge> edges = this.getEdgesByVertexId(vertexId);
        for (final Edge edge : edges) {
            final Integer integer = map.get(edge);
            if (integer < 1) continue;
            map.put(edge, integer - 1);
            final int v = edge.getFirstVertex() == vertexId ? edge.getLastVertex() : edge.getFirstVertex();
            //final int v = edge.getLastVertex();
            this.fleuryEulerianTrailAlgorithm(v, map, trail);
        }
        trail.add(vertexId);
    }

    public void debugTrail(final List<Integer> trail) throws InterruptedException {
        for (final Integer integer : trail) {
            if (integer.equals(513)) continue;
            final char[] array1 = this.gameGraph.getBoard().toCharArray();
            array1[integer] = Constants.PACMAN;
            System.out.println(integer);
            System.out.println(new BoardView().render(Board.from(array1)));
            Thread.sleep(400);
        }
    }

    public ClosedCPPSolver(final GameGraph gameGraph) {
        this.gameGraph = gameGraph;
        final Map<Edge, Integer> edgeInstancesByEdgeHascode = Maps.newHashMap();
        final Set<Integer> oddVerticeIds = Sets.newHashSet();

        /*
        System.out.println(this.gameGraph.getNumberOfVertices());
        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {}
        */

        for (int i = 0; i < this.gameGraph.getNumberOfVertices(); ++i) {
            final Integer vertexId = this.getVertexIdByVertexIndex(i);
            final List<Edge> edges = this.getEdgesByVertexId(vertexId);
            if (edges.size() % 2 == 1) oddVerticeIds.add(vertexId);
            for (final Edge edge : edges)
                edgeInstancesByEdgeHascode.put(edge, 1);
        }
        this.lowerBoundCost = computeCost(edgeInstancesByEdgeHascode);
        // TODO ?! contracter les noeuds de type corner en utilisant un path
        final HashSet<Integer> prunableOddVertices = Sets.newHashSet();
        for (final Integer u : oddVerticeIds) {
            final List<Edge> edges = this.getEdgesByVertexId(u);
            if (edges.size() == 1) {
                final Edge edge = edges.get(0);
                final Integer v = edge.getLastVertex();
                if (oddVerticeIds.contains(v) && !prunableOddVertices.contains(v)) {
                    // TODO revoir l'élagage
                    //prunableOddVertices.add(u);
                    //prunableOddVertices.add(v);
                    //edgeInstancesByEdgeHascode.put(edge, edgeInstancesByEdgeHascode.get(edge) + 1);
                }
            }
        }
        // TODO ?! trier de manière à provoquer le plus de coupures lors du branch & bound
        final List<Integer> prunedOddVertices = this.mapVerticeIdsToVerticeIndexes(Lists.newArrayList(Sets.difference(oddVerticeIds, prunableOddVertices)));

        final TreeMap<Double, List<Integer>> map = Maps.newTreeMap();

        for (final Integer vertexIndex : prunedOddVertices) {
            final Integer vertexId = this.getVertexIdByVertexIndex(vertexIndex);
            final List<Edge> edges = this.getEdgesByVertexId(vertexId);
            /*
            double cost = 0;
            for (final Edge edge : edges) {
                cost += edge.getCost();
            }
            cost = cost / edges.size();
            */
            //cost = edges.size() + cost / 100;

            double cost = Double.POSITIVE_INFINITY;
            for (final Edge edge : edges) {
                if (edge.getCost() < cost)
                    cost = edge.getCost();
                //cost += edge.getCost();
            }

            List<Integer> list = map.get(cost);
            if (list == null) list = Lists.newArrayList();
            list.add(vertexIndex);
            map.put(cost, list);
            //System.out.println(vertexId + " : " + cost);
        }

        /*
                1.01=[20, 18]
                1.02=[37, 17, 8]
                1.03=[38, 7, 26, 15]
                1.04=[12, 25]
                1.08=[32, 4, 28]
                1.1=[13]
                1.12=[40]
                1.1400000000000001=[35]
                3.013333333333333=[36]
                3.0233333333333334=[6]
                3.026666666666667=[11, 24]
                3.033333333333333=[21, 3]
                3.046666666666667=[30, 31]
                3.0766666666666667=[43, 1, 34]
        */

        /*
        for (final Entry<Double, List<Integer>> entry : map.entrySet()) {
            //System.out.println(entry);
        }
        */

        //System.exit(0);

        //System.out.println(map.values());

        //final List<Integer> sortedPrunedOddVertices = Lists.reverse(Lists.newArrayList(Iterables.concat(map.values())));
        final List<Integer> sortedPrunedOddVertices = prunedOddVertices;
        //final List<Integer> sortedPrunedOddVertices = Lists.newArrayList(Iterables.concat(map.values()));
        //System.out.println(sortedPrunedOddVertices);

        //System.exit(0);

        System.out.println(sortedPrunedOddVertices);

        final Graph residualGraph = this.buildResidualGraph(sortedPrunedOddVertices);
        //final Match match = new MatchingSolver(residualGraph).match(Extremum.MIN);
        final Match match = new MatchingSolver(residualGraph).edmondMatch();
        System.out.println(match);
        //System.out.println(match2);
        //System.exit(0);

        for (final Path path : match.apply(this.mapPositionToPath(sortedPrunedOddVertices)))
            for (final Edge edge : path.getEdges())
                edgeInstancesByEdgeHascode.put(edge, edgeInstancesByEdgeHascode.get(edge) + 1);
        this.totalCost = computeCost(edgeInstancesByEdgeHascode);
        this.extraCost = this.getTotalCost() - this.getLowerBoundCost();
        final Builder<Edge, Integer> builder = new ImmutableMap.Builder<Edge, Integer>();
        this.edgeInstancesByEdgeHascode = builder.putAll(edgeInstancesByEdgeHascode).build();

        for (final Entry<Edge, Integer> entry : edgeInstancesByEdgeHascode.entrySet()) {
            System.out.println(entry);
        }

        /*
        try {
            Thread.sleep(2000);
        }
        catch (final InterruptedException e) {}
        */

        /*
        final List<Integer> sortedListOfOddVerticeIds = Lists.newArrayList(oddVerticeIds);
        Collections.sort(sortedListOfOddVerticeIds);
        this.oddVerticeIndexes = ImmutableList.copyOf(this.mapVerticeIdsToVerticeIndexes(sortedListOfOddVerticeIds));
        */

        //Collections.sort(sortedPrunedOddVertices);
        this.oddVerticeIndexes = ImmutableList.copyOf(sortedPrunedOddVertices);
    }

    public static GameGraph newGameGraph(final ClosedCPPSolver closedCPPSolver, final Arc virtualFrom, final Arc virtualTo) {

        final GameGraph gameGraph = closedCPPSolver.getGameGraph();

        final Map<Integer, List<Edge>> edgesByVertexId = Maps.newHashMap(gameGraph.getEdgesByVertexId());

        final double virtualCost = virtualFrom.getCost();
        final Integer virtualVertexId = gameGraph.getVertexIdByVertexIndex(virtualFrom.getU());
        Preconditions.checkState(virtualVertexId == 513); // TODO MAX ID
        final Integer startingVertexId = gameGraph.getVertexIdByVertexIndex(virtualFrom.getV());
        final Integer startingOrOddVertexId = gameGraph.getVertexIdByVertexIndex(virtualTo.getU());

        // TODO ? gérer le cas où startingOrOddVertexId = startingVertexId

        final Edge edge1 = new Edge(virtualVertexId, startingVertexId, virtualCost);
        final Edge symetricOfEdge1 = edge1.getSymetric();
        final Edge edge2 = new Edge(startingOrOddVertexId, virtualVertexId, virtualCost);
        final Edge symetricOfEdge2 = edge2.getSymetric();

        /*
        System.out.println();

        System.out.println(edge1);
        System.out.println(symetricOfEdge1);
        System.out.println(edge2);
        System.out.println(symetricOfEdge2);

        System.out.println();
        */

        final List<Edge> edgesOfStartingVertex = Lists.newArrayList(edgesByVertexId.get(startingVertexId));
        edgesOfStartingVertex.add(symetricOfEdge1);
        edgesByVertexId.put(startingVertexId, ImmutableList.copyOf(edgesOfStartingVertex));

        /*
        System.out.println();
        System.out.println(startingOrOddVertexId);
        System.out.println(startingVertexId);
        System.out.println();

        System.out.println(!startingOrOddVertexId.equals(startingVertexId));
        */

        if (!startingOrOddVertexId.equals(startingVertexId)) {
            final List<Edge> edgesOfStartingOrOddVertexId = Lists.newArrayList(edgesByVertexId.get(startingOrOddVertexId));
            edgesOfStartingOrOddVertexId.add(edge2);
            edgesByVertexId.put(startingOrOddVertexId, ImmutableList.copyOf(edgesOfStartingOrOddVertexId));
            edgesByVertexId.put(virtualVertexId, ImmutableList.of(edge1, symetricOfEdge2));
        }
        else {
            edgesByVertexId.put(virtualVertexId, ImmutableList.of(edge1));
        }
        //edgesByVertexId.put(virtualVertexId, ImmutableList.of(edge1, symetricOfEdge2));

        //System.out.println(edgesOfStartingVertex);
        //System.out.println(edgesOfStartingOrOddVertexId);

        final Builder<Integer, List<Edge>> builder = new ImmutableSortedMap.Builder<Integer, List<Edge>>(Ordering.natural());
        builder.putAll(edgesByVertexId);

        final Map<Integer, List<Edge>> IedgesByVertexId = builder.build();
        /*
        for (final Entry<Integer, List<Edge>> entry : IedgesByVertexId.entrySet())
            System.out.println(entry + ": " + entry.getValue().size());
        */
        //System.exit(0);

        return new GameGraph(gameGraph.getBoard(), gameGraph.getWalkableGameTiles(), IedgesByVertexId);

    }

    public ClosedCPPSolver(final ClosedCPPSolver closedCPPSolver, final Arc virtualFrom, final Arc virtualTo) {
        this(newGameGraph(closedCPPSolver, virtualFrom, virtualTo));
    }

    // TODO ?! utiliser l'objet Vertex à la place de vertexId
    public List<Integer> solveFrom(final int vertexIndex) {
        final List<Integer> trail = Lists.newArrayList();
        this.fleuryEulerianTrailAlgorithm(this.getVertexIdByVertexIndex(vertexIndex), Maps.newHashMap(this.edgeInstancesByEdgeHascode), trail);
        return trail;
    }

    public boolean hasEulerianTrail() {
        return this.getOddVerticeIndexes().isEmpty();
    }

}