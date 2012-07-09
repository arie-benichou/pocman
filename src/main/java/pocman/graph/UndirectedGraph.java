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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import pocman.graph.functions.NodeDegreeFunctions;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

// TODO ? vertex x = null edge of (x,x)
public final class UndirectedGraph<T> implements Iterable<T> {

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

        public int getOrder() {
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
    private final Map<T, Set<WeightedEdge<T>>> edgesByEndpoint;
    //private final Map<Integer, WeightedEdge<T>> edgeByHashCode;
    private final Map<T, Set<T>> mGraph;
    private final Map<Integer, T> verticeByIndex;
    private final Path<T>[][] shortestPaths;

    private Boolean isConnected;
    //private Boolean isEulerian;
    private final Set<WeightedEdge<T>> edgeSet;

    @SuppressWarnings("unchecked")
    private Path<T>[][] buildPathMatrix() {
        final Path<T>[][] paths = new Path[this.order][this.order];
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                double weight;
                final T endPoint1 = this.verticeByIndex.get(i);
                final T endPoint2 = this.verticeByIndex.get(j);
                if (i == j) weight = 0.0;
                else weight = this.contains(endPoint1, endPoint2) ? this.getEdge(endPoint1, endPoint2).getWeight() : INFINITY;
                paths[i][j] = Path.from(endPoint1, endPoint2, weight);
            }
        }
        return paths;
    }

    private UndirectedGraph(final Builder<T> builder) {

        this.order = builder.order;
        this.vertices = ImmutableMap.copyOf(builder.vertices);
        this.edgeSet = ImmutableSet.copyOf(builder.edgeSet);

        final ImmutableMap.Builder<T, Set<T>> builder2 = new ImmutableMap.Builder<T, Set<T>>();
        for (final Entry<T, Set<T>> entry : builder.mGraph.entrySet()) {
            builder2.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
        }
        this.mGraph = builder2.build();

        final ImmutableMap<T, List<WeightedEdge<T>>> edgesByEndpoint = ImmutableMap.copyOf(builder.edgesByMazeNode);
        // safe copy
        final double[][] values = new double[this.order][this.order];
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                if (i == j) continue;
                final double value = builder.values[i][j];
                values[i][j] = value == 0 ? INFINITY : value;
            }
        }

        final ImmutableMap.Builder<T, Set<WeightedEdge<T>>> edgesByMazeNodeBuilder = new ImmutableMap.Builder<T, Set<WeightedEdge<T>>>();
        for (final Entry<T, List<WeightedEdge<T>>> entry : edgesByEndpoint.entrySet())
            edgesByMazeNodeBuilder.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
        this.edgesByEndpoint = edgesByMazeNodeBuilder.build();

        /*
        this.edgeByHashCode = Maps.newHashMap();
        for (final T MazeNode : this.vertices.keySet()) {
            for (final WeightedEdge<T> weightedEdge : this.getEdges(MazeNode)) {
                final Integer hashCode = WeightedEdge.hashCode(weightedEdge.getEndPoint1().hashCode(), weightedEdge.getEndPoint2().hashCode());
                this.edgeByHashCode.put(hashCode, weightedEdge);
            }
        }
        */

        this.verticeByIndex = Maps.newHashMap();
        for (final Entry<T, Integer> entry : this.vertices.entrySet())
            this.verticeByIndex.put(entry.getValue() - 1, entry.getKey());

        final Path<T>[][] paths = this.buildPathMatrix();

        // TODO extract Roy-Warshall-Floyd algorithm
        for (int k = 0; k < this.order; ++k) {
            for (int i = 0; i < this.order; ++i) {
                for (int j = 0; j < this.order; ++j) {
                    if (i == j) continue;
                    final double currentCost = paths[i][j].getWeight();
                    final double newCost = paths[i][k].getWeight() + paths[k][j].getWeight();
                    if (newCost < currentCost) paths[i][j] = paths[i][k].add(paths[k][j]);
                }
            }
        }

        this.shortestPaths = paths;
    }

    public boolean contains(final T endPoint) {
        Preconditions.checkArgument(endPoint != null);
        return this.vertices.containsKey(endPoint);
    }

    private void checkEndPoint(final T endPoint) {
        if (!this.contains(endPoint)) throw new NoSuchElementException("Node " + endPoint + "does not exist.");
    }

    public boolean contains(final T endPoint1, final T endPoint2) {
        this.checkEndPoint(endPoint1);
        this.checkEndPoint(endPoint2);
        return this.mGraph.get(endPoint1).contains(endPoint2);
    }

    public Path<T> getShortestPathBetween(final T endPoint1, final T endPoint2) {
        this.checkEndPoint(endPoint1);
        this.checkEndPoint(endPoint2);
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
        //if (this.isConnected == null) // TODO Thread Safe
        this.isConnected = this.computeIsConnected();
        return this.isConnected;
    }

    @Override
    public Iterator<T> iterator() {
        return this.vertices.keySet().iterator();
    }

    public Set<T> getEndPoints(final T endPoint) {
        this.checkEndPoint(endPoint);
        return this.mGraph.get(endPoint);
    }

    public WeightedEdge<T> getEdge(final T endPoint1, final T endPoint2) {
        this.checkEndPoint(endPoint1);
        this.checkEndPoint(endPoint2);
        //final Integer hashCode = WeightedEdge.hashCode(endPoint1.hashCode(), endPoint2.hashCode());
        //return this.edgeByHashCode.get(hashCode);
        final Set<WeightedEdge<T>> edges = this.getEdges(endPoint1);
        WeightedEdge<T> edge = null;
        for (final WeightedEdge<T> weightedEdge : edges) {
            if (weightedEdge.getEndPoint1().equals(endPoint2)) {
                edge = weightedEdge;
                break;
            }
            if (weightedEdge.getEndPoint2().equals(endPoint2)) {
                edge = weightedEdge;
                break;
            }
        }
        Preconditions.checkState(edge != null);
        return edge;
    }

    public Set<WeightedEdge<T>> getEdges(final T endPoint) {
        this.checkEndPoint(endPoint);
        return this.edgesByEndpoint.get(endPoint);
    }

    // TODO : strictly a graph is ALSO eulerian if it has two and only two endpoints having odd degree.
    public boolean isEulerian() {
        final Map<T, Integer> nodesWithOddDegree = this.getNodesWithOddDegree();
        return nodesWithOddDegree.isEmpty(); // || nodesWithOddDegree.size() == 2;
    }

    public int getOrder() {
        return this.order;
    }

    private volatile NodeDegreeFunctions<T> nodeDegreeFunctions = null;

    private NodeDegreeFunctions<T> getNodeDegreeFunctions() {
        NodeDegreeFunctions<T> value = this.nodeDegreeFunctions;
        if (value == null) {
            synchronized (this) {
                if ((value = this.nodeDegreeFunctions) == null) this.nodeDegreeFunctions = value = NodeDegreeFunctions.from(this);
            }
        }
        return value;
    }

    public Map<T, Integer> getNodesWithOddDegree() {
        return this.getNodeDegreeFunctions().getNodesWithOddDegree();
    }

    public Set<WeightedEdge<T>> getSetOfEdges() {
        return this.edgeSet;
    }

    public Map<T, Integer> getNodesHavingDegree(final int degree) {
        return this.getNodeDegreeFunctions().getNodesHavingDegree(degree);
    }

    public Map<T, Integer> getNodesNotHavingDegree(final int degree) {
        return this.getNodeDegreeFunctions().getNodesNotHavingDegree(degree);
    }

    public Map<T, Integer> getDegreeByNode() {
        return this.getNodeDegreeFunctions().getData();
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