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

package pocman.graph.functions;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class NodeOfDegree1Pruning<T> {

    private final NodeDegreeFunctions<T> nodeDegreeVisitor;

    private Map<T, Integer> data = null;
    private Set<T> remainingOddVertices;

    private Set<WeightedEdge<T>> doubledEdges;

    public static <T> NodeOfDegree1Pruning<T> from(final NodeDegreeFunctions<T> nodeDegreeVisitor) {
        return new NodeOfDegree1Pruning<T>(nodeDegreeVisitor);
    }

    private NodeOfDegree1Pruning(final NodeDegreeFunctions<T> nodeDegreeVisitor) {
        this.nodeDegreeVisitor = nodeDegreeVisitor;
    }

    public NodeDegreeFunctions<T> getNodeDegreeVisitor() {
        return this.nodeDegreeVisitor;
    }

    public void apply() {
        if (this.data == null) {
            final Map<T, Integer> deltas = Maps.newHashMap();
            final ImmutableSet.Builder<WeightedEdge<T>> doubledEdgesbuilder = new ImmutableSet.Builder<WeightedEdge<T>>();
            final Map<T, Integer> nodesWithDegree1 = this.getNodeDegreeVisitor().getNodesWithDegree(1);
            final List<T> nodes = Lists.newArrayList(nodesWithDegree1.keySet());
            for (final T node : nodes) {
                final WeightedEdge<T> edge = this.getNodeDegreeVisitor().getGraph().getEdges(node).get(0);
                Integer integer1 = deltas.get(edge.getEndPoint1());
                if (integer1 == null) integer1 = 0;
                deltas.put(edge.getEndPoint1(), integer1 + 1);
                Integer integer2 = deltas.get(edge.getEndPoint2());
                if (integer2 == null) integer2 = 0;
                deltas.put(edge.getEndPoint2(), integer2 + 1);
                doubledEdgesbuilder.add(edge);
            }
            final Builder<T, Integer> remainingOddVerticesBuilder = new ImmutableMap.Builder<T, Integer>();
            final Set<T> remainingOddVertices = Sets.newHashSet();
            for (final Entry<T, Integer> entry : this.getNodeDegreeVisitor().getData().entrySet()) {
                final T node = entry.getKey();
                final Integer degree = entry.getValue();
                Integer delta = deltas.get(node);
                if (delta == null) delta = 0;
                remainingOddVerticesBuilder.put(node, degree + delta);
                if ((degree + delta) % 2 == 1) remainingOddVertices.add(node);
            }
            this.data = remainingOddVerticesBuilder.build();
            this.doubledEdges = doubledEdgesbuilder.build();
            this.remainingOddVertices = remainingOddVertices;
        }
    }

    public Map<T, Integer> getData() {
        this.apply();
        return this.data;
    }

    public Set<T> getRemainingOddVertices() {
        this.apply();
        return this.remainingOddVertices;
    }

    public Set<WeightedEdge<T>> getDoubledEdges() {
        this.apply();
        return this.doubledEdges;
    }

    public boolean hasStillNodeWithOddDegree() {
        return !this.getRemainingOddVertices().isEmpty();
    }

    public static void main(final String[] args) {

        final UndirectedGraph<String> graph = new UndirectedGraph.Builder<String>(4)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .addEdge("A", "D", 1.0)
                .build();
        /*
        final UndirectedGraph<String> graph = new UndirectedGraph.Builder<String>(5)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .addEdge("A", "D", 1.0)
                .addEdge("B", "E", 1.0)
                .addEdge("A", "E", 1.0)
                .build();
        */
        final NodeDegreeFunctions<String> nodeDegreeVisitor = NodeDegreeFunctions.from(graph);

        {
            final Map<String, Integer> data = nodeDegreeVisitor.getData();
            for (final Entry<String, Integer> entry : data.entrySet())
                System.out.println(entry);
        }

        System.out.println();

        {
            final NodeOfDegree1Pruning<String> nodeOfDegree1Pruning = NodeOfDegree1Pruning.from(nodeDegreeVisitor);
            final Map<String, Integer> data = nodeOfDegree1Pruning.getData();
            for (final Entry<String, Integer> entry : data.entrySet())
                System.out.println(entry);

            final boolean isEulerianNow = !nodeOfDegree1Pruning.hasStillNodeWithOddDegree();
            System.out.println(isEulerianNow);

            System.out.println(nodeOfDegree1Pruning.getRemainingOddVertices());

            final Set<WeightedEdge<String>> doublesEdges = nodeOfDegree1Pruning.getDoubledEdges();
            for (final WeightedEdge<String> weightedEdge : doublesEdges)
                System.out.println(weightedEdge);

        }

        System.out.println();

    }

}