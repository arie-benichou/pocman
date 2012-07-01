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

package pocman.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import pocman.graph.Path.Factory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class UndirectedGraph<T> implements UndirectedGraphInterface<T> { // TODO à revoir

    private static final double INFINITY = Double.POSITIVE_INFINITY / 2;

    public final static class Builder<T> {

        private final int order;
        private final Map<T, Integer> vertices;
        private final Map<T, List<WeightedEdge<T>>> edgesByMazeNode;
        private final Map<T, Set<T>> mGraph;
        private final Set<WeightedEdge<T>> edgeSet;
        private final double[][] values;

        private int ordinal;

        public Builder(final int order) {
            Preconditions.checkArgument(order >= 2, order);
            this.ordinal = 0;
            this.order = order;
            this.mGraph = Maps.newHashMap();
            this.edgeSet = Sets.newHashSet();
            this.vertices = Maps.newHashMap();
            this.edgesByMazeNode = Maps.newHashMap();
            this.values = new double[order][order];
        }

        public int getNumberOfVertices() {
            return this.order;
        }

        public boolean contains(final WeightedEdge<T> edge) {
            return this.edgeSet.contains(edge);
        }

        private Integer getEndPointIndex(final T endPoint) {
            Integer endPointIndex = this.vertices.get(endPoint);
            if (endPointIndex == null) {
                Preconditions.checkState(this.ordinal != this.order, "Maximal number of vertices (" + this.order + ") reached.");
                endPointIndex = ++this.ordinal;
                this.vertices.put(endPoint, endPointIndex);
                this.mGraph.put(endPoint, new HashSet<T>());
                this.edgesByMazeNode.put(endPoint, new ArrayList<WeightedEdge<T>>());
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
            final List<WeightedEdge<T>> endPoint1Edges = this.edgesByMazeNode.get(endPoint1);
            endPoint1Edges.add(edge);
            this.mGraph.get(endPoint1).add(endPoint2);

            this.values[endPoint2Index - 1][endPoint1Index - 1] = edge.getWeight();
            final List<WeightedEdge<T>> endPoint2Edges = this.edgesByMazeNode.get(endPoint2);
            endPoint2Edges.add(edge);
            this.mGraph.get(endPoint2).add(endPoint1);

            return this;
        }

        public Builder<T> addEdge(final T endPoint1, final T endPoint2, final double weight) {
            return this.addEdge(WeightedEdge.from(endPoint1, endPoint2, weight));
        }

        public UndirectedGraph<T> build() {
            Preconditions.checkState(
                    this.order == this.ordinal,
                    "Declared number of vertices (" + this.order + ") does not match number of defined vertices (" + this.ordinal + ")"
                    );
            return new UndirectedGraph<T>(this);
        }
    }

    private final int order;
    private final Map<T, Integer> vertices;
    private final Map<T, List<WeightedEdge<T>>> edgesByMazeNode;
    private final Map<Integer, WeightedEdge<T>> edgeByHashCode;
    private final Map<T, Set<T>> mGraph;
    private final Map<Integer, T> verticeByIndex;
    private final Path<T>[][] shortestPaths;

    private Boolean isConnected;
    private Boolean isEulerian;

    private Path<T>[][] buildPathMatrix() {
        final Factory<T> pathFactory = new Path.Factory<T>();
        @SuppressWarnings("unchecked")
        final Path<T>[][] paths = new Path[this.order][this.order];
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                Path<T> path;
                if (i == j) path = pathFactory.newPath(0);
                else {
                    path = pathFactory.newPath(INFINITY);
                    final T endpoint1 = this.verticeByIndex.get(i);
                    final T endpoint2 = this.verticeByIndex.get(j);
                    if (this.hasEdge(endpoint1, endpoint2)) {
                        final WeightedEdge<T> edge = this.getEdge(endpoint1, endpoint2);
                        path = pathFactory.newPath(edge);
                    }
                }
                paths[i][j] = path;
            }
        }
        return paths;
    }

    private UndirectedGraph(final Builder<T> builder) {

        this.order = builder.order;
        this.vertices = ImmutableMap.copyOf(builder.vertices);
        this.mGraph = ImmutableMap.copyOf(builder.mGraph);
        final ImmutableMap<T, List<WeightedEdge<T>>> edgesByMazeNode = ImmutableMap.copyOf(builder.edgesByMazeNode);
        // safe copy
        final double[][] values = new double[this.order][this.order];
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                if (i == j) continue;
                final double value = builder.values[i][j];
                values[i][j] = value == 0 ? INFINITY : value;
            }
        }

        final ImmutableMap.Builder<T, List<WeightedEdge<T>>> edgesByMazeNodeBuilder = new ImmutableMap.Builder<T, List<WeightedEdge<T>>>();
        for (final Entry<T, List<WeightedEdge<T>>> entry : edgesByMazeNode.entrySet())
            edgesByMazeNodeBuilder.put(entry.getKey(), ImmutableList.copyOf(entry.getValue()));
        this.edgesByMazeNode = edgesByMazeNodeBuilder.build();

        this.edgeByHashCode = Maps.newHashMap();
        for (final T MazeNode : this.vertices.keySet()) {
            final List<WeightedEdge<T>> edges = this.getEdges(MazeNode);
            for (final WeightedEdge<T> weightedEdge : edges) {
                final Integer hashCode = WeightedEdge.hashCode(weightedEdge.getEndPoint1().hashCode(), weightedEdge.getEndPoint2().hashCode());
                this.edgeByHashCode.put(hashCode, weightedEdge);
            }
        }

        this.verticeByIndex = Maps.newHashMap();
        for (final Entry<T, Integer> entry : this.vertices.entrySet())
            this.verticeByIndex.put(entry.getValue() - 1, entry.getKey());

        final Path<T>[][] paths = this.buildPathMatrix();

        // TODO extract Roy-Warshall-Floyd algorithm
        for (int k = 0; k < this.order; ++k) {
            for (int i = 0; i < this.order; ++i) {
                for (int j = 0; j < this.order; ++j) {
                    //if (i == j) continue;
                    final double currentCost = paths[i][j].getWeight();
                    final double newCost = paths[i][k].getWeight() + paths[k][j].getWeight();
                    if (newCost < currentCost) paths[i][j] = paths[i][k].add(paths[k][j]);
                }
            }
        }

        this.shortestPaths = paths;
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

    private boolean computeIsConnected() {
        for (int i = 0; i < this.order; ++i)
            for (int j = 0; j < this.order; ++j)
                if (i != j && this.shortestPaths[i][j].getWeight() == INFINITY) return false;
        return true;
    }

    public boolean isConnected() {
        if (this.isConnected == null) this.isConnected = this.computeIsConnected();
        return this.isConnected;
    }

    @Override
    public Iterator<T> iterator() {
        return this.vertices.keySet().iterator();
    }

    @Override
    public boolean hasEdge(final T endpoint1, final T endpoint2) {
        if (!this.vertices.containsKey(endpoint1) || !this.vertices.containsKey(endpoint2))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        return this.mGraph.get(endpoint1).contains(endpoint2);
    }

    @Override
    public boolean hasMazeNode(final T endpoint) {
        return this.vertices.containsKey(endpoint);
    }

    @Override
    public boolean isEmpty() {
        return this.vertices.isEmpty(); // should always be false
    }

    @Override
    public Set<T> getConnectedVerticeSet(final T MazeNode) {
        final Set<T> edges = this.mGraph.get(MazeNode);
        if (edges == null) throw new NoSuchElementException("Source node does not exist.");
        return Collections.unmodifiableSet(edges); // TODO
    }

    public WeightedEdge<T> getEdge(final T endpoint1, final T endpoint2) {
        Preconditions.checkNotNull(endpoint1);
        Preconditions.checkNotNull(endpoint2);
        if (!this.vertices.containsKey(endpoint1) || !this.vertices.containsKey(endpoint2))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        final Integer hashCode = WeightedEdge.hashCode(endpoint1.hashCode(), endpoint2.hashCode());
        return this.edgeByHashCode.get(hashCode);
    }

    @Override
    public List<WeightedEdge<T>> getEdges(final T MazeNode) { // TODO retourner un Set ?
        final List<WeightedEdge<T>> edges = this.edgesByMazeNode.get(MazeNode);
        if (edges == null) throw new NoSuchElementException("Source node does not exist.");
        return edges;
    }

    private boolean computeIsEulerian() {
        for (final T MazeNode : this)
            if (this.getConnectedVerticeSet(MazeNode).size() % 2 == 1) return false;
        return true;
    }

    public boolean isEulerian() {
        if (this.isEulerian == null) this.isEulerian = this.computeIsEulerian();
        return this.isEulerian;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    // TODO NodeVisitor, EdgeVisitor
    public Set<T> getOddVertices() {// TODO unit tests
        final Set<T> oddVertices = Sets.newHashSet();
        for (final T MazeNode : this)
            if (this.getConnectedVerticeSet(MazeNode).size() % 2 == 1) oddVertices.add(MazeNode);
        return oddVertices;
    }

    /*
    private void debug(final double[][] array, final double infinity) {
        double max = 0;
        for (int i = 0; i < this.order; ++i)
            for (int j = 0; j < this.order; ++j)
                if (i != j && array[i][j] > max && array[i][j] != infinity) max = array[i][j];
        final int n = (int) Math.floor(Math.log10(Double.valueOf(max).intValue())) + 1;
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
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

}