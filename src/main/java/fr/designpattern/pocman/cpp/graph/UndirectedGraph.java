
package fr.designpattern.pocman.cpp.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.cpp.graph.Path.Factory;

public final class UndirectedGraph<T> implements UndirectedGraphInterface<T> {

    private static final double INFINITY = Double.POSITIVE_INFINITY / 2;

    public final static class Builder<T> {

        private final int numberOfVertices;
        private final Map<T, Integer> vertices = Maps.newHashMap();
        private final Map<T, List<WeightedEdge<T>>> edgesByVertex = Maps.newHashMap();
        private final Map<T, Set<T>> mGraph = new HashMap<T, Set<T>>();

        private int vertexCounter = 0;
        private final double[][] values;

        private final Set<WeightedEdge<T>> edgeSet = Sets.newHashSet();
        private final WeightedEdge.Factory<T> edgeFactory;

        public Builder(final int numberOfVertices) {
            Preconditions.checkArgument(numberOfVertices >= 2, numberOfVertices);
            this.numberOfVertices = numberOfVertices;
            this.values = new double[numberOfVertices][numberOfVertices];
            this.edgeFactory = new WeightedEdge.Factory<T>();
        }

        public int getNumberOfVertices() {
            return this.numberOfVertices;
        }

        public boolean contains(final WeightedEdge<T> edge) {
            return this.edgeSet.contains(edge);
        }

        private Integer getEndPointIndex(final T endPoint) {
            Integer endPointIndex = this.vertices.get(endPoint);
            if (endPointIndex == null) {
                Preconditions.checkState(this.vertexCounter != this.numberOfVertices, "Maximal number of vertices (" + this.numberOfVertices + ") reached.");
                endPointIndex = ++this.vertexCounter;
                this.vertices.put(endPoint, endPointIndex);
                this.mGraph.put(endPoint, new HashSet<T>());
                this.edgesByVertex.put(endPoint, new ArrayList<WeightedEdge<T>>());
            }
            return endPointIndex;
        }

        public Builder<T> addEdge(final WeightedEdge<T> edge) {

            Preconditions.checkArgument(edge != null);
            Preconditions.checkState(!this.contains(edge), "Edge " + edge + " is already defined.");

            final T endPoint1 = edge.getEndPoint1();
            final Integer endPoint1Index = this.getEndPointIndex(endPoint1);

            final T endPoint2 = edge.getEndPoint2();
            final Integer endPoint2Index = this.getEndPointIndex(endPoint2);

            Preconditions.checkState(this.values[endPoint1Index - 1][endPoint2Index - 1] == 0, "Incoherence."); // TODO ? inutile

            this.edgeSet.add(edge);

            this.values[endPoint1Index - 1][endPoint2Index - 1] = edge.getWeight();
            final List<WeightedEdge<T>> endPoint1Edges = this.edgesByVertex.get(endPoint1);
            endPoint1Edges.add(edge);
            this.mGraph.get(endPoint1).add(endPoint2);

            this.values[endPoint2Index - 1][endPoint1Index - 1] = edge.getWeight();
            final List<WeightedEdge<T>> endPoint2Edges = this.edgesByVertex.get(endPoint2);
            endPoint2Edges.add(edge);
            this.mGraph.get(endPoint2).add(endPoint1);

            return this;
        }

        public Builder<T> addEdge(final T endPoint1, final T endPoint2, final double weight) {
            return this.addEdge(this.edgeFactory.newEdge(endPoint1, endPoint2, weight));
        }

        public UndirectedGraph<T> build() {
            Preconditions.checkState(
                    this.numberOfVertices == this.vertexCounter,
                    "Declared number of vertices (" + this.numberOfVertices + ") does not match number of defined vertices (" + this.vertexCounter + ")"
                    );
            return new UndirectedGraph<T>(this);
        }
    }

    private final int n;
    private final Map<T, Integer> vertices;
    private final Map<T, List<WeightedEdge<T>>> edgesByVertex;
    private final Map<Integer, WeightedEdge<T>> edgeByHashCode;

    private final Map<T, Set<T>> mGraph;

    //private final double[][] values;
    private final boolean isConnected;
    //private final Map<Integer, Path<T>> shortestPathsByHashcode;
    private final HashMap<Integer, T> verticeByIndex;
    private final Path<T>[][] shortestPaths;

    private Path<T>[][] buildPathMatrix() {
        final Factory<T> pathFactory = new Path.Factory<T>();
        @SuppressWarnings("unchecked")
        final Path<T>[][] paths = new Path[this.n][this.n];
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                Path<T> path;
                if (i == j) path = pathFactory.newPath(0);
                else {
                    path = pathFactory.newPath(INFINITY);

                    final T endpoint1 = this.verticeByIndex.get(i);
                    final T endpoint2 = this.verticeByIndex.get(j);
                    //System.out.println(this.edgeExists(endpoint1, endpoint2));
                    if (this.edgeExists(endpoint1, endpoint2)) {
                        //System.out.println(endpoint1 + ", " + endpoint2);
                        final WeightedEdge<T> edge = this.getEdge(endpoint1, endpoint2);
                        //System.out.println(edge);
                        //System.out.println();
                        path = pathFactory.newPath(edge);
                    }
                }
                paths[i][j] = path;
            }
        }
        return paths;
    }

    private UndirectedGraph(final Builder<T> builder) {

        this.n = builder.numberOfVertices;

        this.vertices = builder.vertices;

        final com.google.common.collect.ImmutableMap.Builder<T, List<WeightedEdge<T>>> edgesByVertexBuilder =
                                                                                                              new ImmutableMap.Builder<T, List<WeightedEdge<T>>>();
        for (final Entry<T, List<WeightedEdge<T>>> entry : builder.edgesByVertex.entrySet())
            edgesByVertexBuilder.put(entry.getKey(), ImmutableList.copyOf(entry.getValue()));

        this.edgesByVertex = edgesByVertexBuilder.build();

        this.edgeByHashCode = Maps.newHashMap();
        for (final T vertex : this.vertices.keySet()) {
            final List<WeightedEdge<T>> edges = this.getEdges(vertex);
            for (final WeightedEdge<T> weightedEdge : edges) {
                final Integer hashCode = WeightedEdge.Factory.hashCode(weightedEdge.getEndPoint1().hashCode(), weightedEdge.getEndPoint2().hashCode());
                this.edgeByHashCode.put(hashCode, weightedEdge);
            }
        }

        this.verticeByIndex = Maps.newHashMap();
        for (final Entry<T, Integer> entry : this.vertices.entrySet())
            this.verticeByIndex.put(entry.getValue() - 1, entry.getKey());

        this.mGraph = builder.mGraph; // TODO immutable copy

        //this.debug(builder.values, INFINITY);

        // safe copy
        final double[][] values = new double[this.n][this.n];
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (i == j) continue;
                final double value = builder.values[i][j];
                values[i][j] = value == 0 ? INFINITY : value;
            }
        }

        //this.debug(values, INFINITY);

        //System.out.println(this.vertices);
        //System.out.println(this.verticeByIndex);

        final Path<T>[][] paths = this.buildPathMatrix();

        // Roy-Warshall-Floyd algorithm
        for (int k = 0; k < this.n; ++k) {
            for (int i = 0; i < this.n; ++i) {
                for (int j = 0; j < this.n; ++j) {
                    //if (i == j) continue;
                    final double currentCost = paths[i][j].getWeight();
                    final double newCost = paths[i][k].getWeight() + paths[k][j].getWeight();
                    if (newCost < currentCost) paths[i][j] = paths[i][k].add(paths[k][j]);
                }
            }
        }

        this.shortestPaths = paths;

        final double[][] costs = new double[this.n][this.n];
        for (int ii = 0; ii < this.n; ++ii) {
            for (int jj = 0; jj < this.n; ++jj) {
                if (ii == jj) continue;
                costs[ii][jj] = this.shortestPaths[ii][jj].getWeight();
            }
        }
        //this.debug(costs, Double.POSITIVE_INFINITY);

        // a connected graph has a path between every pair of nodes
        boolean isConnected = true;
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (i == j) continue;
                if (this.shortestPaths[i][j].getWeight() == INFINITY) isConnected = false;
            }
        }
        this.isConnected = isConnected;

        // TODO à virer
        //this.shortestPathsByHashcode = Maps.newTreeMap();
        /*
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (i == j) continue;
                final int hashCode1 = i * this.n + j;
                final int hashCode2 = j * this.n + i;
                final int hashCode = hashCode1 * hashCode2;
                if (this.shortestPathsByHashcode.containsKey(hashCode)) continue;
                this.shortestPathsByHashcode.put(hashCode, paths[i][j]);
            }
        }
        */

        //System.exit(0);

    }

    public Path<T> getShortestPathBetween(final T endPoint1, final T endPoint2) {

        Preconditions.checkNotNull(endPoint1);
        Preconditions.checkNotNull(endPoint2);

        if (!this.vertices.containsKey(endPoint1) || !this.vertices.containsKey(endPoint2))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        final int i = this.vertices.get(endPoint1) - 1;
        final int j = this.vertices.get(endPoint2) - 1;

        return this.shortestPaths[i][j];
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    /*
    private void debug(final double[][] array, final double infinity) {
        double max = 0;
        for (int i = 0; i < this.n; ++i)
            for (int j = 0; j < this.n; ++j)
                if (i != j && array[i][j] > max && array[i][j] != infinity) max = array[i][j];
        final int n = (int) Math.floor(Math.log10(Double.valueOf(max).intValue())) + 1;
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                String string;
                if (i == j) string = Strings.repeat(".", n);
                else if (array[i][j] == infinity) string = Strings.repeat("X", n);
                else string = Strings.padStart(String.valueOf((int) array[i][j]), n, '0');
                System.out.print(string + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    */

    /*
    public static void main(final String[] args) {
        final Builder<Vertex> builder = new Builder<Vertex>(5);

        builder.addEdge(Vertex.from(1), Vertex.from(2), 1.0);
        builder.addEdge(Vertex.from(2), Vertex.from(3), 1.0);
        builder.addEdge(Vertex.from(1), Vertex.from(3), 1.0);
        builder.addEdge(Vertex.from(2), Vertex.from(4), 1.0);
        builder.addEdge(Vertex.from(4), Vertex.from(5), 1.0);

        final UndirectedGraph<Vertex> graph = builder.build();
        System.out.println(graph.isConnected());
    }
    */

    @Override
    public Iterator<T> iterator() {
        return this.vertices.keySet().iterator();
    }

    @Override
    public boolean addVertex(final T vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addEdge(final T endpoint1, final T endpoint2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeEdge(final T endpoint1, final T endpoint2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean edgeExists(final T endpoint1, final T endpoint2) {
        if (!this.vertices.containsKey(endpoint1) || !this.vertices.containsKey(endpoint2))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        return this.mGraph.get(endpoint1).contains(endpoint2);
    }

    @Override
    public boolean isEmpty() {
        return this.vertices.isEmpty(); // should always be false
    }

    @Override
    public Set<T> getConnectedVerticeSet(final T vertex) {
        final Set<T> edges = this.mGraph.get(vertex);
        if (edges == null) throw new NoSuchElementException("Source node does not exist.");
        return Collections.unmodifiableSet(edges); // TODO
    }

    public WeightedEdge<T> getEdge(final T endpoint1, final T endpoint2) {
        Preconditions.checkNotNull(endpoint1);
        Preconditions.checkNotNull(endpoint2);
        if (!this.vertices.containsKey(endpoint1) || !this.vertices.containsKey(endpoint2))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        final Integer hashCode = WeightedEdge.Factory.hashCode(endpoint1.hashCode(), endpoint2.hashCode());
        return this.edgeByHashCode.get(hashCode);
    }

    @Override
    public List<WeightedEdge<T>> getEdges(final T vertex) { // TODO retourner un Set ?
        final List<WeightedEdge<T>> edges = this.edgesByVertex.get(vertex);
        if (edges == null) throw new NoSuchElementException("Source node does not exist.");
        return edges;
    }

}