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

import java.util.Map;
import java.util.Map.Entry;

import pocman.graph.UndirectedGraph;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

public final class NodeDegreeFunctions<T> {

    // TODO Extract to Integers
    private final static class isEven implements Predicate<Integer> {

        @Override
        public boolean apply(final Integer integer) {
            return integer % 2 == 0;

        }
    }

    // TODO Extract to Integers
    private final static class isSame implements Predicate<Integer> {

        private final Integer integer;

        public isSame(final int integer) {
            this.integer = integer;
        }

        @Override
        public boolean apply(final Integer integer) {
            return this.integer.equals(integer);

        }
    }

    // TODO Extract to Integers
    private final static Predicate<Integer> IS_EVEN = new isEven();

    // TODO Extract to Integers
    private final static Predicate<Integer> IS_ODD = Predicates.not(new isEven());

    private final UndirectedGraph<T> graph;
    private Map<T, Integer> data = null;

    public static <T> NodeDegreeFunctions<T> from(final UndirectedGraph<T> graph) {
        return new NodeDegreeFunctions<T>(graph);
    }

    private NodeDegreeFunctions(final UndirectedGraph<T> graph) {
        this.graph = graph;
    }

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    public void apply() {
        if (this.data == null) {
            final Builder<T, Integer> builder = new ImmutableMap.Builder<T, Integer>();
            for (final T node : this.getGraph())
                builder.put(node, this.getGraph().getConnectedVerticeSet(node).size());
            this.data = builder.build();
        }
    }

    public Map<T, Integer> getData() {
        this.apply();
        return this.data;
    }

    public Map<T, Integer> getNodesWithOddDegree() {
        return Maps.filterValues(this.getData(), IS_ODD);
    }

    public Map<T, Integer> getNodesWithEvenDegree() {
        return Maps.filterValues(this.getData(), IS_EVEN);
    }

    public Map<T, Integer> getNodesWithDegree(final int degree) {
        return Maps.filterValues(this.getData(), new isSame(degree));
    }

    public static void main(final String[] args) {

        final UndirectedGraph<String> graph = new UndirectedGraph.Builder<String>(4)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .addEdge("A", "D", 1.0)
                .build();

        final NodeDegreeFunctions<String> nodeDegreeVisitor = NodeDegreeFunctions.from(graph);

        final Map<String, Integer> data = nodeDegreeVisitor.getData();
        for (final Entry<String, Integer> entry : data.entrySet())
            System.out.println(entry);

        System.out.println();

        final Map<String, Integer> nodesWithEvenDegree = nodeDegreeVisitor.getNodesWithEvenDegree();
        for (final Entry<String, Integer> entry : nodesWithEvenDegree.entrySet())
            System.out.println(entry);

        System.out.println();

        final Map<String, Integer> nodesWithOddDegree = nodeDegreeVisitor.getNodesWithOddDegree();
        for (final Entry<String, Integer> entry : nodesWithOddDegree.entrySet())
            System.out.println(entry);

        System.out.println();

        final Map<String, Integer> nodesOfDegree1 = nodeDegreeVisitor.getNodesWithDegree(1);
        for (final Entry<String, Integer> entry : nodesOfDegree1.entrySet())
            System.out.println(entry);
    }

}